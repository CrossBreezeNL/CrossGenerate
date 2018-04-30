package com.xbreeze.xgenerate.config;

import com.xbreeze.xgenerate.CrossGenerateException;

public class ConfigException extends CrossGenerateException {
	/**
	 * The serial version uid.
	 */
	private static final long serialVersionUID = -4823415587325665341L;

	/**
	 * Constructor.
	 * @param message The message of the exception.
	 */
	public ConfigException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * @param throwable Throwable of the exception.
	 */
	public ConfigException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * Constructor.
	 * @param message The message of the exception.
	 * @param throwable The throwable.
	 */
	public ConfigException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
