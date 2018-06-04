package com.xbreeze.xgenerate.config.model;

import javax.xml.bind.annotation.XmlAttribute;

public class ModelNodeRemoval {
	/**
	 * The XPath to execute on the model document to find the node to remove.
	 */
	private String modelXPath;

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
