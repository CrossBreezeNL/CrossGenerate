package com.xbreeze.xgenerate.template.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.FileFormatConfig;
import com.xbreeze.xgenerate.config.template.TemplateAttributeInjection;
import com.xbreeze.xgenerate.config.template.TemplatePlaceholderInjection;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateCommentAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.scanner.AnnotationScanner;
import com.xbreeze.xgenerate.template.section.NamedTemplateSection;
import com.xbreeze.xgenerate.template.section.RawTemplateSection;
import com.xbreeze.xgenerate.template.section.SectionedTemplate;

public class XMLTemplatePreprocessor extends TemplatePreprocessor {
	
	/**
	 * Constructor.
	 * @param templateConfig The template config.
	 */
	public XMLTemplatePreprocessor(XGenConfig config) {
		super(config);
	}

	/**
	 * Sectionize the XML template and return the SectionedTemplate.
	 * @throws TemplatePreprocessorException 
	 */
	@Override
	protected SectionedTemplate sectionizeTemplate(RawTemplate rawTemplate, String rootSectionName) throws TemplatePreprocessorException {
		FileFormatConfig fileFormatConfig = _config.getTemplateConfig().getFileFormatConfig();
		logger.info(String.format("Performing xml sectionizing for '%s'.", rawTemplate.getRawTemplateFileName()));
		
		// Recursively loop through document and search nodes for section annotations.
		// A node marks a section when it's text value or one of it's attributes contains a section annotation
		// The node and it's child notes, as long as they are not part of a new section, are part of the raw template
		// TODO: this does not take into account text based sections in a XML template yet.
		
		// Initialize the DocumentBuilder.
		DocumentBuilder db;
		try {
			db = XMLUtils.getDocumentBuilder();
		} catch (GeneratorException e) {
			throw new TemplatePreprocessorException(e);
		}
		
		// Parse the text of the raw template into a Document.
		Document templateDocument;
		try {
			// Create an InputSource using the raw template content.
			InputSource rawTemplateInputSource = new InputSource(new StringReader(rawTemplate.getRawTemplateContent()));
			// Parse the template into a Document object.
			templateDocument = db.parse(rawTemplateInputSource);
		} catch (IOException e) {
			// When an IOException occurred the StringSource couldn't be read. This shouldn't happen.
			throw new TemplatePreprocessorException(String.format("Couldn't read the raw template content: %s", e.getMessage()));
		} catch (SAXException e) {
			// When a SAXException occurred, the template couldn't be parsed into a Document.
			throw new TemplatePreprocessorException(String.format("Couldn't parse the raw template document: %s", e.getMessage()));
		}
		
		//Perform template attribute injections on XML document
		performAttributeInjections(templateDocument, _config.getTemplateConfig().getTemplateAttributeInjections());
		
		// Build a list of sections that are specified in the template config, populate each section with the nodes matching the template section's XPath
		ArrayList<XMLTemplateSectionWithNodes> sectionsWithNodes = new ArrayList<>();
		if (_config.getTemplateConfig() != null && _config.getTemplateConfig().getSectionAnnotations() != null && _config.getTemplateConfig().getSectionAnnotations().size() > 0) {
			for (TemplateSectionAnnotation tsa : _config.getTemplateConfig().getSectionAnnotations()) {
				//Perform XPath and store result in nodes.
				try {
					// Evaluate the XPath of the annotation from the config on the current node.
					NodeList sectionNodes = (NodeList)XMLUtils.getXPath().evaluate(tsa.getTemplateXPath(), templateDocument, XPathConstants.NODESET);
					// When the nodes are found, create the XMLTemplateSectionWithNodes object.
					sectionsWithNodes.add(new XMLTemplateSectionWithNodes(tsa, sectionNodes));
				} catch (XPathExpressionException e) {
					throw new TemplatePreprocessorException(String.format("Error while searching for section nodes for section %s: %s", tsa.getName(),  e.getMessage()));
				}
			}
		}
		
		// If template placeholder injections are defined, create a set of them here.
		HashMap<Node, ArrayList<TemplatePlaceholderInjection>> nodeTemplatePlaceholderInjections = new HashMap<Node, ArrayList<TemplatePlaceholderInjection>>();
		if (_config.getTemplateConfig() != null && _config.getTemplateConfig().getTemplatePlaceholderInjections() != null && _config.getTemplateConfig().getTemplatePlaceholderInjections().size() > 0) {
			for (TemplatePlaceholderInjection tpi : _config.getTemplateConfig().getTemplatePlaceholderInjections()) {
				//Perform XPath and store result in nodes.
				try {
					// Evaluate the XPath of the annotation from the config on the current node.
					NodeList placeholderInjectionNodes = (NodeList)XMLUtils.getXPath().evaluate(tpi.getTemplateXPath(), templateDocument, XPathConstants.NODESET);
					for (int i=0; i<placeholderInjectionNodes.getLength(); i++) {
						Node currentNode = placeholderInjectionNodes.item(i);
						// If the node is not yet in the hashmap, add it.
						if (!nodeTemplatePlaceholderInjections.containsKey(currentNode))
							nodeTemplatePlaceholderInjections.put(currentNode, new ArrayList<TemplatePlaceholderInjection>());
						// Add the template placeholder injection to the node tpi set.
						nodeTemplatePlaceholderInjections.get(currentNode).add(tpi);
					}
				} catch (XPathExpressionException e) {
					throw new TemplatePreprocessorException(String.format("Error while searching for template placeholder injection nodes for %s: %s", tpi.getTemplateXPath(),  e.getMessage()));
				}
			}
		}
		
		// Initialize the sectionized template.
		SectionizedXMLTemplate sectionizedXMLTemplate = new SectionizedXMLTemplate(rootSectionName);
		
		// Add the XML declaration to the template.
		String xmlVersion = (templateDocument.getXmlVersion() != null) ? templateDocument.getXmlVersion() : "1.0";
		String xmlEncoding = (templateDocument.getXmlEncoding() != null) ? templateDocument.getXmlEncoding() : "UTF-8";
		String xmlDeclaration = XMLUtils.excapeXMLChars(String.format("<?xml version=\"%s\" encoding=\"%s\"?>%s", xmlVersion, xmlEncoding, System.lineSeparator()));
		sectionizedXMLTemplate.addTemplateSection(new RawTemplateSection(xmlDeclaration, 0, xmlDeclaration.length()));
		
		// Add the doctype to the template.
		if (templateDocument.getDoctype() != null) {
			String docTypePart = XMLUtils.excapeXMLChars(getDocTypeString(templateDocument.getDoctype()));
			sectionizedXMLTemplate.addTemplateSection(new RawTemplateSection(docTypePart, 0, docTypePart.length()));
		}
		
		// Now parse the content of the template into sections, starting with the document element as the root element.
		// Document itself (also a node) is always root section
		processTemplateNode(templateDocument.getDocumentElement(), sectionizedXMLTemplate, fileFormatConfig, sectionsWithNodes, nodeTemplatePlaceholderInjections);
         
		// Return the PreprocessedXMLTemplate
		return sectionizedXMLTemplate;
	}
	
