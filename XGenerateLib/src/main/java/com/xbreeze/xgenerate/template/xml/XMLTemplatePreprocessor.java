package com.xbreeze.xgenerate.template.xml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.FileFormatConfig;
import com.xbreeze.xgenerate.config.template.TemplateAttributeInjection;
import com.xbreeze.xgenerate.config.template.TemplatePlaceholderInjection;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.template.PreprocessedTemplate;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionBoundsAnnotation;
import com.xbreeze.xgenerate.template.scanner.AnnotationScanner;
import com.xbreeze.xgenerate.template.section.NamedTemplateSection;
import com.ximpleware.AutoPilot;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

public class XMLTemplatePreprocessor extends TemplatePreprocessor {
	
	/**
	 * Constructor.
	 * @param templateConfig The template config.
	 */
	public XMLTemplatePreprocessor(XGenConfig config) {
		super(config);
	}

	/**
	 * Create the PreproceddedTemplate for the XML template.
	 * @throws TemplatePreprocessorException 
	 */
	@Override
	protected PreprocessedTemplate getPreprocessedTemplate(RawTemplate rawTemplate) throws TemplatePreprocessorException {
		logger.info(String.format("Creating pre-processed template for '%s'.", rawTemplate.getRawTemplateFileName()));

		FileFormatConfig fileFormatConfig = _config.getTemplateConfig().getFileFormatConfig();
		
		// Store the template in a String so it can be updated while some modifications are done.
		String preprocessedTemplate = rawTemplate.getRawTemplateContent();
		
		// First perform all modifications on the XML document (like attribute and placeholder injection).
		// This is to make sure the document doesn't change anymore when sectionizing, since character indexes are stored.
		
		// Perform template attribute injections on XML document (if defined).
		if (_config.getTemplateConfig() != null
				&& _config.getTemplateConfig().getTemplateAttributeInjections() != null
				&& _config.getTemplateConfig().getTemplateAttributeInjections().size() > 0) 
		{
			preprocessedTemplate = performAttributeInjections(preprocessedTemplate, _config.getTemplateConfig().getTemplateAttributeInjections());
		}
		
		// Perform the template placeholder injections on the XML document (if defined).
		if (_config.getTemplateConfig() != null
				&& _config.getTemplateConfig().getTemplatePlaceholderInjections() != null
				&& _config.getTemplateConfig().getTemplatePlaceholderInjections().size() > 0) 
		{
			preprocessedTemplate = performPlaceholderInjections(preprocessedTemplate, _config.getTemplateConfig().getTemplatePlaceholderInjections());
		}
		
		// Now all modifications are done we can sectionize the XML document using the sections defined
		// - in the config
		// - in the template annotations.
		
		// Create an array to store the section bounds in.
		ArrayList<TemplateAnnotation> templateAnnotations = new ArrayList<TemplateAnnotation>();
		
		// Create a VTDNav for navigating the document.
		VTDNav nv;
		try {
			nv = XMLUtils.getVTDNav(preprocessedTemplate);
		} catch (GeneratorException e) {
			throw new TemplatePreprocessorException(e);
		}
		
		// Create the xml sections using the sections from the config.
		if (_config.getTemplateConfig() != null
				&& _config.getTemplateConfig().getSectionAnnotations() != null
				&& _config.getTemplateConfig().getSectionAnnotations().size() > 0)
		{
			logger.info("Processing sections defined in config.");
			
			// Loop through the template attribute injections and apply them.
			for (TemplateSectionAnnotation sa : _config.getTemplateConfig().getSectionAnnotations()) {
				logger.info(String.format("Processing section '%s' defined in the config.", sa.getName()));
				// Create an AutoPilot for querying the document.
				AutoPilot ap = new AutoPilot(nv);
				int sectionNodesFound = 0;
				// Search for the node the section represents.
				try {
					// Set the XPath expression from the config.
					ap.selectXPath(sa.getTemplateXPath());
					// Execute the XPath expression and loop through the results.
			        while ((ap.evalXPath()) != -1) {
			        	// Increase the section nodes found.
			        	++sectionNodesFound;
			        	
			        	// Get the element offset and length (including whitespaces).
			        	long elementOffset = nv.expandWhiteSpaces(nv.getElementFragment(), VTDNav.WS_LEADING);
			        	int contentOffset = (int)elementOffset;
			        	int contentLength = (int)(elementOffset>>32);
			        	
			        	// Create a new TemplateSectionBoundsAnnotation using the annotation and offset.
			        	TemplateSectionBoundsAnnotation tsba = new TemplateSectionBoundsAnnotation(sa, contentOffset);
			        	// Set the end index.
			        	tsba.setAnnotationEndIndex(contentOffset + contentLength);
			        	// Add the bounds to the collection.
			        	templateAnnotations.add(tsba);
			        	logger.info(String.format("Section '%s' bounds found (%d:%d)", sa.getName(), tsba.getAnnotationBeginIndex(), tsba.getAnnotationEndIndex()));
			        }
				}
				catch (XPathParseException | XPathEvalException | NavException e) {
					throw new TemplatePreprocessorException(String.format("Error while processing template section annotation for XPath %s: %s", sa.getTemplateXPath(),  e.getMessage()));
				}
				
				// If there are no nodes found for this section, log a severe error.
				if (sectionNodesFound == 0)
					logger.severe(String.format("No template nodes found for section '%s' using XPath '%s'", sa.getName(), sa.getTemplateXPath()));
			}
		}
		
		// Scan the XML document for annotations (if the comment node XPath is defined).
		// TODO: this does not take into account text based sections in a XML template yet.
		if (_config.getTemplateConfig() != null 
				&& fileFormatConfig != null 
				&& fileFormatConfig.getCommentNodeXPath() != null 
				&& fileFormatConfig.getCommentNodeXPath().length() > 0)
		{
			logger.info("Scanning template for annotations.");
			
			// Create an AutoPilot for querying the document.
			AutoPilot annotatedElementAp = new AutoPilot(nv);
			// Find all elements with annotations, where the XPath is -> //*[contains(<<element-or-attribute-selection>>, '<<annotation-prefix>>')]
			// For example for SSIS we use @*[lower-case(local-name())='description'] to select all attributes (namespace and case insensitive) with the name 'description'.
			try {
				annotatedElementAp.selectXPath(String.format("//*[contains(%s, '%s')]", fileFormatConfig.getCommentNodeXPath(), fileFormatConfig.getAnnotationPrefix()));
		        while ((annotatedElementAp.evalXPath()) != -1) {
		        	// Get the annotation node value.
	        		// Clone the navigation object.
		        	VTDNav annotationNav = nv.cloneNav();
		        	// Create an AutoPilot for selecting the annotation node.
		        	AutoPilot annotationAp = new AutoPilot(annotationNav);
		        	annotationAp.selectXPath(fileFormatConfig.getCommentNodeXPath());
		        	// We don't need to check whether the index is -1, since the parent XPath is filtering on this node existing.
		        	// There can also be only 1 node, so we don't need to loop on the result.
		        	// The annotation node can be either an attribute or an element.
		        	// When it is an attribute, we take the attribute value and if it is a element we take the element text.
		        	int annotationNodeIndex = annotationAp.evalXPath();
		        	int annotationValueIndex = -1;
		        	switch (nv.getTokenType(annotationNodeIndex)) {
			        	case VTDNav.TOKEN_ATTR_NAME:
			        		annotationValueIndex = annotationNodeIndex + 1;
			        		break;
			        	case VTDNav.TOKEN_STARTING_TAG:
			        		annotationValueIndex = annotationNav.getText();
			        		break;
			        	case VTDNav.TOKEN_CHARACTER_DATA:
			        		annotationValueIndex = annotationNodeIndex;
			        		break;
		        		default:
		        			throw new TemplatePreprocessorException(String.format("Found unsupported XML node type for annotations usung XPath '%s' at %d.", fileFormatConfig.getCommentNodeXPath(), annotationNodeIndex));
		        	}
		        	// If the annotation value index isn't found, throw an exception.
		        	if (annotationValueIndex == -1)
		        		throw new TemplatePreprocessorException(String.format("Error while getting annotation value for XPath '%s' at %d.", fileFormatConfig.getCommentNodeXPath(), annotationNodeIndex));
		        	
		        	// Get the annotation value.
		        	String annotationAttributeValue = annotationNav.toString(annotationValueIndex);
		        	int annotationValueStartIndex = (int)annotationNav.getTokenOffset(annotationValueIndex);
					int annotationValueEndIndex = annotationValueStartIndex + annotationNav.getTokenLength(annotationValueIndex);
					logger.fine(String.format("Found annotation node value '%s'; start=%d; end=%d", annotationAttributeValue, annotationValueStartIndex, annotationValueEndIndex));
					
					ArrayList<TemplateAnnotation> foundInlineAnnotations = AnnotationScanner.collectInlineAnnotations(preprocessedTemplate, fileFormatConfig, annotationValueStartIndex, annotationValueEndIndex);
		        	
					for (TemplateSectionAnnotation tsa : foundInlineAnnotations.stream().filter(sa -> sa instanceof TemplateSectionAnnotation).toArray(TemplateSectionAnnotation[]::new)) {
						// Get the element offset and length (including whitespaces).
						long elementOffset = nv.expandWhiteSpaces(nv.getElementFragment(), VTDNav.WS_LEADING);
						int sectionBoundsStart = (int)elementOffset;
						int sectionBoundsEnd = sectionBoundsStart + (int)(elementOffset>>32);
						logger.info(String.format("Found template section bounds for section %s: start=%d; end=%d", tsa.getName(), sectionBoundsStart, sectionBoundsEnd));
						
						// Create the template section bounds annotation.
						TemplateSectionBoundsAnnotation tsba = new TemplateSectionBoundsAnnotation(tsa, sectionBoundsStart);
						tsba.setAnnotationEndIndex(sectionBoundsEnd);
						foundInlineAnnotations.add(tsba);
					}
		        	
					// Add all found annotations to the template list of annotations.
					templateAnnotations.addAll(foundInlineAnnotations);
		        }
			} catch (XPathParseException | XPathEvalException | NavException e) {
				throw new TemplatePreprocessorException("Error while processing template annotations.", e);
			}
		}
		
		// Return the pre-processed template.
		return new PreprocessedTemplate(preprocessedTemplate, templateAnnotations);
	}
	
