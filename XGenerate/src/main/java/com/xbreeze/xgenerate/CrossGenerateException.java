package com.xbreeze.xgenerate;

public abstract class CrossGenerateException extends Exception {
	/**
	 * The serial version UID.
	 */
	private static final long serialVersionUID = 350438359716246610L;
	
	/**
	 * Constructor.
	 * Use when only throwing a message.
	 * @param message The message of the exception.
	 */
	public CrossGenerateException(String message) {
		super(message);
	}
	
	/**
	 * Constructor.
	 * Used for unhandled exception.
	 * @param throwable The throwable.
	 */
	public CrossGenerateException(Throwable throwable) {
		super(throwable);
	}
	
	/**
	 * Constructor.
	 * @param message The message of the exception.
	 * @param throwable The throwable.
	 */
	public CrossGenerateException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
