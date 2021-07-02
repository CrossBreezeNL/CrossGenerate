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
package com.xbreeze.xgenerate.template.section;

import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionAction;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionStyle;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionType;

public class RepetitionTemplateSection extends RawTemplateSection {
	
	private RepetitionType _reptitionType;
	private RepetitionStyle _repetitionStyle;
	private RepetitionAction _repetitionAction;
	
	/**
	 * Constructor.
	 * @param content The content of the repetition section.
	 * @param sectionBeginIndex The section begin index.
	 * @param sectionEndIndex The section end index.
	 */
	public RepetitionTemplateSection(String repetitionContent, int sectionIndex, RepetitionType reptitionType, RepetitionStyle repetitionStyle, RepetitionAction repetitionAction) {
		super(repetitionContent, sectionIndex, sectionIndex);
		this._reptitionType = reptitionType;
		this._repetitionStyle = repetitionStyle;
		this._repetitionAction = repetitionAction;
	}
	
	public RepetitionType getReptitionType() {
		return _reptitionType;
	}

	public RepetitionStyle getRepetitionStyle() {
		return _repetitionStyle;
	}

	public RepetitionAction getRepetitionAction() {
		return _repetitionAction;
	}
}