	/**
	 * Performs attribute injection on the template document
	 * @param xmlDoc
	 * @param templateAttributeInjections
	 * @throws TemplatePreprocessorException
	 */
	private String performAttributeInjections(String template, ArrayList<TemplateAttributeInjection> templateAttributeInjections) throws TemplatePreprocessorException {
		logger.info("Performing template attribute injections.");
		
		// Create a VTDNav for navigating the document.
		VTDNav nv;
		try {
			nv = XMLUtils.getVTDNav(template);
		} catch (GeneratorException e) {
			throw new TemplatePreprocessorException(e);
		}
		
		// Create the XMLModifier for modifying the document.
		XMLModifier xm;
		try {
			xm = new XMLModifier(nv);
		} catch (ModifyException e) {
			throw new TemplatePreprocessorException("Error while initializing XMLModifier.", e);
		}
		
		// Loop through the template attribute injections and apply them.
		for (TemplateAttributeInjection tai: templateAttributeInjections){
			// Create an AutoPilot for querying the document.
			AutoPilot ap = new AutoPilot(nv);
			
			// Search for the element to inject an attribute on.
			try {
				// Set the XPath expression from the config.
				ap.selectXPath(tai.getTemplateXPath());
				// Execute the XPath expression and loop through the results.
		        while ((ap.evalXPath()) != -1) {
		        	// Append the attribute.
		        	XMLUtils.appendAttribute(nv, xm, tai.getAttributeName(), tai.getAttributeValue());
		        }
		        
		        // Output and re-parse the document for the next injection.
		        // This is necessary, otherwise exceptions will be thrown when injecting attributes for the same element.
		        nv = xm.outputAndReparse();
		        // Reset and bind the modifier to the new VTDNav object.
		        xm.reset();
		        xm.bind(nv);
			} catch (GeneratorException | XPathParseException | XPathEvalException | NavException | ParseException | TranscodeException | ModifyException | IOException e) {
				throw new TemplatePreprocessorException(String.format("Error while processing template attribute injection for XPath %s: %s", tai.getTemplateXPath(),  e.getMessage()));
			}
		}
		
		// Return the modified XML document.
		try {
			return XMLUtils.getResultingXml(xm);
		} catch (GeneratorException e) {
			throw new TemplatePreprocessorException(e);
		}
	}
	
