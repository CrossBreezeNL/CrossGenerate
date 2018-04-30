package com.xbreeze.xgenerate.config.app;

import javax.xml.bind.annotation.XmlElement;

/**
 * The App part of the CrossGenerate configuration.
 * This part of the configuration contains:
 *  - outputlocations
 *  
 * 
 * @author Harmen
 */
public class AppConfig {

	/**
	 * The folder location for the template files.
	 */
	private String _templateFolder;
	
	/**
	 * The folder location for the model files.
	 */
	private String _modelFolder;
	
	/**
	 * The folder location for the output.
	 */
	private String _outputFolder;
	
	/**
	 * The folder location for the config files.
	 */
	private String _configFolder;

	/**
	 * @return the templateFolder
	 */
	@XmlElement(name="TemplateFolder", required=true)
	public String getTemplateFolder() {
		return _templateFolder;
	}

	/**
	 * @param templateFolder the templateFolder to set
	 */
	public void setTemplateFolder(String templateFolder) {
		this._templateFolder = templateFolder;
	}

	/**
	 * @return the modelFolder
	 */
	@XmlElement(name="ModelFolder", required=true)
	public String getModelFolder() {
		return _modelFolder;
	}

	/**
	 * @param modelFolder the modelFolder to set
	 */
	public void setModelFolder(String modelFolder) {
		this._modelFolder = modelFolder;
	}

	/**
	 * @return the outputFolder
	 */
	@XmlElement(name="OutputFolder", required=true)
	public String getOutputFolder() {
		return _outputFolder;
	}

	/**
	 * @param outputFolder the outputFolder to set
	 */
	public void setOutputFolder(String outputFolder) {
		this._outputFolder = outputFolder;
	}

	/**
	 * @return the configFolder
	 */
	@XmlElement(name="ConfigFolder")
	public String getConfigFolder() {
		return _configFolder;
	}

	/**
	 * @param configFolder the configFolder to set
	 */
	public void setConfigFolder(String configFolder) {
		this._configFolder = configFolder;
	}
}
