package com.xbreeze.xgenerate.template;

import com.xbreeze.xgenerate.CrossGenerateException;

public class TemplatePreprocessorException extends CrossGenerateException {
	/**
	 * The serial version UID for this class.
	 */
	private static final long serialVersionUID = -3054179347491409324L;
	
	/**
	 * Constructor.
	 * @param message The exception message.
	 */
	public TemplatePreprocessorException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * @param throwable The throwable.
	 */
	public TemplatePreprocessorException(Throwable throwable) {
		super(throwable);
	}
}
