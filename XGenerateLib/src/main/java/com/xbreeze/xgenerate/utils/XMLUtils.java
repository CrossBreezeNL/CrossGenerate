package com.xbreeze.xgenerate.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

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
	 * See: https://vtd-xml.sourceforge.io/javadoc/.
	 * @param xmlDocument The XML document as a String.
	 * @return The VTDNav.
	 * @throws GeneratorException
	 */
	public static VTDNav getVTDNav(String xmlDocument) throws GeneratorException {
		// Create a VTGGen object.
		VTDGen vg = new VTDGen();
		
		// Enable collecting all whitespaces.
		vg.enableIgnoredWhiteSpace(true);
		
		// Set the document (in UTF-8 encoding).
		vg.setDoc(xmlDocument.getBytes(StandardCharsets.UTF_8));
		
		// Parse without namespace.
		try {
			vg.parse(false);
		} catch (ParseException e) {
			throw new GeneratorException(String.format("Error while reading file as XML document: %s.", e.getMessage()), e);
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
    		// If the attribute doesn't exist, create it.
			if (attributeValueIndex == -1) {
				// Take the element index and count 2 token per attribute (name and value) to get to the last attribute value index.
				int lastAttributeValueIndex = nv.getCurrentIndex() + (nv.getAttrCount() * 2);
				// Take the offset of the last value, add the length of the value + 1 (for the double quote)
				int lastAttributeValueEndIndex = (int)nv.getTokenOffset(lastAttributeValueIndex) + nv.getTokenLength(lastAttributeValueIndex) + 1;
				logger.info(String.format("Appending attribute '%s' at %d", attributeName, lastAttributeValueEndIndex));
				// Insert  the new attribute.
				xm.insertBytesAt(lastAttributeValueEndIndex, String.format(" %s=\"%s\"", attributeName, attributeValue).getBytes());
			}
			// If the attribute already exists, update it.
			else {
				try {
					xm.updateToken(attributeValueIndex, attributeValue.getBytes());
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
}
