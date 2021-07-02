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

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The placeholder configuration object.
 * 
 * @author Harmen
 */
public class PlaceholderConfig {
	/**
	 * The name of the placeholder.
	 */
	private String name;
	
	/**
	 * The XPath of the model the placeholder points to.
	 */
	private String modelXPath;
	
	/**
	 * The variable name which is bound to the element the modelXPath points to.
	 */
	private String variableName;

	/**
	 * @return the name
	 */
	@XmlAttribute(required=true)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the modelXPath
	 */
	@XmlAttribute(required=true)
	public String getModelXPath() {
		return modelXPath;
	}

	/**
	 * @param modelXPath the modelXPath to set
	 */
	public void setModelXPath(String modelXPath) {
		this.modelXPath = modelXPath;
	}

	/**
	 * @return the variableName
	 */
	@XmlAttribute
	public String getVariableName() {
		return variableName;
	}

	/**
	 * @param variableName the variableName to set
	 */
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	/**
	 * Check whether the variable name is set.
	 * @return Whether the variable name is set.
	 */
	public boolean hasVariableName() {
		return (variableName != null && variableName.length() > 0);
	}
}
