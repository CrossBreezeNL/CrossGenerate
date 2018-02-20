package com.xbreeze.xgenerate.template;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PreprocessedTemplate {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(PreprocessedTemplate.class.getName());
	
	/**
	 * The template buffer.
	 */
	private StringBuffer _templateBuffer;
	
	/**
	 * Track whether the template is finalized.
	 * This is used to give a warning when toString is used on an unfinalized template.
	 */
	private boolean _isFinalized = false;
	
	/**
	 * Constructor.
	 * @param templateType The template type.
	 * @param rootModelXPath The XPath of the element to match on the root of the template.
	 */
	public PreprocessedTemplate(String templateId, TemplateType templateType, String rootModelXPath) {
		this._templateBuffer = new StringBuffer();
		
		// Initialize the template.
		initTemplate(templateId, templateType, rootModelXPath);
	}
	
	/**
	 * Initialize the template, by creating the starting elements for the XSLT.
	 */
	private void initTemplate(String templateId, TemplateType templateType, String rootModelXPath) {
		appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		// https://www.w3schools.com/xml/ref_xsl_el_stylesheet.asp
		appendLine("<xsl:stylesheet id=\"%s\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"3.0\">", templateId);
		
		// Set the output method based on the template type.
		// https://www.w3schools.com/xml/ref_xsl_el_output.asp
		// TODO: Maybe encoding should also be set in the FileFormatConfig.
		if (templateType.equals(TemplateType.xml)) {
			//appendLine("<xsl:output method=\"xml\" version=\"1.0\" encoding=\"UTF-8\" indent=\"yes\" />");
			//appendLine("<xsl:output method=\"xml\" version=\"1.0\" encoding=\"UTF-8\" indent=\"yes\" omit-xml-declaration=\"no\" />");
			appendLine("<xsl:output method=\"text\" encoding=\"UTF-8\" indent=\"no\" />");
		} else {
			appendLine("<xsl:output method=\"text\" encoding=\"UTF-8\" indent=\"no\" />");
		}
		
		// Strip white space from all elements (from the model).
		appendLine("<xsl:strip-space elements=\"*\"/>");
		
		// Add the template match part on the model node to match the template on.
		append(String.format("<xsl:template match=\"%s\">", rootModelXPath));
	}
	
	/**
	 * Finalize the template by closing the xsl element.
	 */
	public void finalizeTemplate() {
		// Add an newline at the end of the template.
		appendLine("");
		appendLine("</xsl:template>");
		append("</xsl:stylesheet>");
		
		// Set the status of isFinalized to true.
		_isFinalized = true;
	}
	
	/**
	 * Append the content of str to the template buffer.
	 * @param str The text to append.
	 */
	public void append(String str) {
		_templateBuffer.append(str);
	}
	
	/**
	 * Append a string using the template and the args.
	 * @param template The string template.
	 * @param args The args as an input for the String.format.
	 */
	public void append(String template, Object... args) {
		append(String.format(template, args));
	}
	
	/**
	 * Append the content of str with a line separator to the template buffer.
	 * @param str The content of the line to append.
	 */
	public void appendLine(String str) {
		_templateBuffer.append(str).append(System.lineSeparator());
	}
	
	/**
	 * Append a string with a newline separator using the template and the args.
	 * @param template The string template.
	 * @param args The args as an input for the String.format.
	 */
	public void appendLine(String template, Object... args) {
		appendLine(String.format(template, args));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// If the template is not finalized yet, give a warning.
		if (!_isFinalized)
			logger.log(Level.WARNING, "The toString() method is called on a PreprocessedTemplate, but it's not finalized yet!");
		
		return _templateBuffer.toString();
	}
}
