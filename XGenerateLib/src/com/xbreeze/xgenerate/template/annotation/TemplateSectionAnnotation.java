package com.xbreeze.xgenerate.template.annotation;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The Section annotation
 * With this annotation one can specify a section in a template.
 * This section is identified by its name (which must be unique within a template).
 * 
 * @author Harmen
 */
public class TemplateSectionAnnotation extends TemplateAnnotation {

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
		allButFirstAndLast
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
	 * The name of the section.
	 */
	private String name;
	
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
	 * Defined the number of lines of the section after the @XGenSection annotation.
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
	 * When the template is an XML template, this specifies the element representing the section.
	 */
	private String templateXPath;
	
	/**
	 * Constructor.
	 */
	public TemplateSectionAnnotation() { }
	
	/**
	 * Constructor.
	 * @param name The name of the section.
	 */
	public TemplateSectionAnnotation(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * @return the name
	 */
	@XmlAttribute(required=true)
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the templateXPath
	 */
	@XmlAttribute
	public String getTemplateXPath() {
		return templateXPath;
	}

	/**
	 * @param templateXPath the templateXPath to set
	 */
	public void setTemplateXPath(String templateXPath) {
		this.templateXPath = templateXPath;
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
