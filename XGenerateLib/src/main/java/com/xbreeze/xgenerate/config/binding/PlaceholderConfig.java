package com.xbreeze.xgenerate.config.binding;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The placeholder configuration object.
 * 
 * @author Harmen
 */
public class PlaceholderConfig {
	/**
	 * The name of the placeholder.
	 */
	private String name;
	
	/**
	 * The XPath of the model the placeholder points to.
	 */
	private String modelXPath;

	/**
	 * @return the name
	 */
	@XmlAttribute(required=true)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the modelXPath
	 */
	@XmlAttribute(required=true)
	public String getModelXPath() {
		return modelXPath;
	}

	/**
	 * @param modelXPath the modelXPath to set
	 */
	public void setModelXPath(String modelXPath) {
		this.modelXPath = modelXPath;
	}
}
