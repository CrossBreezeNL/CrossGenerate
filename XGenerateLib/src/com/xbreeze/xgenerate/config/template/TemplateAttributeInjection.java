package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;


public class TemplateAttributeInjection {

	/**
	 * The XPath for template where attribute needs to be injected
	 */
	private String _templateXPath;

	/**
	 * The name of the attribute to inject
	 */
	private String _attributeName;
	
	/**
	 * Default value for the newly injected attribute
	 */
	private String _attributeValue;

	@XmlAttribute
	public String getTemplateXPath() {
		return _templateXPath;
	}

	public void setTemplateXPath(String templateXPath) {
		this._templateXPath = templateXPath;
	}

	@XmlAttribute
	public String getAttributeName() {
		return _attributeName;
	}

	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}

	@XmlAttribute
	public String getAttributeValue() {
		return _attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this._attributeValue = attributeValue;
	}
}
