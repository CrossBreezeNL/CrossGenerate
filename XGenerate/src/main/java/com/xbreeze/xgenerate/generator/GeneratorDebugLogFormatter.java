package com.xbreeze.xgenerate.generator;

import java.util.logging.LogRecord;

public class GeneratorDebugLogFormatter extends GeneratorLogFormatter {

	@Override
	public String format(LogRecord logRecord) {
		StringBuffer sb = initLogRecordStringBuffer(logRecord);
		StringBuffer classMethod = new StringBuffer(250);
		classMethod.append(logRecord.getSourceClassName()).append(".").append(logRecord.getSourceMethodName());
		sb.append("[").append(classMethod).append("]");
		//Add padding for classname/method
		sb = appendPadding(sb, classMethod.toString(), 85);
		//Add the log message
		sb.append("  ").append(logRecord.getMessage()).append(System.lineSeparator());
		return sb.toString();
	}

}
