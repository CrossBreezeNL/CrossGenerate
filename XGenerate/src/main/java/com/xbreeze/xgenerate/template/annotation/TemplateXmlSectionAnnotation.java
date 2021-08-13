/*******************************************************************************
 *   Copyright (c) 2021 CrossBreeze
 *
 *   This file is part of CrossGenerate.
 *
 *      CrossGenerate is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      CrossGenerate is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with CrossGenerate.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *      Willem Otten - CrossBreeze
 *      Harmen Wessels - CrossBreeze
 *      Jacob Siemaszko - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.template.annotation;

import javax.xml.bind.annotation.XmlAttribute;

public class TemplateXmlSectionAnnotation extends TemplateSectionAnnotation {

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
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setPlaceholderName(String placeholderName) {
		this.placeholderName = placeholderName;
	}
	
	@Override
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	
}