	/**
	 * Function to get the doctype string from a DocumentType node.
	 * Created based on example functions: https://www.programcreek.com/java-api-examples/org.w3c.dom.DocumentType
	 * @param node The DocumentType node
	 * @return The strint representation of the DocumentType.
	 */
	private String getDocTypeString(DocumentType node) {
		StringBuffer docTypeString = new StringBuffer();
		docTypeString.append("<!DOCTYPE "); //$NON-NLS-1$
		docTypeString.append(node.getName());
	    String publicId = node.getPublicId();
	    String systemId = node.getSystemId();
	    if (publicId != null) {
	    	docTypeString.append(" PUBLIC \""); //$NON-NLS-1$
	    	docTypeString.append(publicId);
	    	docTypeString.append("\" \""); //$NON-NLS-1$
	    	docTypeString.append(systemId);
	    	docTypeString.append('\"');
	    } else if (systemId != null) {
	    	docTypeString.append(" SYSTEM \""); //$NON-NLS-1$
	    	docTypeString.append(systemId);
	    	docTypeString.append('"');
	    }

	    String internalSubset = node.getInternalSubset();
	    if (internalSubset != null) {
	    	docTypeString.append(" [").append(System.lineSeparator()); //$NON-NLS-1$
	    	docTypeString.append(internalSubset);
	    	docTypeString.append(']');
	    }
	    docTypeString.append('>').append(System.lineSeparator());
	    
	    return docTypeString.toString();
	}
	
