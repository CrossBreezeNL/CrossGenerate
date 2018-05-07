package com.xbreeze.xgenerate.template.annotation;

import javax.xml.bind.annotation.XmlAttribute;

public class TemplateXmlSectionAnnotation extends TemplateSectionAnnotation {
	/**
	 * The name of the section.
	 */
	protected String name;
	
	/**
	 * When the template is an XML template, this specifies the element representing the section.
	 */
	private String templateXPath;
	
	/**
	 * Constructor.
	 */
	public TemplateXmlSectionAnnotation() { }
	
	/**
	 * Constructor.
	 * @param name The name of the section.
	 */
	public TemplateXmlSectionAnnotation(String name) {
		this.name = name;
	}
	
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

	@Override
	@XmlAttribute
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
