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

import jakarta.xml.bind.annotation.XmlAttribute;

public class TemplateTextSectionAnnotation extends TemplateSectionAnnotation {
	/**
	 * The repetition types.
	 */
	public enum RepetitionType {
		prefix,
		suffix
	}

	/**
	 * The repetition style for a prefix or suffix.
	 */
	public enum RepetitionStyle {
		// Only apply the suffix or prefix on the first repetition of the section.
		firstOnly,
		
		// Only apply the suffix or prefix on the last repetition of the section.
		lastOnly,
		
		// Apply the suffix or prefix on all repetition of the section, except the first.
		allButFirst,

		// Apply the suffix or prefix on all repetition of the section, except the last.
		allButLast,

		// Apply the suffix or prefix on all repetition of the section, except the first and last.
		allButFirstAndLast,
		
		// Apply the suffix or prefix on all repetition of the section.
		all
	}
	
	/**
	 * The repetition action for a prefix or suffix.
	 * So whether to add or remove the prefix or suffix. 
	 */
	public enum RepetitionAction {
		add,
		remove
	}
	
	/**
	 * Character sequence which defines the beginning of the section.
	 */
	private String begin;
	
	/**
	 * Whether to include the characters specified in begin in the output
	 */
	private Boolean includeBegin = true;
	
	/**
	 * Character sequence which defines the beginning of the section.
	 */
	private String end;
	
	/**
	 * Whether to include the characters specified in end in the output
	 */
	private Boolean includeEnd = true;
	
	/**
	 * Literal which exists on the first line of the section, the whole line will be taken into the section.
	 */
	private String literalOnFirstLine;
	
	/**
	 * 	Literal which exists on the last line of the section, the whole line will be taken into the section.
	 */
	private String literalOnLastLine;
	
	/**
	 * Defined the number of lines of the section after the @XGenTextSection annotation.
	 */
	private Integer nrOfLines = 1;
	
	/**
	 * The prefix to prepend using the prefixStyle.
	 */
	private String prefix;
	
	/**
	 * The style of the prefix.
	 * @see RepetitionStyle
	 */
	private RepetitionStyle prefixStyle = RepetitionStyle.allButFirst;
	
	/**
	 * The action to be performed with the prefix, either add or remove.
	 * @see RepetitionAction
	 */
	private RepetitionAction prefixAction = RepetitionAction.add;
	
	/**
	 * The suffix to append using the suffxStyle.
	 */
	private String suffix;
	
	/**
	 * The style of the suffix.
	 * @see RepetitionStyle
	 */
	private RepetitionStyle suffixStyle = RepetitionStyle.allButLast;
	
	/**
	 * The action to be performed with the suffix, either add or remove.
	 * @see RepetitionAction
	 */
	private RepetitionAction suffixAction = RepetitionAction.add;
	
	/**
	 * The string literal which represents a line separator within this section.
	 * By default this is the line separator as specified in the FileFormatConfig which is applicable for the template part this annotation is in.
	 * For a text template it will be the main TextTemplate configuration element.
	 * For a XML template, it will be the respective TextTemplate element in the TextTemplates collection.
	 * @see FileFormatConfig
	 */
	private String lineSeparator;
	
	/**
	 * Constructor (primarily here for XML serialization).
	 */
	public TemplateTextSectionAnnotation() { }

	/**
	 * Constructor for user defined sections.
	 * @param name The name of the section.
	 * @param lineSeparator The line separator to use for the section which is annotated.
	 */
	public TemplateTextSectionAnnotation(String name, String lineSeparator) {
		this(name, lineSeparator, true);
	}
	