	/**
	 * Performs attribute injection on the template document
	 * @param xmlDoc
	 * @param templateAttributeInjections
	 * @throws TemplatePreprocessorException
	 */
	private void performAttributeInjections(Document xmlDoc, ArrayList<TemplateAttributeInjection> templateAttributeInjections) throws TemplatePreprocessorException {
		if (templateAttributeInjections != null) {
			for (TemplateAttributeInjection tai: templateAttributeInjections){
				try {
					NodeList templateNodes = (NodeList)XMLUtils.getXPath().evaluate(tai.get_parentNodeXPath(), xmlDoc, XPathConstants.NODESET);
					for (int i = 0; i < templateNodes.getLength(); i++) {
						if (templateNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element templateNode = (Element)templateNodes.item(i);
							//only inject attribute if it does not already exist
							if (!templateNode.hasAttribute(tai.get_attributeName())) {
								templateNode.setAttribute(tai.get_attributeName(), tai.get_defaultValue());
							}
						}
					}
				} catch (XPathExpressionException e) {
					throw new TemplatePreprocessorException(String.format("Error while processing template attribute injection for xpath %s: %s", tai.get_parentNodeXPath(),  e.getMessage())); 
				}
			}
		}
	}
	/**
	 * Perform placeholder injection for the current node.
	 * @param placeholderName
	 * @param templateNode
	 * @param nodeTemplatePlaceholderInjections
	 * @throws TemplatePreprocessorException
	 */
	private void performPlaceholderInjections(String placeholderName, Node templateNode, HashMap<Node, ArrayList<TemplatePlaceholderInjection>> nodeTemplatePlaceholderInjections) throws TemplatePreprocessorException {
		// If there are placeholder injections defined for the current node, execute them.
		if (nodeTemplatePlaceholderInjections.containsKey(templateNode)) {
			for (TemplatePlaceholderInjection tpi : nodeTemplatePlaceholderInjections.get(templateNode)) {
				logger.info(String.format("Placeholder injection defined for current node: %s -> %s", templateNode.getNodeName(), tpi.getTemplateXPath()));
				templateNode.setTextContent(getPlaceholderInjectionValue(placeholderName, tpi));
			}
		}
		
		// If there are placeholder injections defined for the attributes of the current node, execute them.
		for (int i=0; i<templateNode.getAttributes().getLength(); i++) {
			Node attributeNode = templateNode.getAttributes().item(i);
			if (nodeTemplatePlaceholderInjections.containsKey(attributeNode)) {
				for (TemplatePlaceholderInjection tpi : nodeTemplatePlaceholderInjections.get(attributeNode)) {
					logger.info(String.format("Placeholder injection defined for current node attribute: %s -> %s", attributeNode.getNodeName(), tpi.getTemplateXPath()));
					attributeNode.setNodeValue(getPlaceholderInjectionValue(placeholderName, tpi));
				}
			}
		}
	}
	
	/**
	 * Get the replacement value for the placeholder injection.
	 * @param placeholderName The placeholder name.
	 * @param tpi The template placeholder injection object.
	 * @return The new string value for the node.
	 * @throws TemplatePreprocessorException
	 */
	private String getPlaceholderInjectionValue(String placeholderName, TemplatePlaceholderInjection tpi) throws TemplatePreprocessorException {
		String accessor;
		switch (tpi.getScope()) {
			case current:
				accessor = _config.getTemplateConfig().getFileFormatConfig().getCurrentAccessor();
				break;
			case child:	
				accessor = _config.getTemplateConfig().getFileFormatConfig().getChildAccessor();
				break;
			default:
				throw new TemplatePreprocessorException(String.format("Unrecognized scope defined in TemplatePlaceholderInjection '%s'", tpi.getScope().toString()));
		}
		return String.format("%s%s%s", placeholderName, accessor, tpi.getModelNode());
	}
	
