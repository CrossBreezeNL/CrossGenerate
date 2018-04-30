package com.xbreeze.xgenerate.model;

import com.xbreeze.xgenerate.CrossGenerateException;

public class ModelPreprocessorException extends CrossGenerateException {
	/**
	 * The serial version UID for this class.
	 */
	private static final long serialVersionUID = -3054179347491409324L;
	
	/**
	 * Constructor.
	 * @param message The exception message.
	 */
	public ModelPreprocessorException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * @param throwable The throwable.
	 */
	public ModelPreprocessorException(Throwable throwable) {
		super(throwable);
	}
}
