package com.xbreeze.xgenerate.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

public class FileUtils {

	public static String getFileContent(URI fileLocation) throws IOException {
		// Create a input stream from the template file.
		FileInputStream fis = new FileInputStream(new File(fileLocation));
		// Wrap the input stream in a BOMInputStream so it is invariant for the BOM.
		BOMInputStream bomInputStream = new BOMInputStream(fis);
		// Create a String using the BOMInputStream and the charset.
		// The charset can be null, this gives no errors.
		return IOUtils.toString(bomInputStream, bomInputStream.getBOMCharsetName());
	}
}
