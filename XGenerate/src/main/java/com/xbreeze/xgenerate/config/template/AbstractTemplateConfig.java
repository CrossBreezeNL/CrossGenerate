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
