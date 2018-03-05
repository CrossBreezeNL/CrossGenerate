package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;


public class TemplateAttributeInjection {

	/**
	 * The XPath for parent node where attribute needs to be injected
	 */
	private String _parentNodeXPath;

	/**
	 * The name of the attribute to inject
	 */
	private String _attributeName;
	
	/**
	 * Default value for the newly injected attribute
	 */
	private String _defaultValue;

	@XmlAttribute
	public String get_parentNodeXPath() {
		return _parentNodeXPath;
	}

	public void set_parentNodeXPath(String _parentNodeXPath) {
		this._parentNodeXPath = _parentNodeXPath;
	}

	@XmlAttribute
	public String get_attributeName() {
		return _attributeName;
	}

	public void set_attributeName(String _attributeName) {
		this._attributeName = _attributeName;
	}

	@XmlAttribute
	public String get_defaultValue() {
		return _defaultValue;
	}

	public void set_defaultValue(String _defaultValue) {
		this._defaultValue = _defaultValue;
	}

	
}
