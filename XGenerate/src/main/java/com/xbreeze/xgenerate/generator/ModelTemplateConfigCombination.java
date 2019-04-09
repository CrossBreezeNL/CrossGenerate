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