	/**
	 * Process the template node.
	 * @param templateNode
	 * @param parentSection
	 * @param fileFormatConfig
	 * @param sectionsFromConfigWithNodes
	 * @param nodeTemplatePlaceholderInjections
	 * @throws TemplatePreprocessorException
	 */
	private void processTemplateNode(Node templateNode, NamedTemplateSection parentSection, FileFormatConfig fileFormatConfig, ArrayList<XMLTemplateSectionWithNodes> sectionsFromConfigWithNodes, HashMap<Node, ArrayList<TemplatePlaceholderInjection>> nodeTemplatePlaceholderInjections) throws TemplatePreprocessorException {
		// Scan attributes and text content for section annotation
		// If not found, add node as raw section to parent and process child nodes
		
		// Create a local variable for the current section.
		// This can be overridden when in the annotation a section annotation is found.
		NamedTemplateSection currentSection = parentSection;
		
		// First check if the node matches one of the sections from the config, if so create a new named section
		boolean sectionFromConfigFound = false;
		for (XMLTemplateSectionWithNodes tsw : sectionsFromConfigWithNodes) {
			if (tsw.containsNode(templateNode)) {
				logger.info(String.format("Section match from config found for template node '%s' and section %s", templateNode.getNodeName(), tsw.getName()));				
				if (sectionFromConfigFound) {
					throw new TemplatePreprocessorException(String.format("Multiple section annotations found from config on node '%s', only 1 allowed", templateNode.getNodeName()));
				}
				currentSection = new NamedTemplateSection(tsw.getName(), 0, tsw.getTemplateSectionAnnotation());
				parentSection.addTemplateSection(currentSection);
				sectionFromConfigFound = true;
			}
		}
		
		// If the comment node XPath is specified in the config, we can search for annotation in these nodes.
		// TODO WO: Means this has to be specified in config
		if (fileFormatConfig.getCommentNodeXPath() != null) {
			//logger.info("The comment node XPath is specified, so we will search for annotations there.");
			Node annotationNode;
			try {
				// Evaluate the XPath of the CommentNode from the config on the current node.
				annotationNode = (Node)XMLUtils.getXPath().evaluate(fileFormatConfig.getCommentNodeXPath(), templateNode, XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				throw new TemplatePreprocessorException(String.format("Error while searching for annotations on node '%s': %s", templateNode.getNodeName(), e.getMessage()));
			}

			// Store the value of the comment node in a string.
			String annotationNodeTextContent = null;
			// Attribute
			if (annotationNode != null && annotationNode instanceof Attr) {
				annotationNodeTextContent = annotationNode.getNodeValue();
			}
			// Element
			else if (annotationNode != null && annotationNode.getTextContent() != null) {
				annotationNodeTextContent = annotationNode.getTextContent();
			}
			
			// If the annotation node was found and it contains something, search for the annotations.
			if (annotationNodeTextContent != null && annotationNodeTextContent.length() > 0) {
				logger.info(String.format("The annotation node was found on the current element (%s -> %s -> '%s')", templateNode.getNodeName(), annotationNode.getNodeName(), annotationNodeTextContent));
				// Find the annotations in the comment node.
				ArrayList<TemplateAnnotation> collectedAnnotations = AnnotationScanner.collectAnnotations(annotationNodeTextContent, "", fileFormatConfig.getAnnotationPrefix(), fileFormatConfig.getAnnotationArgsPrefix(), fileFormatConfig.getAnnotationArgsSuffix(), "");
				// If annotations are found on the node, strip its annotations text.
				if (collectedAnnotations.size() > 0) {
					logger.info(String.format("Found %d annotations on the current element (%s -> %s)", collectedAnnotations.size(), templateNode.getNodeName(), annotationNode.getNodeName()));
					StringBuffer strippedAnnotationNodeTextContentBuffer = new StringBuffer();
					// The collection of annotations can contain multiple annotations, for example Section mixed with Comment.
					// There can only be 1 Section annotation on a node, see keep a state.
					boolean foundSectionAnnotation = false;
					int lastAnnotationTextEndIndex = 0;
					for (TemplateAnnotation annotation : collectedAnnotations) {
						// If there was text between the last annotation and the next, store it in the stripped string.
						if (annotation.getAnnotationBeginIndex() > lastAnnotationTextEndIndex) {
							strippedAnnotationNodeTextContentBuffer.append(annotationNodeTextContent.substring(lastAnnotationTextEndIndex, annotation.getAnnotationBeginIndex()));
						}
						
						// Add the annotation to the sectionized template.
						// TemplateCommentAnnotation
						if (annotation instanceof TemplateCommentAnnotation) {
							// Don't do anything for the comment annotation.
							// TODO We could add the comment as a section in the sectionized template.
						}
						
						// TemplateSectionAnnotation
						else if (annotation instanceof TemplateSectionAnnotation) {
							//If a section was already found from the config, throw an error since it is not allowed to add sections for the same node from config and template as well
							if (sectionFromConfigFound)
								throw new TemplatePreprocessorException(String.format("Found section from config as well as fron template for the same node: '%s'. This is not allowed.", templateNode.getNodeName()));
							
							// If a section annotation was already found, throw an exception since there can only be 1.
							if (foundSectionAnnotation)
								throw new TemplatePreprocessorException(String.format("Multiple section annotations found on node '%s', only 1 allowed", templateNode.getNodeName()));
							
							// Store the template section annotation in a local variable.
							TemplateSectionAnnotation tsa = (TemplateSectionAnnotation) annotation;
							logger.info(String.format("Found section annotation '%s' on node '%s'", tsa.getName(), templateNode.getNodeName()));
							// Add the named template using the section name in the annotation.
							currentSection = new NamedTemplateSection(tsa.getName(), 0, tsa);
							parentSection.addTemplateSection(currentSection);
							
							// Set the indicator that a section annotation was found.
							foundSectionAnnotation = true;
						}
						
						// If another annotation was found, we throw an exception since its not supported (yet).
						else {
							throw new TemplatePreprocessorException(String.format("Unsupported annotation specified: '%s' in node '%s'", annotation.getAnnotationName(), templateNode.getNodeName()));
						}
						
						lastAnnotationTextEndIndex = annotation.getAnnotationEndIndex();
					}
					
					// If there is still some text after the last annotation, add it to the stripped annotation node text buffer.
					if (lastAnnotationTextEndIndex < annotationNodeTextContent.length()) {
						strippedAnnotationNodeTextContentBuffer.append(annotationNodeTextContent.substring(lastAnnotationTextEndIndex, annotationNodeTextContent.length()));
					}
					
					// Now update the annotation text content, without the annotations.
					// Attribute
					if (annotationNode != null && annotationNode instanceof Attr) {
						annotationNode.setNodeValue(strippedAnnotationNodeTextContentBuffer.toString());
					}
					// Element
					else {
						annotationNode.setTextContent(strippedAnnotationNodeTextContentBuffer.toString());
					}
				}
			}
		}
		
		// Perform the placeholder injections (very important this is done after the section detection!).
		// TODO Currently a placeholder is written into the injected value, so in the XSLT appender we can replace it with the actual placeholder in that section.
		performPlaceholderInjections(NamedTemplateSection.PLACEHOLDER_PLACEHOLDER_NAME, templateNode, nodeTemplatePlaceholderInjections);
		
		// Count the number of child nodes.
		int childCount = templateNode.getChildNodes().getLength();
		
		// Add the node's 'head' as a raw section to the currentSection, then process children and then add nodes tail if there where any child-nodes.
		// If the current element has no children, we add the whole content of the element as a raw template. 
		if (childCount == 0) {
			// Add the content of the node as a raw template section.
			String escapedTemplateNodeContent = XMLUtils.excapeXMLChars(getNodeString(templateNode));
			currentSection.addTemplateSection(new RawTemplateSection(escapedTemplateNodeContent, 0, escapedTemplateNodeContent.length()));
		}
		
		// If there are children, we first add the head as a raw template, add the children and then add the tail.
		else {
			// Create a copy of the templateNode without descendants.
			Node currentNodeWithoutDescendants = copyNode(templateNode);
			// Use copy to get the XML of the node in text, without any child nodes
			String templateNodeContent = getNodeString(currentNodeWithoutDescendants);
			// Split the content in head and tail parts.
			SplittedXMLNode templateNodeSplit = splitNodeBodyAndTail(templateNode.getNodeName(), templateNodeContent);
			
			// Add the content of the head as a raw template section.
			String escapedTemplateHeadContent = XMLUtils.excapeXMLChars(templateNodeSplit.getHead());
			currentSection.addTemplateSection(new RawTemplateSection(escapedTemplateHeadContent, 0, escapedTemplateHeadContent.length()));
			
			// Loop through the children and handle there content recursively.
			for (int i = 0; i < childCount; i++) {
				Node childNode = templateNode.getChildNodes().item(i);
				
				if (childNode instanceof Element) {
					//logger.info(String.format("Processing element '%s' -> child element '%s'", templateNode.getNodeName(), childNode.getNodeName()));
					Element childElement = (Element)templateNode.getChildNodes().item(i);
					processTemplateNode(childElement, currentSection, fileFormatConfig, sectionsFromConfigWithNodes, nodeTemplatePlaceholderInjections);
				}
				
				// Add a text node as a raw template section.
				//this would make it double, since it is already included with in the escaoedTemplateHeadContent
				// TODO HW Wouldn't it make sense if to original order of the elements is adhered? I can have a text node somewhere between element nodes right?
				/*else if (childNode instanceof Text) {
					//logger.info(String.format("Processing element '%s' -> child text '%s'", templateNode.getNodeName(), childNode.getNodeName()));
					Text childText = (Text)childNode;
					String childTextContent = excapeXMLChars(childText.getData()); 
					currentSection.addTemplateSection(new RawTemplateSection(childTextContent, 0, childTextContent.length()));
				}*/
				// If we encounter another type of child elements, lets log it as a severe.
				// Let's not log text and cdata nodes since they are covered in the copy node, and each element has a chhild text node, usually empty				 
	            else if (childNode.getNodeType() != Node.TEXT_NODE && childNode.getNodeType() != Node.CDATA_SECTION_NODE){
					logger.severe(String.format("Skipping element '%s' -> child node '%s'", templateNode.getNodeName(), childNode.getNodeName()));
				}
			}
			
			// Add the content of the tail as a raw template section.
			String escapedTemplateTailContent = XMLUtils.excapeXMLChars(templateNodeSplit.getTail());
			currentSection.addTemplateSection(new RawTemplateSection(escapedTemplateTailContent, 0, escapedTemplateTailContent.length()));			
		}
	}
	
	/***
	 * Gets the textual representation of an XML node, consisting of its attribute values and text or CDATA content.
	 * @param xmlNode the XML node that is input
	 * @return the textual content of templateNode.
	 */
	private String getNodeString(Node xmlNode) {		
		StringWriter sw = new StringWriter();		
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer t = transformerFactory.newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(xmlNode), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}
	
	/***
	 * Splits the XML node in the part before child nodes (body) and the part after the child nodes (tail
	 * @param nodeName The name of the node
	 * @param nodeContent Textual representation of XML node
	 * @return An ArrayList with the node's body and tail
	 * @throws TemplatePreprocessorException 
	 */
	private SplittedXMLNode splitNodeBodyAndTail(String nodeName, String nodeContent) throws TemplatePreprocessorException {
		String nodeEnd = "</" + nodeName +">";
		// If the node has children it is a complex type xml element.
		if (nodeContent.endsWith(nodeEnd)) {
			return new SplittedXMLNode(nodeContent.substring(0, nodeContent.length() - nodeEnd.length()), nodeEnd);
		}
		// If the node does not have children it is a simple xml element.
		// node will end with />
		else {
			// Strip of the /> and add the >.
			return new SplittedXMLNode(nodeContent.substring(0, nodeContent.length() - 2) + ">", nodeEnd);
		}
	}
	
	/**
	 * Class which holds the head and teal of a XML Node with children.
	 */
	private class SplittedXMLNode {
		private String _head;
		private String _tail;
		
		public SplittedXMLNode(String head, String tail) {
			this._head = head;
			this._tail = tail;
		}

		/**
		 * @return the head
		 */
		public String getHead() {
			return _head;
		}

		/**
		 * @return the tail
		 */
		public String getTail() {
			return _tail;
		}
	}
	
	/***
	 * Creates a copy of an XML node, but without it's descendants
	 * @param fromNode the node to copy
	 * @return returns a copy of the fromNode without descendants.
	 */
	private Node copyNode(Node fromNode) {
		// A non-deep clone of the element (without children.
		Node nodeClone = fromNode.cloneNode(false);
		
		// Copy the text content over to the clone (if there was any).
		// WO: getTextContent also copies textcontent of child nodes, this is not desired, therefore text and cdata nodes are explicitly copied
		// https://docs.oracle.com/javase/7/docs/api/org/w3c/dom/Node.html#getTextContent()
		//if (fromNode.getTextContent() != null && fromNode.getTextContent().trim().length() > 0)
		//	nodeClone.setTextContent(fromNode.getTextContent());
		
        // Copy TEXT and CDATA sections if present.		
        for (int i = 0; i < fromNode.getChildNodes().getLength();i++)
        {   
            Node child = fromNode.getChildNodes().item(i);
            
            if (child.getNodeType() == Node.TEXT_NODE) {
                 if (!child.getTextContent().trim().equals("")) {
                    Node tn = fromNode.getOwnerDocument().createTextNode(child.getTextContent());
                    nodeClone.appendChild(tn);                   
                 }
            }
            //Copy character data if any
            if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                 //Node tn = fromNode.getOwnerDocument().createCDATASection(dn.getData());
                 Node tn = fromNode.getOwnerDocument().createTextNode("<![CDATA[".concat(child.getTextContent()).concat("]]>"));
                 nodeClone.appendChild(tn);
            }
        }
        
		return nodeClone;
	}

}
