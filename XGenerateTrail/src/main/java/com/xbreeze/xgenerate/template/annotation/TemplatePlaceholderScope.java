package com.xbreeze.xgenerate.template.annotation;

import javax.xml.bind.annotation.XmlEnum;

/**
 * The scope in the placeholder injection defines at what level placeholder will be resolved.
 * This can be either current or child. 
 * @author Harmen
 */
@XmlEnum
public enum TemplatePlaceholderScope {
	/**
	 * current element attributes
	 */
	current,
	
	/**
	 * current element child elements
	 */
	child
}
