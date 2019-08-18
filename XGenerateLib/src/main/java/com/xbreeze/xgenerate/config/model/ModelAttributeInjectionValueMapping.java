package com.xbreeze.xgenerate.config.model;

import javax.xml.bind.annotation.XmlAttribute;

public class ModelAttributeInjectionValueMapping {
	
	private String inputValue;
	
	private String outputValue;
	
	/**
	 * @return the input value
	 */
	@XmlAttribute(required=true)
	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	/**
	 * @return the output value
	 */
	@XmlAttribute(required=true)
	public String getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(String outputValue) {
		this.outputValue = outputValue;
	}
}
