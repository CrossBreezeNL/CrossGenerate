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
