package com.xbreeze.xgenerate.generator;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
		StringBuffer sb = initLogRecordStringBuffer(logRecord);
				
		//Add the log message
		sb.append(logRecord.getMessage()).append(System.lineSeparator());
		return sb.toString();		
	}
	
	/**
	 * Initialize the stringbuffer for a logrecord
	 * @param logRecord
	 * @return a stringbuffer with timestamp and log level
	 */
	protected StringBuffer initLogRecordStringBuffer(LogRecord logRecord) {
		StringBuffer sb = new StringBuffer(1024);
		//Add date/time of logrecord
		sb.append("[").append(calcDate(logRecord.getMillis())).append("]");
		//Add log level
		sb.append(" [" + logRecord.getLevel().getName().toLowerCase()).append("]");
		//Add padding to loglevel and return stringbuffer		
		return appendPadding(sb, logRecord.getLevel().getName(), 8);
	}
	
	/**
	 * Add padding to the stringbuffer 
	 * @param sb StringBuffer to add padding to
	 * @param value Value that needs to be padded
	 * @param length Length that value needs to be padded
	 * @return the stringbuffer with padding appended (if needed)
	 */
	protected StringBuffer appendPadding(StringBuffer sb, String value, int length) {
		if (length - value.length() > 0) {
			char[] padding = new char[length - value.length()];
			Arrays.fill(padding, ' ');
			return sb.append(padding);
		}
		return sb;
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
