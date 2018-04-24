package com.xbreeze.xgenerate.generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Log formatter class used to format logrecords
 * @author Willem
 *
 */

public class GeneratorLogFormatter extends Formatter {

	@Override
	/**
	 * Formats the logrecord
	 */
	public String format(LogRecord logRecord) {
		StringBuffer sb = new StringBuffer(1024);
		//Add date/time of logrecord
		sb.append("[").append(calcDate(logRecord.getMillis())).append("]");
		//Add log level
		sb.append(" [" + logRecord.getLevel().getName().toLowerCase()).append("]");
		//Add the log message
		sb.append("  ").append(logRecord.getMessage()).append(System.lineSeparator());
		return sb.toString();		
	}
	
	/**
	 * Method for displaying the logrecord's timestamp in a human friendly form
	 * @param millisecs
	 * @return the timestamp in readable form
	 */
	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss:ms");
	    Date resultdate = new Date(millisecs);
	    return date_format.format(resultdate);
	}
}
