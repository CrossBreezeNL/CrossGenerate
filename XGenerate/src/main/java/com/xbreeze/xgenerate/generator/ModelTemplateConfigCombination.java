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
package com.xbreeze.xgenerate.generator;

public class ModelTemplateConfigCombination {
	
	
	private String modelFileLocation;
	private String templateFileLocation;
	private String configFileLocation;
	
	/**
	 * Constructor.
	 * @param modelFileLocation
	 * @param templateFileLocation
	 * @param configFileLocation
	 */
	public ModelTemplateConfigCombination(String modelFileLocation, String templateFileLocation, String configFileLocation) {
		this.setModelFileLocation(modelFileLocation);
		this.setTemplateFileLocation(templateFileLocation);
		this.setConfigFileLocation(configFileLocation);
	}
	
	public static ModelTemplateConfigCombination fromString(String mtcString) throws GeneratorException {
		String[] mtmParams = mtcString.split("::");
		if (mtmParams.length != 3)
			throw new GeneratorException(String.format("The specified Model-Template-Config combination is not of the expected format '%s'", mtcString));
		
		return new ModelTemplateConfigCombination(mtmParams[0], mtmParams[1], mtmParams[2]);
	}

	/**
	 * @return the modelFileLocation
	 */
	public String getModelFileLocation() {
		return modelFileLocation;
	}

	/**
	 * @param modelFileLocation the modelFileLocation to set
	 */
	public void setModelFileLocation(String modelFileLocation) {
		this.modelFileLocation = modelFileLocation;
	}

	/**
	 * @return the templateFileLocation
	 */
	public String getTemplateFileLocation() {
		return templateFileLocation;
	}

	/**
	 * @param templateFileLocation the templateFileLocation to set
	 */
	public void setTemplateFileLocation(String templateFileLocation) {
		this.templateFileLocation = templateFileLocation;
	}

	/**
	 * @return the configFileLocation
	 */
	public String getConfigFileLocation() {
		return configFileLocation;
	}

	/**
	 * @param configFileLocation the configFileLocation to set
	 */
	public void setConfigFileLocation(String configFileLocation) {
		this.configFileLocation = configFileLocation;
	}
}
