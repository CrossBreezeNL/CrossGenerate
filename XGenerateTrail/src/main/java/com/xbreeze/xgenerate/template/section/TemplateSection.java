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
