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

public class RawTemplateSection extends TemplateSection {
	/**
	 * The content of the section.
	 */
	private String _content;
	
	/**
	 * Constructor.
	 * @param content The content of the section.
	 * @param sectionBeginIndex The section begin index.
	 * @param sectionEndIndex The section end index.
	 */
	public RawTemplateSection(String content, int sectionBeginIndex, int sectionEndIndex) {
		super(sectionBeginIndex, sectionEndIndex);
		this._content = content;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return _content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this._content = content;
	}
}
