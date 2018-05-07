package com.xbreeze.xgenerate.template.annotation;

public class UnknownAnnotationException extends AnnotationException {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 5064479870544800815L;

	/**
	 * Constructor.
	 * @param annotationName The annotation name used.
	 */
	public UnknownAnnotationException(String annotationName, Throwable throwable) {
		super(String.format("An unknown annotation was used (%s)", annotationName), throwable);
	}
}
