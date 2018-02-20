package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;

import com.xbreeze.xgenerate.template.annotation.TemplatePlaceholderScope;

public class TemplatePlaceholderInjection {
	
	/**
	 * The XPath to evaluate on the template document to find the node on which to inject the placeholder.
	 */
	private String templateXPath;
	
	/**
	 * What node in the model needs to be selected in the placeholder (it will be the right side of the placeholder).
	 */
	private String modelNode;
	
	/**
	 * The template placeholder scope.
	 * @see TemplatePlaceholderScope
	 */
	private TemplatePlaceholderScope scope;

	/**
	 * @return the templateXPath
	 */
	@XmlAttribute
	public String getTemplateXPath() {
		return templateXPath;
	}

	/**
	 * @param templateXPath the templateXPath to set
	 */
	public void setTemplateXPath(String templateXPath) {
		this.templateXPath = templateXPath;
	}

	/**
	 * @return the modelNode
	 */
	@XmlAttribute
	public String getModelNode() {
		return modelNode;
	}

	/**
	 * @param modelNode the modelNode to set
	 */
	public void setModelNode(String modelNode) {
		this.modelNode = modelNode;
	}

	/**
	 * @return the scope
	 */
	@XmlAttribute
	public TemplatePlaceholderScope getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(TemplatePlaceholderScope scope) {
		this.scope = scope;
	}
}
