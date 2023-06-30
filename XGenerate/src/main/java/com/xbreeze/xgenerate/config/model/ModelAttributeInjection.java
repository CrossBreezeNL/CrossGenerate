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
package com.xbreeze.xgenerate.config.model;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

public class ModelAttributeInjection {

	private String modelXPath;
	
	private String targetAttribute;
	
	private String targetValue;
	
	private String targetXPath;
	
	private ModelAttributeInjectionValueMappings valueMappings;

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
	 * @return the targetAttribute
	 */
	@XmlAttribute(required=true)
	public String getTargetAttribute() {
		return targetAttribute;
	}

	/**
	 * @param targetAttribute the targetAttribute to set
	 */
	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
	}

	/**
	 * @return the targetValue
	 */
	@XmlAttribute(required=false)
	public String getTargetValue() {
		return targetValue;
	}

	/**
	 * @param targetValue the targetValue to set
	 */
	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	@XmlAttribute(required=false)
	public String getTargetXPath() {
		return targetXPath;
	}

	/**
	 * @param targetXPath the targetXPath to set
	 */
	public void setTargetXPath(String targetXPath) {
		this.targetXPath = targetXPath;
	}

	/**
	 * @return The value mappings for the model attribute injection.
	 */
	@XmlElement(name="ValueMappings", required=false)
	public ModelAttributeInjectionValueMappings getValueMappings() {
		return valueMappings;
	}

	/**
	 * @param valueMappings the valueMappings to set
	 */
	public void setValueMappings(ModelAttributeInjectionValueMappings valueMappings) {
		this.valueMappings = valueMappings;
	}
	
	
}
