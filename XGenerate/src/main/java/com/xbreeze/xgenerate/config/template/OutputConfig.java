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

/**
 * The Output part of the Template configuration.
 * One can specify the output type here.
 * 
 * @author Harmen
 */
public class OutputConfig {
	/**
	 * Configuration enum which specifies the output type.
	 *  - output_per_element - will results in 1 output for each element mapped to the root section of the template.
	 *  - single_output - will results in 1 output in total, combining all translations from elements into the template into 1 output.
	 */
	public enum OutputType {
		output_per_element,
		single_output
	}
	
	/**
	 * The OutputType configuration.
	 * @see OutputType
	 */
	private OutputType _type;

	/**
	 * @return the type
	 */
	@XmlAttribute(required=true)
	public OutputType getType() {
		return _type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(OutputType type) {
		this._type = type;
	}
}
