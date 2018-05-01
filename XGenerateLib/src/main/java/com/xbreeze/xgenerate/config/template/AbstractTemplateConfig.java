package com.xbreeze.xgenerate.config.template;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;

/**
 * The RootTemplateConfig part of CrossBreeze configuration.
 * It contains configuration for:
 *  - FileFormat
 *  - Sections (TemplateSectionAnnotation)
 *  
 * @author Harmen
 */
@XmlTransient
public abstract class AbstractTemplateConfig {
	
	/**
	 * The section name of the root section.
	 * A template implicitly always has a root section containing the whole template. This variable defines to name.
	 */
	private String _rootSectionName = "_template_";
	
	/**
	 * The FileFormat of the template.
	 * This can be one of the predefined file formats.
	 * This will be initialized with an empty file format by default.
	 * @see FileFormatConfig
	 */
	private FileFormatConfig _fileFormatConfig = new FileFormatConfig();
	
	/**
	 * The XGenSection annotations specified in the configuration file.
	 * @see TemplateSectionAnnotation
	 */
	private ArrayList<TemplateSectionAnnotation> _sectionAnnotations;
	
	/**
	 * @return the rootSectionName
	 */
	@XmlAttribute
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

}
