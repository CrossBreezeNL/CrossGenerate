package com.xbreeze.xgenerate.test.util;

import java.io.OutputStream;
import java.io.PrintStream;

public class CapturedConsolePrintStream extends PrintStream {

	PrintStream _capturedStream;
	public CapturedConsolePrintStream(OutputStream out, PrintStream capturedPrintStream) {
		super(out);
		this._capturedStream = capturedPrintStream;
	}
	
	@Override
	public void write(int b) {		
		super.write(b);
		_capturedStream.write(b);
	}
	@Override
	public void write(byte[] buf, int off, int len) {
		super.write(buf, off, len);
		_capturedStream.write(buf, off, len);
	}
}
