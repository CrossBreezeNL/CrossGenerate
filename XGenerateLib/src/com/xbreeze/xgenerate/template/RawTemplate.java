package com.xbreeze.xgenerate.template;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

public class RawTemplate {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(RawTemplate.class.getName());
	
	/**
	 * The file name of the raw template.
	 */
	private String _rawTemplateFileName;

	/**
	 * The file location of the raw template.
	 */
	private String rawTemplateFileLocation;
	
	/**
	 * The raw template.
	 */
	private String _rawTemplateContent;
	
	/**
	 * Constructor.
	 * @param rawTemplateContent The raw template.
	 */
	public RawTemplate(String rawTemplateFileName, String rawTemplateFileLocation, String rawTemplateContent) {
		this._rawTemplateFileName = rawTemplateFileName;
		this.rawTemplateFileLocation = rawTemplateFileLocation;
		this._rawTemplateContent = rawTemplateContent;
	}
	
	/**
	 * @return the rawTemplateFileName
	 */
	public String getRawTemplateFileName() {
		return _rawTemplateFileName;
	}
	
	/**
	 * @return the rawTemplateFileLocation
	 */
	public String getRawTemplateFileLocation() {
		return rawTemplateFileLocation;
	}

	/**
	 * @return the rawTemplate
	 */
	public String getRawTemplateContent() {
		return _rawTemplateContent;
	}
	
	public static RawTemplate fromFile(URI rawTemplateFileUri) throws TemplateException {
		// Construct a Path object using the file location.
		logger.info(String.format("Creating RawTemplate object from '%s'", rawTemplateFileUri));
		Path rawTemplateFilePath = Paths.get(rawTemplateFileUri);
		
		// Get the file name and its content.
		String rawTemplateFileName = rawTemplateFilePath.getFileName().toString();
		String rawTemplateContent;
		
		// Try to read the template file into a string.
		try {
			// Create a input stream from the template file.
			FileInputStream fis = new FileInputStream(rawTemplateFilePath.toFile());
			// Wrap the input stream in a BOMInputStream so it is invariant for the BOM.
			BOMInputStream bomInputStream = new BOMInputStream(fis);
			// Create a String using the BOMInputStream and the charset.
			// The charset can be null, this gives no errors.
			String rawTemplateString = IOUtils.toString(bomInputStream, bomInputStream.getBOMCharsetName());
			// Double entity-encode XML entity encoded stuff.
			rawTemplateContent = TemplatePreprocessor.doubleEntityEncode(rawTemplateString);
		} catch (IOException e) {
			throw new TemplateException(String.format("Couldn't read the template file (%s)", rawTemplateFilePath));
		}
		
		// Return the RawTemplate object.
		return new RawTemplate(rawTemplateFileName, rawTemplateFilePath.toString(), rawTemplateContent);
	}
}
