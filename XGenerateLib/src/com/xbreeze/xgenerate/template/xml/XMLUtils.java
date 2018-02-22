package com.xbreeze.xgenerate.template.xml;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;

import com.xbreeze.xgenerate.generator.GeneratorException;

import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.xpath.XPathFactoryImpl;

//TODO move this to a more generic package
public class XMLUtils {

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
}
