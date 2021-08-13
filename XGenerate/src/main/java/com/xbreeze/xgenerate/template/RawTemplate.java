/*******************************************************************************
 *   Copyright (c) 2021 CrossBreeze
 *
 *   This file is part of CrossGenerate.
 *
 *      CrossGenerate is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      CrossGenerate is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with CrossGenerate.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *      Willem Otten - CrossBreeze
 *      Harmen Wessels - CrossBreeze
 *      Jacob Siemaszko - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.template;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.xbreeze.xgenerate.utils.FileUtils;

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
		logger.fine(String.format("Creating RawTemplate object from '%s'", rawTemplateFileUri));
		Path rawTemplateFilePath = Paths.get(rawTemplateFileUri);
		
		// Get the file name and its content.
		String rawTemplateFileName = rawTemplateFilePath.getFileName().toString();
		String rawTemplateContent;
		
		// Try to read the template file into a String.
		try {
			rawTemplateContent = FileUtils.getFileContent(rawTemplateFileUri);
		} catch (IOException e) {
			throw new TemplateException(String.format("Couldn't read the template file (%s): %s", rawTemplateFilePath, e.getMessage()));
		}
		
		// Return the RawTemplate object.
		return new RawTemplate(rawTemplateFileName, rawTemplateFilePath.toString(), rawTemplateContent);
	}
}
