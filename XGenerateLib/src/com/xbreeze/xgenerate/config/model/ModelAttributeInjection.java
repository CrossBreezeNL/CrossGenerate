package com.xbreeze.xgenerate.config.model;

import javax.xml.bind.annotation.XmlAttribute;

public class ModelAttributeInjection {

	private String modelXPath;
	
	private String targetAttribute;
	
	private String targetValue;

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
	 * @return the targetAttribute
	 */
	@XmlAttribute(required=true)
	public String getTargetAttribute() {
		return targetAttribute;
	}

	/**
	 * @param targetAttribute the targetAttribute to set
	 */
	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
	}

	/**
	 * @return the targetValue
	 */
	@XmlAttribute(required=true)
	public String getTargetValue() {
		return targetValue;
	}

	/**
	 * @param targetValue the targetValue to set
	 */
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}
}
