package com.xbreeze.xgenerate.config.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ModelAttributeInjection {

	private String modelXPath;
	
	private String targetAttribute;
	
	private String targetValue;
	
	private String targetXPath;
	
	private ModelAttributeInjectionValueMappings valueMappings;

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
	@XmlAttribute(required=false)
	public String getTargetValue() {
		return targetValue;
	}

	/**
	 * @param targetValue the targetValue to set
	 */
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	@XmlAttribute(required=false)
	public String getTargetXPath() {
		return targetXPath;
	}

	/**
	 * @param targetXPath the targetXPath to set
	 */
	public void setTargetXPath(String targetXPath) {
		this.targetXPath = targetXPath;
	}

	/**
	 * @return The value mappings for the model attribute injection.
	 */
	@XmlElement(name="ValueMappings", required=false)
	public ModelAttributeInjectionValueMappings getValueMappings() {
		return valueMappings;
	}

	/**
	 * @param valueMappings the valueMappings to set
	 */
	public void setValueMappings(ModelAttributeInjectionValueMappings valueMappings) {
		this.valueMappings = valueMappings;
	}
	
	
}
