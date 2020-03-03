package com.xbreeze.xgenerate.config.template;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateXmlSectionAnnotation;

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
	private String _rootSectionName;
	
	/**
	 * The FileFormat of the template.
	 * This can be one of the predefined file formats.
	 * This will be initialized with an empty file format by default.
	 * @see FileFormatConfig
	 */
	private FileFormatConfig _fileFormatConfig = new FileFormatConfig();
	
	/**
	 * The text section annotations.
	 * @see TemplateTextSectionAnnotation
	 */
	private ArrayList<? extends TemplateSectionAnnotation> _sectionAnnotations;
	
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
	@XmlElements({
		@XmlElement(name="TextSection", type=TemplateTextSectionAnnotation.class),
		@XmlElement(name="XmlSection", type=TemplateXmlSectionAnnotation.class)
	})
	@XmlElementWrapper(name="Sections")
	public ArrayList<? extends TemplateSectionAnnotation> getSectionAnnotations() {
		return _sectionAnnotations;
	}
	
	/**
	 * Set the section annotations. 
	 * @param sectionAnnotations The section annotations.
	 */
	public void setSectionAnnotations(ArrayList<? extends TemplateSectionAnnotation> sectionAnnotations) {
		this._sectionAnnotations = sectionAnnotations;
	}
}
