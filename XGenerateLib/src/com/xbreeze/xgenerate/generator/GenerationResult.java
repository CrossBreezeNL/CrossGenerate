package com.xbreeze.xgenerate.generator;

public class GenerationResult {
	
	private String _templateFileName;
	
	private String _preprocessedTemplate;
	
	private String _outputFileContent;
	
	private String _outputFileLocation;
	
	public GenerationResult(String templateFileName, String preprocessedTemplate, String outputFileContent, String outputFileLocation) {
		this.setTemplateFileName(templateFileName);
		this.setPreprocessedTemplate(preprocessedTemplate);
		this.setOutputFileContent(outputFileContent);
		this.setOutputFileLocation(outputFileLocation);
	}

	/**
	 * @return the result
	 */
	public String getOutputFileContent() {
		return _outputFileContent;
	}

	/**
	 * @param outputFileContent the result to set
	 */
	public void setOutputFileContent(String outputFileContent) {
		this._outputFileContent = outputFileContent;
	}

	/**
	 * @return the outputFileLocation
	 */
	public String getOutputFileLocation() {
		return _outputFileLocation;
	}

	/**
	 * @param outputFileLocation the outputFileLocation to set
	 */
	public void setOutputFileLocation(String outputFileLocation) {
		this._outputFileLocation = outputFileLocation;
	}

	/**
	 * @return the preprocessedTemplate
	 */
	public String getPreprocessedTemplate() {
		return _preprocessedTemplate;
	}

	/**
	 * @param preprocessedTemplate the preprocessedTemplate to set
	 */
	public void setPreprocessedTemplate(String preprocessedTemplate) {
		this._preprocessedTemplate = preprocessedTemplate;
	}

	/**
	 * @return the templateFileName
	 */
	public String getTemplateFileName() {
		return _templateFileName;
	}

	/**
	 * @param templateFileName the templateFileName to set
	 */
	public void setTemplateFileName(String templateFileName) {
		this._templateFileName = templateFileName;
	}
}
