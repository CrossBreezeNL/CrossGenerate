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
 *      Jacob Siemaszko - Crossbreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;


public class XMLTemplateAttributeInjection {

	/**
	 * The XPath for template where attribute needs to be injected
	 */
	private String _templateXPath;

	/**
	 * The name of the attribute to inject
	 */
	private String _attributeName;
	
	/**
	 * Default value for the newly injected attribute
	 */
	private String _attributeValue;

	@XmlAttribute
	public String getTemplateXPath() {
		return _templateXPath;
	}

	public void setTemplateXPath(String templateXPath) {
		this._templateXPath = templateXPath;
	}

	@XmlAttribute
	public String getAttributeName() {
		return _attributeName;
	}

	public void setAttributeName(String attributeName) {
		this._attributeName = attributeName;
	}

	@XmlAttribute
	public String getAttributeValue() {
		return _attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this._attributeValue = attributeValue;
	}
}
