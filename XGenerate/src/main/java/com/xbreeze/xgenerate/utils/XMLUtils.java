/*******************************************************************************
 *   Copyright (c) 2021 CrossBreeze
 *
 *   This file is part of CrossGenerate.
 *
 *      CrossGenerate is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      CrossGenerate is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with CrossGenerate.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *      Willem Otten - CrossBreeze
 *      Harmen Wessels - CrossBreeze
 *      Jacob Siemaszko - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.text.StringEscapeUtils;

import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.ximpleware.AutoPilot;
import com.ximpleware.FastLongBuffer;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

public class XMLUtils {
	// The logger for this class.
	protected static final Logger logger = Logger.getLogger(TemplatePreprocessor.class.getName());
	
	/**
	 * Escape XML characters.
	 * @param input The text to escape.
	 * @return The escaped input.
	 */
	public static String excapeXMLChars(String input) {
		return input.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
	}
	
	/**
	 * Get the VTDNav object for a XML document.
	 * @param xmlDocument The XML document as a String.
	 * @return The VTDNav.
	 * @throws GeneratorException
	 */
	public static VTDNav getVTDNav(String xmlDocument) throws GeneratorException {
		return getVTDNav(xmlDocument, false);
	}
	
	/**
	 * Get the VTDNav object for a XML document.
	 * See: https://vtd-xml.sourceforge.io/javadoc/.
	 * @param xmlDocument The XML document as a String.
	 * @param namespaceAware Whether the parser is namespace aware.
	 * @return The VTDNav.
	 * @throws GeneratorException
	 */
	public static VTDNav getVTDNav(String xmlDocument, boolean namespaceAware) throws GeneratorException {
		// Create a VTGGen object.
		VTDGen vg = new VTDGen();
		
		// Enable collecting all whitespaces.
		vg.enableIgnoredWhiteSpace(true);
		
		// Set the document (in UTF-8 encoding).
		// Currently the encoding is set to US_ASCII, cause this solves the issue for special characters and doesn't seem to break anything.
		// Question is asked to the vtd-gen developer if this is a bug in vtg-gen.
		// https://stackoverflow.com/questions/51507388/vtd-xml-element-fragment-incorrect
		vg.setDoc(xmlDocument.getBytes(StandardCharsets.US_ASCII));
		
		// When enabling namespace awareness, you must map the URLs of all used namespaces here.
		try {
			vg.parse(namespaceAware);
		} catch (ParseException e) {
			throw new GeneratorException(String.format("Error while parsing file as XML document: %s.", e.getMessage()), e);
		}
		
		// Create a VTDNav for navigating the document.
		return vg.getNav();
	}
	
	/**
	 * Append an attribute for the current element.
	 * @param nv The VTDNav where the element is the current index.
	 * @param xm The XMLModifier object to apply the change with.
	 * @param attributeName The new attribute name.
	 * @param attributeValue The new attribute value.
	 * @throws GeneratorException
	 */
	public static void appendAttribute(VTDNav nv, XMLModifier xm, String attributeName, String attributeValue) throws GeneratorException {
    	// Only inject attribute if it does not already exist
    	try {
    		int attributeValueIndex = nv.getAttrVal(attributeName);
    		String encodedAttributeValue = StringEscapeUtils.escapeXml11(attributeValue);
    		// If the attribute doesn't exist, create it.
			if (attributeValueIndex == -1) {
				// Take the element index and count 2 token per attribute (name and value) to get to the last attribute value index.
				int lastAttributeValueIndex = nv.getCurrentIndex() + (nv.getAttrCount() * 2);
				// Take the offset of the last value, add the length of the value + 1 (for the double quote)
				int lastAttributeValueEndIndex = (int)nv.getTokenOffset(lastAttributeValueIndex) + nv.getTokenLength(lastAttributeValueIndex) + 1;
				logger.info(String.format("Appending attribute '%s' at %d", attributeName, lastAttributeValueEndIndex));
				// Insert  the new attribute.
				xm.insertBytesAt(lastAttributeValueEndIndex, String.format(" %s=\"%s\"", attributeName, encodedAttributeValue).getBytes());
			}
			// If the attribute already exists, update it.
			else {
				try {
					xm.updateToken(attributeValueIndex, encodedAttributeValue.getBytes());
				} catch (UnsupportedEncodingException e) {
					throw new GeneratorException(String.format("Error while updating attribute value (%s)", attributeName), e);
				}
			}
		} catch (NavException | ModifyException e) {
			e.printStackTrace();
			throw new GeneratorException("Error while appending attribute into XML element.", e);
		}
	}
	
	/**
	 * Get the value node index based in a given node index.
	 * @param nv The VTDNav object.
	 * @param nodeIndex The node index.
	 * @return The value node index.
	 */
	public static int getNodeValueIndex(VTDNav nv, int nodeIndex) {
    	// The annotation node can be either an attribute or an element.
    	// When it is an attribute, we take the attribute value and if it is a element we take the element text.
    	switch (nv.getTokenType(nodeIndex)) {
	    	case VTDNav.TOKEN_ATTR_NAME:
	    		return nodeIndex + 1;
	    	case VTDNav.TOKEN_STARTING_TAG:
	    		return nv.getText();
	    	case VTDNav.TOKEN_CHARACTER_DATA:
	    		return nodeIndex;
			default:
				return -1;
    	}
	}
	
	/**
	 * Write the new XML structure to a String.
	 * @param xm The XMLModifier
	 * @return The resulting XML document as a String.
	 * @throws GeneratorException 
	 */
	public static String getResultingXml(XMLModifier xm) throws GeneratorException {
		// Write the XML document into a ByteArray.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			xm.output(baos);
		} catch (ModifyException | TranscodeException | IOException e) {
			throw new GeneratorException("Error while getting resulting XML document after modification(s).", e);
		}
        // Store the ByteArray into a String.
        String modifiedTemplate = baos.toString();
        // Close the output stream.
        try {
			baos.close();
		} catch (IOException e) {
			throw new GeneratorException("Error while closing ByteArrayOutputStream after writing XML document.", e);
		}
        // Return the XML document as a String.
        return modifiedTemplate;
	}
	
	/**
	 * Apply a XPath expression on a XML document and return the resulting nodes as a XML fragment in a string
	 * @param xmlDoc a String object containing a XML document
	 * @param xPath the XPath expression to evaluate
	 * @return a String object containing the result of the Xpath evaluated against xmlDoc
	 * @throws ConfigException
	 */
	public static String getXmlFragment(String xmlDoc, String xPath) throws XmlException {
		try {
			logger.fine(String.format("Applying xpointer %s on config file %s", xPath, xmlDoc));
			StringBuilder sb = new StringBuilder();
			VTDNav nav = getVTDNav(xmlDoc);
			AutoPilot ap = new AutoPilot(nav);
			ap.selectXPath(xPath);
			FastLongBuffer flb = new FastLongBuffer();
			//Store the record identifiers from matching elements
			while ((ap.evalXPath()) != -1) {
				flb.append(nav.getElementFragment());				
			}
			logger.fine(String.format("Found %d matches", flb.size()));
			//Process matched elements by extracting them from xml and appending to stringbuilder.
			byte[] xml = nav.getXML().getBytes();
			for (int i = 0; i < flb.size(); i++) {
				sb.append(new String(xml, flb.lower32At(i), flb.upper32At(i)));
			}
			return sb.toString();
		} catch (GeneratorException e) {
			throw new XmlException(String.format("Error parsing %s as XML", xmlDoc), e);
		} catch (XPathParseException | XPathEvalException e) {
			throw new XmlException(getAutopilotExceptionMessage(xPath, e),e);		
		} catch (NavException e) {
			throw new XmlException(String.format("Error navigating %s", xmlDoc), e);
		}		
	}
	
	public static XsltTransformer getXsltTransformer(String xsltTemplateContent, String modelFileContent, URI outputFolderUri) throws GeneratorException {
		// Create a string reader on the pre-processed template.
		StringReader xslStringReader = new StringReader(xsltTemplateContent);
		StreamSource xslSource = new StreamSource(xslStringReader);
		
		// Create an ErrorListener for the TransformerFactory and Transformer, so warnings are logged using the local logger.
		ErrorListener errorListener = new ErrorListener() {
			@Override
			public void warning(TransformerException exception) throws TransformerException {
				// Send warnings to the local logger on the fine log level, so this is only visible when running in debug mode.
				logger.fine(String.format("Warning fired during template transformation: %s", exception.getMessage()));
			}
			
			@Override
			public void fatalError(TransformerException exception) throws TransformerException {
				throw new TransformerException(exception);
			}
			
			@Override
			public void error(TransformerException exception) throws TransformerException {
				throw new TransformerException(exception);
			}
		};
		
		// Create a Saxon processor.
		Processor processor = new Processor(false);
		// Create the Xslt Compiler.
		XsltCompiler xsltCompiler = processor.newXsltCompiler();
		
		// Compile the XSLT stylesheet.
		XsltExecutable xsltExecutable;
		try {
			xsltExecutable = xsltCompiler.compile(xslSource);
		} catch (SaxonApiException e) {
			throw new GeneratorException(String.format("Error while parsing XSLT template: %s", e.getMessage()));
		}
		// Load the Xslt Transformer.
		XsltTransformer xsltTransformer = xsltExecutable.load();
		// Set the error listener on the XSLT transformer.
		xsltTransformer.setErrorListener(errorListener);
		// Create a XdmNode based on the model file content.
		XdmNode modelDocumentNode;
		try {
			StreamSource modelFileStreamSource = new StreamSource(new StringReader(modelFileContent));
			modelDocumentNode = processor.newDocumentBuilder().build(modelFileStreamSource);
		} catch (SaxonApiException e) {
			throw new GeneratorException(String.format("Error while parsing model file content: %s", e.getMessage()));
		}
		// Set the initial context node the the model XdmNode.
		xsltTransformer.setInitialContextNode(modelDocumentNode);
		// Set the serializer on the transformer, this can be an unconfigured serializer since the output uri's are absolute.
		xsltTransformer.setBaseOutputURI(outputFolderUri.toString());
		Serializer outputSerializer = processor.newSerializer();
		xsltTransformer.setDestination(outputSerializer);
		
		// Return the xslt transformer.
		return xsltTransformer;
	}
	
	/**
	 * Function to get a more informative error message when a XPathParseException is fired while using the VTD-Gen AutoPilot.
	 * @param xPath The XPath which was parsed.
	 * @param e The exception which was thrown.
	 * @return The improved exception message.
	 */
	public static String getAutopilotExceptionMessage(String xPath, Exception e) {
		// Init the exception message.
		String exceptionMessage = e.getMessage();
		// If the exception is a XPathParseException, create a exception message using the offset.
		if (e instanceof XPathParseException && ((XPathParseException) e).getOffset() > 0) {
			int substringEnd = ((XPathParseException) e).getOffset();
			int substringStart = (substringEnd <= 10) ? 0 : substringEnd - 10;
			exceptionMessage = String.format("Syntax error after or around the end of ´%s´", xPath.substring(substringStart, substringEnd));
		}
		// Return the exception message.
		return exceptionMessage;
	}
	
	/**
	 * Recursively resolve XIncludes in the XML string. 
	 * @param xmlFileContents The XML file contents that might include XIncludes to resolve
	 * @param xmlFileUri The file URI of the XML file.
	 * @param level The depth of the current inclusion call.
	 * @param resolvedIncludes A collection of previously resolved includes to detect a cycle of inclusions
	 * @return The XML file contents with resolved includes 
	 * @throws ConfigException
	 */
	public static String getXmlWithResolvedIncludes(String xmlFileContents, URI xmlFileUri, int level, HashMap<URI, Integer> resolvedIncludes, boolean namespaceAware) throws XmlException {
		logger.fine(String.format("Scanning file %s for includes", xmlFileUri.toString()));
		// Check for cycle detection, e.g. an include that is already included previously
		if (resolvedIncludes.containsKey(xmlFileUri) && resolvedIncludes.get(xmlFileUri) != level) {
			throw new XmlException(String.format("XML include cycle detected at level %d, file %s is already included previously", level, xmlFileUri.toString()));
		}
		else if (!resolvedIncludes.containsKey(xmlFileUri)) {
			resolvedIncludes.put(xmlFileUri, level);						
		}
		
		// Get basePath of configFile. If the provided URI refers to a file, use its parent path, if it refers to a folder use it as base path
		try {
			URI basePath  = new URI("file:///../");			
			File xmlFile = new File(xmlFileUri.getPath());
			if (xmlFile.isDirectory()) {
				basePath = xmlFileUri;
			}
			else if (xmlFile.isFile()) {
				String parentPath = xmlFile.getParent();
				if (parentPath != null) {			
					basePath = Paths.get(parentPath).toUri();	
				}
			}
			// Resolve basePath to absolute/real path
			try {
				basePath = Paths.get(basePath).toRealPath(LinkOption.NOFOLLOW_LINKS).toUri();
			} catch (IOException e) {
				throw new XmlException(String.format("Error resolving basePath %s to canonical path", basePath.toString()), e);
			} 
			
			// Open the config file and look for includes		
			// Depending on the passed namespaceAware parameter make this XPath namespace aware.
			// This setting influences whether to looks for xi:include or include elements in all namespaces.
			VTDNav nav = XMLUtils.getVTDNav(xmlFileContents, namespaceAware);
			AutoPilot ap = new AutoPilot(nav);
			// Depending on whether we wan't to resolve the includes namespace aware, we setup VTDNav and have an XPath with or without the namespace.
			if (namespaceAware) {
				// Declare the XInclude namespace.
				ap.declareXPathNameSpace("xi", "http://www.w3.org/2001/XInclude");
				// Search for all xi:include elements.
				ap.selectXPath("//xi:include");
			}
			else {
				// Search for all include elements.
				ap.selectXPath("//include");
			}
			
			int includeCount = 0;		
			try {
				XMLModifier vm = new XMLModifier (nav);
				while ((ap.evalXPath()) != -1) {
					// Obtain the filename of include
					AutoPilot ap_href = new AutoPilot(nav);
					ap_href.selectXPath("@href");
					String includeFileLocation = ap_href.evalXPathToString();
					logger.fine(String.format("Found include for %s in file %s", includeFileLocation, xmlFileUri.toString()));
					// Resolve include to a valid path against the basePath
					logger.fine(String.format("base path %s", basePath.toString()));
					Path p = Paths.get(basePath);
					URI includeFileUri = null;
					try {
						includeFileUri = p.resolve(Paths.get(includeFileLocation)).toRealPath(LinkOption.NOFOLLOW_LINKS).toUri();
					} catch (IOException e) {
						throw new XmlException(String.format("Error resolving found include %s for %s to canonical path", includeFileLocation, xmlFileUri.toString()), e);
					} 
					logger.fine(String.format("Resolved include to %s", includeFileUri.toString()));
					
					try {
						// get file contents, recursively processing any includes found
						String includeContents = getXmlWithResolvedIncludes(FileUtils.getFileContent(includeFileUri), includeFileUri, level + 1, resolvedIncludes, namespaceAware);

						// Check for xpointer and apply if found
						AutoPilot ap_xpoint = new AutoPilot(nav);
						ap_xpoint.selectXPath("@xpointer");
						String xPoint = ap_xpoint.evalXPathToString();
						if (xPoint != null && xPoint.length() > 0) {
							logger.fine(String.format("Found xpointer in include: %s", xPoint));
							includeContents = XMLUtils.getXmlFragment(includeContents, xPoint);
						}
						
						// If the file contains an XML declaration, remove it			
						if (includeContents.startsWith("<?xml")) {
							includeContents = includeContents.replaceFirst("^<\\?xml.*\\?>", "");
						}
						
						// Replace the node with the include contents
						vm.insertAfterElement(includeContents);
						// Then remove the include node
						vm.remove();					
					} catch (IOException e) {
						throw new XmlException(String.format("Could not read contents of included file %s", includeFileUri.toString()), e);
					}	
					includeCount++;
				}				
				logger.fine(String.format("Found %d includes in file %s", includeCount, xmlFileUri.toString()));
				//if includes were found, output and parse the modifier and return it, otherwise return the original one
				if (includeCount > 0) {
					String resolvedXGenConfig = XMLUtils.getResultingXml(vm);					
					logger.fine(String.format("File %s with includes resolved:", xmlFileUri.toString()));
					logger.fine("**** Begin of file ****");
					logger.fine(resolvedXGenConfig);
					logger.fine("**** End of file ****");					
					return resolvedXGenConfig;
				} else {
					return xmlFileContents;
				}
			} catch (NavException e) {
				throw new XmlException(String.format("Error scanning %s for includes", xmlFileUri.toString()),e);
			} 	catch (ModifyException e  ) {
				throw new XmlException(String.format("Error modifying file %s", xmlFileUri.toString()), e);
			} 
		} catch (URISyntaxException e) {
			throw new XmlException(String.format("Could not extract base path from file %s", xmlFileUri.toString()), e);
		} catch (GeneratorException e) {
			throw new XmlException(e.getMessage(), e);		
		} catch (XPathParseException | XPathEvalException e) {
			throw new XmlException(String.format("XPath error scanning for includes in %s", xmlFileUri.toString()), e);				
		}
	}
}
