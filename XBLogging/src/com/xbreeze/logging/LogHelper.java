package com.xbreeze.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * Class that provides helper functions for logging
 * @author Willem
 *
 */
public class LogHelper {
	
	/**
	 * Method for obtaining a logger that is used through the entire application.
	 * @return the logger used for CrossGenerate
	 */
	public static Logger getLogger() {		
		return Logger.getLogger("XGenerate.Logger");
	}
	
	/**
	 * Transfers the given LogLevel to a logging Level value
	 * @param level
	 * @return appropriate Level  
	 */
	public static Level translateLogLevel(LogLevel logLevel) {
		if (logLevel == LogLevel.MINIMAL)
			return Level.WARNING;		
		if (logLevel == LogLevel.VERBOSE)
			return Level.FINE;			
		//Default level is informational
		return Level.INFO;
		
	}
}
