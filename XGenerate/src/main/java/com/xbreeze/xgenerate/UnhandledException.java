package com.xbreeze.xgenerate;

public class UnhandledException extends CrossGenerateException {
	/**
	 * The serial version uid. 
	 */
	private static final long serialVersionUID = -4048082389494977506L;
	
	/**
	 * Constructor.
	 * @param The unhandled exception.
	 */
	public UnhandledException(Throwable throwable) {
		super(throwable);
	}
}