	/**
	 * Constructor.
	 * @param name The name of the section.
	 * @param lineSeparator The line separator to use for the section which is annotated.
	 * @param userDefinedSectionName Whether this is a user defined section name.
	 */
	public TemplateTextSectionAnnotation(String name, String lineSeparator, boolean userDefinedSectionName) {
		this.name = name;
		this.userDefinedSectionName = userDefinedSectionName;
		this.lineSeparator = lineSeparator;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public void setPlaceholderName(String placeholderName) {
		this.placeholderName = placeholderName;
	}
	
	@Override
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	
	/**
	 * @return the begin
	 */
	@XmlAttribute
	public String getBegin() {
		return begin;
	}

	/**
	 * @param begin the begin to set
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}

	/**
	 * @return the includeBegin
	 */
	@XmlAttribute
	public Boolean isIncludeBegin() {
		return includeBegin;
	}

	/**
	 * @param includeBegin the includeBegin to set
	 */
	public void setIncludeBegin(Boolean includeBegin) {
		this.includeBegin = includeBegin;
	}

	/**
	 * @return the end
	 */
	@XmlAttribute
	public String getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = end;
	}

	/**
	 * @return the includeEnd
	 */
	@XmlAttribute
	public Boolean isIncludeEnd() {
		return includeEnd;
	}

	/**
	 * @param includeEnd the includeEnd to set
	 */
	public void setIncludeEnd(Boolean includeEnd) {
		this.includeEnd = includeEnd;
	}

	/**
	 * @return the literalOnFirstLine
	 */
	@XmlAttribute
	public String getLiteralOnFirstLine() {
		return literalOnFirstLine;
	}

	/**
	 * @param literalOnFirstLine the literalOnFirstLine to set
	 */
	public void setLiteralOnFirstLine(String literalOnFirstLine) {
		this.literalOnFirstLine = literalOnFirstLine;
	}

	/**
	 * @return the literalOnLastLine
	 */
	@XmlAttribute
	public String getLiteralOnLastLine() {
		return literalOnLastLine;
	}

	/**
	 * @param literalOnLastLine the literalOnLastLine to set
	 */
	public void setLiteralOnLastLine(String literalOnLastLine) {
		this.literalOnLastLine = literalOnLastLine;
	}

	/**
	 * @return the nrOfLines
	 */
	@XmlAttribute
	public Integer getNrOfLines() {
		return nrOfLines;
	}

	/**
	 * @param nrOfLines the nrOfLines to set
	 */
	public void setNrOfLines(Integer nrOfLines) {
		this.nrOfLines = nrOfLines;
	}

	/**
	 * @return the prefix
	 */
	@XmlAttribute
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the prefixStyle
	 */
	@XmlAttribute
	public RepetitionStyle getPrefixStyle() {
		return prefixStyle;
	}

	/**
	 * @param prefixStyle the prefixStyle to set
	 */
	public void setPrefixStyle(RepetitionStyle prefixStyle) {
		this.prefixStyle = prefixStyle;
	}

	/**
	 * @return the prefixAction
	 */
	@XmlAttribute
	public RepetitionAction getPrefixAction() {
		return prefixAction;
	}

	/**
	 * @param prefixAction the prefixAction to set
	 */
	public void setPrefixAction(RepetitionAction prefixAction) {
		this.prefixAction = prefixAction;
	}

	/**
	 * @return the suffix
	 */
	@XmlAttribute
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the suffixStyle
	 */
	@XmlAttribute
	public RepetitionStyle getSuffixStyle() {
		return suffixStyle;
	}

	/**
	 * @param suffixStyle the suffixStyle to set
	 */
	public void setSuffixStyle(RepetitionStyle suffixStyle) {
		this.suffixStyle = suffixStyle;
	}

	/**
	 * @return the suffixAction
	 */
	@XmlAttribute
	public RepetitionAction getSuffixAction() {
		return suffixAction;
	}

	/**
	 * @param suffixAction the suffixAction to set
	 */
	public void setSuffixAction(RepetitionAction suffixAction) {
		this.suffixAction = suffixAction;
	}
	
	/**
	 * @return the lineSeparator
	 */
	@XmlAttribute
	public String getLineSeparator() {
		return lineSeparator;
	}

	/**
	 * @param lineSeparator the lineSeparator to set
	 */
	public void setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}
	
	/**
	 * Get the length of the begin of the section.
	 * @return The length of the begin of the section.
	 */
	public int getSectionBeginLength() {
		// If the begin is specified and it is included in the section, the the begin length is returned.
		if (this.begin.length() > 0 && this.includeBegin)
			return this.begin.length();
		else return 0;
	}
}
