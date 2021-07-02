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
package com.xbreeze.xgenerate.template;

import java.util.ArrayList;
import java.util.Collections;

import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionBoundsAnnotation;

/**
 * This object represents a pre-processed template
 * where all modifications on the raw content are done so it can be sectionized.
 * @author Harmen
 */
public class PreprocessedTemplate {
	/**
	 * The raw pre-processed template as a String.
	 */
	private String _preprocessedRawTemplate;
	
	/**
	 * The root setion bounds annotation.
	 */
	private TemplateSectionBoundsAnnotation _rootTemplateSectionBoundsAnnotation;
	
	/**
	 * The collection of template annotations for this template.
	 */
	private ArrayList<TemplateAnnotation> _templateAnnotations;
	
	/**
	 * Constructor.
	 * Template annotations are sorted here so its guaranteed they are sorted when consuming.
	 * @param preprocessedRawTemplate The raw pre-processed template.
	 * @param templateAnnotations The template annotations.
	 */
	public PreprocessedTemplate(String preprocessedRawTemplate, TemplateSectionBoundsAnnotation rootTemplateSectionBoundsAnnotation, ArrayList<TemplateAnnotation> templateAnnotations) {
		this._preprocessedRawTemplate = preprocessedRawTemplate;
		this._rootTemplateSectionBoundsAnnotation = rootTemplateSectionBoundsAnnotation;
		this._templateAnnotations = templateAnnotations;
		
		// Now the section begin indexes are found, we sort the collection based on the section begin index for sections and annotation index for other annotations.
		// This makes the order of set according to where section should be created in the preprocessed template.
		Collections.sort(this._templateAnnotations);
	}
	
	/**
	 * Get the raw pre-processed template.
	 * @return
	 */
	public String getPreprocessedRawTemplate() {
		return this._preprocessedRawTemplate;
	}
	
	public TemplateSectionBoundsAnnotation getRootTemplateSectionBoundsAnnotation() {
		return _rootTemplateSectionBoundsAnnotation;
	}
	
	/**
	 * Get the template annotations.
	 * @return The template annotations.
	 */
	public ArrayList<TemplateAnnotation> getTemplateAnnotations() {
		return this._templateAnnotations;
	}
}
