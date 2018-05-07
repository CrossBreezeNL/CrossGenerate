package com.xbreeze.xgenerate.template.annotation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The Section annotation
 * With this annotation one can specify a section in a template.
 * This section is identified by its name (which must be unique within a template).
 * 
 * @author Harmen
 */
@XmlTransient
abstract public class TemplateSectionAnnotation extends TemplateAnnotation {
	/**
	 * @return the name
	 */
	@XmlAttribute(required=true)
	abstract public String getName();

	/**
	 * @param name the name to set
	 */
	abstract public void setName(String name);
}
