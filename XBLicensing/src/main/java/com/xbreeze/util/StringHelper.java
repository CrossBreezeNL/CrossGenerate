package com.xbreeze.util;

/***
 * Helper class for string functions
 * @author Willem
 *
 */
public class StringHelper {
	
	/***
	 * Checks if a string is null, empty or only consists of whitespace
	 * @param s the string to validate
	 * @return true if string is null, empty or whitespace only, false otherwise
	 */
	public static Boolean isEmptyOrWhitespace(String s) {
		return s == null || s.trim().isEmpty();
	}

}
