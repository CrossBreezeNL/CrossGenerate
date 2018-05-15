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
	 * The name of the section.
	 */
	protected String name;
	
	/**
	 * Indicator whether the section name was set by the user.
	 */
	protected boolean userDefinedSectionName = true;
	
	/**
	 * @return the name
	 */
	@XmlAttribute(required=true)
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 * This must be implemented by the child classes so we can deviate set methods available in annotations from other set methods.
	 */
	abstract public void setName(String name);
	
	/**
	 * @return Return whether the section name is user defined.
	 */
	public boolean isUserDefinedSectionName() {
		return userDefinedSectionName;
	}
}