	/**
	 * Perform placeholder injection for the current node.
	 * @param placeholderName
	 * @param templateNode
	 * @param templatePlaceholderInjections
	 * @throws TemplatePreprocessorException
	 */
	private String performPlaceholderInjections(String template, ArrayList<TemplatePlaceholderInjection> templatePlaceholderInjections) throws TemplatePreprocessorException {
		logger.info("Performing template placeholder injections.");
		
		// Create a VTDNav for navigating the document.
		VTDNav nv;
		try {
			nv = XMLUtils.getVTDNav(template);
		} catch (GeneratorException e) {
			throw new TemplatePreprocessorException(e);
		}
		
		// Create the XMLModifier for modifying the document.
		XMLModifier xm;
		try {
			xm = new XMLModifier(nv);
		} catch (ModifyException e) {
			throw new TemplatePreprocessorException("Error while initializing XMLModifier.", e);
		}
		
		// Loop through the template placeholder injections and apply them.
		for (TemplatePlaceholderInjection tpi : templatePlaceholderInjections) {
			String placeholderToInject = getPlaceholderInjectionValue(NamedTemplateSection.PLACEHOLDER_PLACEHOLDER_NAME, tpi);
			logger.fine(String.format("Performing template placeholder injection (templateXPath='%s' => '%s')", tpi.getTemplateXPath(), placeholderToInject));
			
			// Create an AutoPilot for querying the document.
			AutoPilot ap = new AutoPilot(nv);
			
			// Search for the element to inject an attribute on.
			try {
				// Set the XPath expression from the config.
				ap.selectXPath(tpi.getTemplateXPath());
				// Execute the XPath expression and loop through the results.
				int elementIndex = -1;
		        while ((elementIndex = ap.evalXPath()) != -1) {
		        	// Inject the placeholder based on the token type (element or attribute).
					switch (nv.getTokenType(elementIndex)) {
						// When the token is an attribute we update the value of the attribute.
						case VTDNav.TOKEN_ATTR_NAME:
							// Update the value token (so index of attr name + 1).
							xm.updateToken(elementIndex + 1, placeholderToInject);
							break;
						// When the token is an element, we update the text part of the element.
						case VTDNav.TOKEN_STARTING_TAG:
							int textIndex = nv.getText();
							// Only update the tekst part if it exists.
							if (textIndex != -1) {
								xm.updateToken(textIndex, placeholderToInject);
							}
							// If the text part doesn't exist, throw an exception.
							else {
								throw new GeneratorException("Can't inject placeholder in element, since there is no text part!");
							}
							break;
						default:
							throw new GeneratorException("Unrecognized token type for placeholder injection!");
					}
		        	
		        }
			} catch (GeneratorException | XPathParseException | XPathEvalException | NavException | ModifyException | UnsupportedEncodingException e) {
				throw new TemplatePreprocessorException(String.format("Error while processing template attribute injection for XPath %s: %s", tpi.getTemplateXPath(),  e.getMessage()));
			}
		}
		
		// Return the modified XML document.
		try {
			return XMLUtils.getResultingXml(xm);
		} catch (GeneratorException e) {
			throw new TemplatePreprocessorException(e);
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
		// If the accessor is not set, throw an exception.
		if (accessor == null)
			throw new TemplatePreprocessorException(String.format("%s scope used in TemplatePlaceholderInjection, but not defined in FileFormatConfig.", tpi.getScope().toString()));
		
		return String.format("%s%s%s", placeholderName, accessor, tpi.getModelNode());
	}
}
