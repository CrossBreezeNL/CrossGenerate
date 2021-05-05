package com.xbreeze.xgenerate.config.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ModelAttributeInjectionValueMappings {

	private String inputNode;
	
	private ArrayList<ModelAttributeInjectionValueMapping> modelAttributeInjectionValueMappings;

	/**
	 * @return the input node
	 */
	@XmlAttribute(required=true)
	public String getInputNode() {
		return inputNode;
	}

	public void setInputNode(String inputNode) {
		this.inputNode = inputNode;
	}

	/**
	 * @return The value mapping elements within this value mappings element.
	 */
	@XmlElement(name="ValueMapping")
	public ArrayList<ModelAttributeInjectionValueMapping> getModelAttributeInjectionValueMappings() {
		return modelAttributeInjectionValueMappings;
	}

	public void setModelAttributeInjectionValueMappings(
			ArrayList<ModelAttributeInjectionValueMapping> modelAttributeInjectionValueMappings) {
		this.modelAttributeInjectionValueMappings = modelAttributeInjectionValueMappings;
	}
}
