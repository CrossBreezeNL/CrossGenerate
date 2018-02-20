package com.xbreeze.xgenerate.config.template;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;

/**
 * The Template part of CrossBreeze configuration.
 * It contains configuration for:
 *  - FileFormat
 *  - Output
 *  - Sections (TemplateSectionAnnotation)
 *  - TemplatePlaceholderInjections (TemplatePlaceholderInjection)
 *  
 * @author Harmen
 */
public class TemplateConfig {
	
	/**
	 * The section name of the root section.
	 * A template implicitly always has a root section containing the whole template. This variable defines to name.
	 */
	private String _rootSectionName;
	
	/**
	 * The FileFormat of the template.
	 * This can be one of the predefined file formats.
	 * @see FileFormatConfig
	 */
	private FileFormatConfig _fileFormatConfig;
	
	/**
	 * The XGenSection annotations specified in the configuration file.
	 * @see TemplateSectionAnnotation
	 */
	private ArrayList<TemplateSectionAnnotation> _sectionAnnotations;
	
	/**
	 * The list of template placeholder injections.
	 * @see TemplatePlaceholderInjection
	 */
	private ArrayList<TemplatePlaceholderInjection> _templatePlaceholderInjections;
	
	/**
	 * The Output configuration for the Template.
	 * @see OutputConfig
	 */
	private OutputConfig _outputConfig;
	
	/**
	 * @return the rootSectionName
	 */
	@XmlAttribute(required=true)
	public String getRootSectionName() {
		return _rootSectionName;
	}

	/**
	 * @param rootSectionName the rootSectionName to set
	 */
	public void setRootSectionName(String rootSectionName) {
		this._rootSectionName = rootSectionName;
	}

	/**
	 * @return the fileFormat
	 */
	@XmlElement(name="FileFormat")
	public FileFormatConfig getFileFormatConfig() {
		return _fileFormatConfig;
	}

	/**
	 * @param fileFormatConfig the fileFormat to set
	 */
	public void setFileFormatConfig(FileFormatConfig fileFormatConfig) {
		this._fileFormatConfig = fileFormatConfig;
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
	 * @return the sections
	 */
	@XmlElement(name="Section")
	@XmlElementWrapper(name="Sections")
	public ArrayList<TemplateSectionAnnotation> getSectionAnnotations() {
		return _sectionAnnotations;
	}

	/**
	 * @param sectionAnnotations the sections to set
	 */
	public void setSectionAnnotations(ArrayList<TemplateSectionAnnotation> sectionAnnotations) {
		this._sectionAnnotations = sectionAnnotations;
	}

	/**
	 * @return the templatePlaceholderInjections
	 */
	@XmlElement(name="TemplatePlaceholderInjection")
	@XmlElementWrapper(name="TemplatePlaceholderInjections")
	public ArrayList<TemplatePlaceholderInjection> getTemplatePlaceholderInjections() {
		return _templatePlaceholderInjections;
	}

	/**
	 * @param templatePlaceholderInjections the templatePlaceholderInjections to set
	 */
	public void setTemplatePlaceholderInjections(ArrayList<TemplatePlaceholderInjection> templatePlaceholderInjections) {
		this._templatePlaceholderInjections = templatePlaceholderInjections;
	}
}
