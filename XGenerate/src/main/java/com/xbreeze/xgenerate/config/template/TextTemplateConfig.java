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

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation;
import com.xbreeze.xgenerate.template.text.TextTemplatePreprocessor;

/**
 * A text template
 * This is plain text and doesn't contain an explicit hierarchy.
 * Examples: SQL
 * 
 * @author Harmen
 */
public class TextTemplateConfig extends RootTemplateConfig {
	
	/**
	 * Default constructor.
	 */
	public TextTemplateConfig() {
		super();
	}
	
	/**
	 * Return the TextTemplatePreprocessor.
	 */
	@Override
	public TemplatePreprocessor getTemplatePreprocessor(XGenConfig config) {
		return new TextTemplatePreprocessor(config);
	}

	@Override
	@XmlElementWrapper(name="TextSections")
	@XmlElement(name="TextSection", type=TemplateTextSectionAnnotation.class)
	public ArrayList<? extends TemplateSectionAnnotation> getSectionAnnotations() {
		return super.getSectionAnnotations();
	}
}
