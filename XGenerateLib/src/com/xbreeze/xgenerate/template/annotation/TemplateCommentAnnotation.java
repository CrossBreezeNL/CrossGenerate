package com.xbreeze.xgenerate.template.annotation;

/**
 * The XGenComment annotation.
 * It is used to specify a comment in the template which shouldn't be in the actual output (after generating).
 * It will though be in the pre-processed template.
 * 
 * @author Harmen
 */
public class TemplateCommentAnnotation extends TemplateAnnotation {
	
	/**
	 * The comment inside the annotation.
	 */
	private String _comment;

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
