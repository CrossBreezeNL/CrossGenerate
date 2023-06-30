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

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;

/**
 * The RootTemplateConfig part of CrossBreeze configuration.
 * It contains configuration for:
 *  - Output
 *  
 * @author Harmen
 */
@XmlTransient
public abstract class RootTemplateConfig extends AbstractTemplateConfig {
	
	/**
	 * The default root section name.
	 */
	private static final String DEFAULT_ROOT_SECTION_NAME = "_template_";
	
	/**
	 * The Output configuration for the Template.
	 * @see OutputConfig
	 */
	private OutputConfig _outputConfig;
	
	/**
	 * Default constructor for root templates.
	 * This will set the root section name to the default value.
	 */
	public RootTemplateConfig() {
		this.setRootSectionName(DEFAULT_ROOT_SECTION_NAME);
	}
	
	/**
	 * @return the output
	 */
	@XmlElement(name="Output", required=true)
	public OutputConfig getOutputConfig() {
		return _outputConfig;
	}

	/**
	 * @param outputConfig the output to set
	 */
	public void setOutputConfig(OutputConfig outputConfig) {
		this._outputConfig = outputConfig;
	}
	
	/**
	 * Function to get the preprocessor for the template type.
	 * @param templateConfig The configuration of the template.
	 * @return The template pre-processor for the TemplateType.
	 */
	public abstract TemplatePreprocessor getTemplatePreprocessor(XGenConfig config);

}
