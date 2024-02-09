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

public class CommentTemplateSection extends TemplateSection {
	/**
	 * The comment in this section.
	 */
	private String _comment;
	
	/**
	 * Constructor.
	 * @param content The content of the section.
	 * @param sectionBeginIndex The section begin index.
	 * @param sectionEndIndex The section end index.
	 */
	public CommentTemplateSection(String comment, int sectionBeginIndex, int sectionEndIndex) {
		super(sectionBeginIndex, sectionEndIndex);
		// Set the comment value.
		this._comment = comment;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return _comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this._comment = comment;
	}
}
