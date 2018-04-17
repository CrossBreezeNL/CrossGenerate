package com.xbreeze.xgenerate.generator;

import com.xbreeze.xgenerate.CrossGenerateException;

public class GeneratorException extends CrossGenerateException {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = 2494604516939669046L;
	
	public GeneratorException(String message) {
		super(message);
	}

	public GeneratorException(Throwable throwable) {
		super(throwable);
	}
	
	public GeneratorException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
