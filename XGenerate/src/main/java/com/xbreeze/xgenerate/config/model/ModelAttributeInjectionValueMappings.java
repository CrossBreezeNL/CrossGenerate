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
package com.xbreeze.xgenerate.config.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ModelAttributeInjectionValueMappings {

	private String inputNode;
	
	private ArrayList<ModelAttributeInjectionValueMapping> modelAttributeInjectionValueMappings;

	/**
	 * @return the input node
	 */
	@XmlAttribute(required=true)
	public String getInputNode() {
		return inputNode;
	}

	public void setInputNode(String inputNode) {
		this.inputNode = inputNode;
	}

	/**
	 * @return The value mapping elements within this value mappings element.
	 */
	@XmlElement(name="ValueMapping")
	public ArrayList<ModelAttributeInjectionValueMapping> getModelAttributeInjectionValueMappings() {
		return modelAttributeInjectionValueMappings;
	}

	public void setModelAttributeInjectionValueMappings(
			ArrayList<ModelAttributeInjectionValueMapping> modelAttributeInjectionValueMappings) {
		this.modelAttributeInjectionValueMappings = modelAttributeInjectionValueMappings;
	}
}
