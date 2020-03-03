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
	 * The variable name which is bound to the element the modelXPath points to.
	 */
	private String variableName;

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

	/**
	 * @return the variableName
	 */
	@XmlAttribute
	public String getVariableName() {
		return variableName;
	}

	/**
	 * @param variableName the variableName to set
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	/**
	 * Check whether the variable name is set.
	 * @return Whether the variable name is set.
	 */
	public boolean hasVariableName() {
		return (variableName != null && variableName.length() > 0);
	}
}
