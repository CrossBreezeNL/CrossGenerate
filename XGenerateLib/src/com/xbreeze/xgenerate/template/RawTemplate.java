package com.xbreeze.xgenerate.template;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

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
			rawTemplateContent = new String(Files.readAllBytes(rawTemplateFilePath));
			// Double entity-encode XML entity encoded stuff.
			rawTemplateContent = rawTemplateContent.replaceAll("&([a-zA-Z0-9]+;)", "&amp;$1");
		} catch (IOException e) {
			throw new TemplateException(String.format("Couldn't read the template file (%s)", rawTemplateFilePath));
		}
		
		// Return the RawTemplate object.
		return new RawTemplate(rawTemplateFileName, rawTemplateFilePath.toString(), rawTemplateContent);
	}
}
