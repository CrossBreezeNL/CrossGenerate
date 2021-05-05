package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;

public class XMLTemplateNodeRemoval {
	/**
	 * The XPath to execute on the template document to find the node to remove.
	 */
	private String templateXPath;

	/**
	 * @return the templateXPath
	 */
	@XmlAttribute(required=true)
	public String getTemplateXPath() {
		return templateXPath;
	}

	/**
	 * @param templateXPath the templateXPath to set
	 */
	public void setTemplateXPath(String templateXPath) {
		this.templateXPath = templateXPath;
	}
}
