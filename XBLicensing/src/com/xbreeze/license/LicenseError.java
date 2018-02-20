package com.xbreeze.license;
/**
 * LicenseError is thrown in case of errors during license validation  
 * @author Willem
 *
 */
public class LicenseError extends Exception{
	
	 /**
	 * serial version id
	 */
	private static final long serialVersionUID = -9077208306531770723L;

	/**
	 * Constructor
	 * @param message the message of the error
	 */
	public LicenseError(String message) {
         super();
         System.out.println(message);            
	 } 
	
}
