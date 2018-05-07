package com.xbreeze.xgenerate.template.annotation;

import com.xbreeze.xgenerate.CrossGenerateException;

public class AnnotationException extends CrossGenerateException {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -9051188818364984584L;
	
	/**
	 * Constructor.
	 * @param message The message of the exception.
	 */
	public AnnotationException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * @param throwable The throwable.
	 */
	public AnnotationException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * Constructor.
	 * @param message The message of the exception.
	 * @param throwable The throwable.
	 */
	public AnnotationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
