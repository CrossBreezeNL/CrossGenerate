package com.xbreeze.xgenerate.template.annotation;

public class UnknownAnnotationParamException extends AnnotationException {
	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = -4850371099181650105L;

	/**
	 * Constructor.
	 * @param annotationName The annotation name.
	 * @param annotationParamName The annotation param name.
	 */
	public UnknownAnnotationParamException(String annotationName, String annotationParamName) {
		super(String.format("Unknown annotation param used (%s -> %s)", annotationName, annotationParamName));
	}
}
