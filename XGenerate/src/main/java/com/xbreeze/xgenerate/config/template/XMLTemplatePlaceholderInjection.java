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
package com.xbreeze.xgenerate.config.template;

import jakarta.xml.bind.annotation.XmlAttribute;

import com.xbreeze.xgenerate.template.annotation.TemplatePlaceholderScope;

public class XMLTemplatePlaceholderInjection {
	
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
	private TemplatePlaceholderScope scope = TemplatePlaceholderScope.current;

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
