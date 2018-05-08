package com.xbreeze.xgenerate.template.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;

import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;

import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.xpath.XPathFactoryImpl;

//TODO move this to a more generic package
public class XMLUtils {
	// The logger for this class.
	protected static final Logger logger = Logger.getLogger(TemplatePreprocessor.class.getName());

	public static DocumentBuilder getDocumentBuilder() throws GeneratorException {
		// Create a DocumentBuilderFactory.
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// Setting features on the DocumentBuilderFactory.
		// See: https://stackoverflow.com/questions/155101/make-documentbuilder-parse-ignore-dtd-references
		dbf.setValidating(false);
		dbf.setNamespaceAware(true);
		try {
			dbf.setFeature("http://xml.org/sax/features/namespaces", true);
			dbf.setFeature("http://xml.org/sax/features/validation", false);
			// Disable loading of dtd's.
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			// Return the document builder.
			return dbf.newDocumentBuilder();
		}
		catch (ParserConfigurationException e) {
			throw new GeneratorException(e);
		}
	}
	
	public static TransformerFactory getXmlTransformer() {
		return new TransformerFactoryImpl();
	}
	
	/**
	 * Get a XPath evaluator.
	 * @return
	 */
	public static XPath getXPath() {
		// Create a XPath evaluator (use new XPathFactoryImpl() to make sure the Saxon XPath is used).
		return new XPathFactoryImpl().newXPath();
	}
	
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
		
		// Set the document.
		vg.setDoc(xmlDocument.getBytes());
		
		// Parse without namespace.
		try {
			vg.parse(false);
		} catch (ParseException e) {
			throw new GeneratorException("Error while reading file as XML document.", e);
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
			if (nv.getAttrVal(attributeName) == -1) {
				// Take the element index and count 2 token per attribute (name and value) to get to the last attribute value index.
				int lastAttributeValueIndex = nv.getCurrentIndex() + (nv.getAttrCount() * 2);
				// Take the offset of the last value, add the length of the value + 1 (for the double quote)
				int lastAttributeValueEndIndex = (int)nv.getTokenOffset(lastAttributeValueIndex) + nv.getTokenLength(lastAttributeValueIndex) + 1;
				logger.info(String.format("Appending attribute '%s' into template at %d", attributeName, lastAttributeValueEndIndex));
				// Insert  the new attribute.
				xm.insertBytesAt(lastAttributeValueEndIndex, String.format(" %s=\"%s\"", attributeName, attributeValue).getBytes());
			} else {
				throw new GeneratorException(String.format("Trying to inject an attribute on an existing attribute, this is not allowed ('%s' -> '%s').", xm.toString(), attributeName));
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
}
