package com.xbreeze.xgenerate.template;

import com.xbreeze.xgenerate.CrossGenerateException;

public class PreprocessorException extends CrossGenerateException {
	/**
	 * The serial version UID for this class.
	 */
	private static final long serialVersionUID = -3054179347491409324L;
	
	/**
	 * Constructor.
	 * @param message The exception message.
	 */
	public PreprocessorException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * @param throwable The throwable.
	 */
	public PreprocessorException(Throwable throwable) {
		super(throwable);
	}
}
