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
