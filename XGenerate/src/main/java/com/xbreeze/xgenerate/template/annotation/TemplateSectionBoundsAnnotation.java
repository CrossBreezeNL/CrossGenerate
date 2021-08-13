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
package com.xbreeze.xgenerate.template.annotation;

public class TemplateSectionBoundsAnnotation extends TemplateAnnotation {
	private TemplateSectionAnnotation _templateSectionAnnotation;
	
	public TemplateSectionBoundsAnnotation(TemplateSectionAnnotation templateSectionAnnotation, int annotationBeginIndex, int annotationEndIndex) {
		this._templateSectionAnnotation = templateSectionAnnotation;
		setAnnotationBeginIndex(annotationBeginIndex);
		setAnnotationEndIndex(annotationEndIndex);
	}
	
	public String getName() {
		return _templateSectionAnnotation.getName();
	}

	/**
	 * @return the templateSectionAnnotation
	 */
	public TemplateSectionAnnotation getTemplateSectionAnnotation() {
		return _templateSectionAnnotation;
	}

	/**
	 * @param templateSectionAnnotation the templateSectionAnnotation to set
	 */
	public void setTemplateSectionAnnotation(TemplateSectionAnnotation templateSectionAnnotation) {
		this._templateSectionAnnotation = templateSectionAnnotation;
	}
}
