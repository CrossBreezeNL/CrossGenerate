package com.xbreeze.xgenerate.config.binding;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The literal configuration object.
 * 
 * @author Harmen
 */
public class LiteralConfig {
	
	/**
	 * The literal to replace.
	 */
	private String literal;
	
	/**
	 * The XPath of the model the literal points to.
	 */
	private String modelXPath;

	/**
	 * @return the literal
	 */
	@XmlAttribute(required=true)
	public String getLiteral() {
		return literal;
	}

	/**
	 * @param literal the literal to set
	 */
	public void setLiteral(String literal) {
		this.literal = literal;
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
