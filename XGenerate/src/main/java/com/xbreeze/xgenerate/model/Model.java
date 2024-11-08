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
package com.xbreeze.xgenerate.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.utils.FileUtils;
import com.xbreeze.xgenerate.utils.SaxonXMLUtils;
import com.xbreeze.xgenerate.utils.XmlException;

public class Model {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(Model.class.getName());

	/**
	 * The model file location.
	 */
	private URI _modelFileUri;
	
	/**
	 * The model file content.
	 */
	private String _modelFileContent;
	
	/**
	 * The preprocessed model.
	 */
	private String _preprocessedModel;
	
	/**
	 * Constructor.
	 * @param modelFileUri The model file location.
	 */
	private Model(URI modelFileUri, String modelFileContent) {
		this._modelFileUri = modelFileUri;
		this._modelFileContent = modelFileContent;
		// Store the initial model content in the preprocessed model, in case there is no pre-processing.
		setPreprocessedModel(this._modelFileContent);
	}
	
	/**
	 * Get the model file content.
	 * @return The model file content.
	 */
	public String getModelFileContent() {
		return this._modelFileContent;
	}
	
	/**
	 * Get the Model object using a model file location.
	 * @param modelFileUri The model file location.
	 * @return The Model object.
	 * @throws GeneratorException 
	 */
	public static Model fromFile(URI modelFileUri, ModelConfig modelConfig) throws ModelException {
		logger.fine(String.format("Creating Model object from '%s'", modelFileUri));
		
		// Read the model file content into a String.
		String modelFileContent;
		try {
			modelFileContent = FileUtils.getFileContent(modelFileUri);
		} catch (IOException e) {
			throw new ModelException(String.format("Couldn't read the model file (%s): %s", modelFileUri, e.getMessage()));
		}
		
		return fromString(modelFileContent, modelFileUri, modelConfig);
	}
	
	/**
	 * Static function to construct a Model object from a string.
	 * @param modelFileUri
	 * @param modelFileContents
	 * @return
	 * @throws ModelException
	 */
	public static Model fromString(String modelFileContents, URI modelFileUri, ModelConfig modelConfig) throws ModelException {
		String resolvedModelFileContents;
		try {
			// Parse the XML document using Saxon.
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			if (modelConfig != null)
				factory.setNamespaceAware(modelConfig.isNamespaceAware());
			factory.setXIncludeAware(true);
			DocumentBuilder builder;
			
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException exc) {			
				throw new ModelException(
						String.format("Error while reading model XML file: %s", exc.getMessage()));
			}
			Document doc;
			
			try {
				// Set the system-id to the location of the model file, so it can resolve includes, if needed.
				doc = builder.parse(new ByteArrayInputStream(modelFileContents.getBytes()), modelFileUri.toString());
			} catch(SAXException | IOException exc) {
				throw new ModelException(String.format("Error while reading model XML file: %s", exc.getMessage()), exc.getCause());
			}
			
			resolvedModelFileContents = SaxonXMLUtils.XmlDocumentToString(doc);
		} catch (XmlException xec) {
			throw new ModelException(String.format("Error while reading model: %s", xec.getMessage()), xec);
		}
		
		// Return the new Model object.
		return new Model(modelFileUri, resolvedModelFileContents);
	}

	/**
	 * @return the modelFileName
	 */
	public String getModelFileName() {
		return Paths.get(this._modelFileUri).getFileName().toString();
	}

	/**
	 * @return the preprocessedModel
	 */
	public String getPreprocessedModel() {
		return _preprocessedModel;
	}

	/**
	 * @param preprocessedModel the preprocessedModel to set
	 */
	public void setPreprocessedModel(String preprocessedModel) {
		this._preprocessedModel = preprocessedModel;
	}
}
