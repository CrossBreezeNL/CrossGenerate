package com.xbreeze.xgenerate.template.annotation;

public class IncorrectParamValueException extends AnnotationException {
	/**
	 * The serial version uid. 
	 */
	private static final long serialVersionUID = -4727973350784732043L;

	/**
	 * Constructor.
	 * @param annotationName The annotation name.
	 * @param paramName The param name.
	 * @param paramValue The param value.
	 */
	public IncorrectParamValueException(String annotationName, String paramName, Object paramValue, String expectedType) {
		super(String.format("Incorrect param value specified, expected an %s (%s -> %s -> '%s')", expectedType, annotationName, paramName, paramValue));
	}

}
