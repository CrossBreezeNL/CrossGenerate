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
package com.xbreeze.xgenerate.template.section;

/**
 * A the template section.
 * This can be either a NamedTemplateSection, RawTemplateSection or CommentTemplateSection.
 * @see NamedTemplateSection
 * @see RawTemplateSection
 * @see CommentTemplateSection
 * 
 * @author Harmen
 */
public abstract class TemplateSection {
	/**
	 * The starting character index of the section.
	 */
	private int _sectionBeginIndex = -1;
	
	/**
	 * The ending character index of the section.
	 */
	private int _sectionEndIndex = -1;

	/**
	 * Constructor.
	 * @param sectionBeginIndex The section begin index.
	 * @param sectionEndIndex The section end index.
	 */
	public TemplateSection(int sectionBeginIndex, int sectionEndIndex) {
		this._sectionBeginIndex = sectionBeginIndex;
		this._sectionEndIndex = sectionEndIndex;
	}

	/**
	 * @return the sectionBeginIndex
	 */
	public int getSectionBeginIndex() {
		return _sectionBeginIndex;
	}
	
	/**
	 * @param sectionBeginIndex the sectionBeginIndex to set
	 */
	public void setSectionBeginIndex(int sectionBeginIndex) {
		this._sectionBeginIndex = sectionBeginIndex;
	}

	/**
	 * @return the sectionEndIndex
	 */
	public int getSectionEndIndex() {
		return _sectionEndIndex;
	}

	/**
	 * @param sectionEndIndex the sectionEndIndex to set
	 */
	public void setSectionEndIndex(int sectionEndIndex) {
		this._sectionEndIndex = sectionEndIndex;
	}
}
