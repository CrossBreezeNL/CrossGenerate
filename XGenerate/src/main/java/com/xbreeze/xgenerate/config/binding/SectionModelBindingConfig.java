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
package com.xbreeze.xgenerate.config.binding;

import jakarta.xml.bind.annotation.XmlAttribute;

/**
 * The Section Model Binding configuration which can be specified in the Binding part of the CrossGenerate configuration.
 * The binding specifies how a section in the template is bound to an element in the model.
 * 
 * @author Harmen
 */
public class SectionModelBindingConfig extends BindingContainer {
	/**
	 * The name of the section in the template.
	 */
	private String _sectionName;
	
	/**
	 * The XPath which can be executed on the model to get to the element to bind on to the section.
	 */
	private String _modelXPath;
	
	/**
	 * The placeholder which is bound to the element returned by modelXPath.
	 */
	private String _placeholderName;
	
	/**
	 * The variable name which is bound to the current element within a for-each for the section model binding.
	 */
	private String _variableName;
	
	/**
	 * @return the _section
	 */
	@XmlAttribute(name="section", required=true)
	public String getSectionName() {
		return _sectionName;
	}

	/**
	 * @param sectionName the section name to set
	 */
	public void setSectionName(String sectionName) {
		this._sectionName = sectionName;
	}
	
	/**
	 * @return the modelXPath
	 */
	@XmlAttribute(required=true)
	public String getModelXPath() {
		return _modelXPath;
	}

	/**
	 * @param modelXPath the modelXPath to set
	 */
	public void setModelXPath(String modelXPath) {
		this._modelXPath = modelXPath;
	}

	/**
	 * @return the placeholderName
	 * If the placeholder name was not specified, it will return the section name.
	 * So implicitly the placeholder name is the section name by default.
	 */
	@XmlAttribute
	public String getPlaceholderName() {
		return (this._placeholderName != null) ? this._placeholderName : this._sectionName;
	}

	/**
	 * Set the placeholder name of the binding model element.
	 * @param placeholderName the placeholderName to set
	 */
	public void setPlaceholderName(String placeholderName) {
		this._placeholderName = placeholderName;
	}

	/**
	 * @return the variableName
	 */
	@XmlAttribute
	public String getVariableName() {
		return _variableName;
	}

	/**
	 * @param variableName the variableName to set
	 */
	public void setVariableName(String variableName) {
		this._variableName = variableName;
	}
	
	/**
	 * Check whether the variable name is set.
	 * @return Whether the variable name is set.
	 */
	public boolean hasVariableName() {
		return (_variableName != null && _variableName.length() > 0);
	}
}
