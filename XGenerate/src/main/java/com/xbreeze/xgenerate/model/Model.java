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

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.utils.FileUtils;

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
	private Document _modelDocument;
	
	/**
	 * Constructor.
	 * @param modelFileUri The model file location.
	 */
	private Model(URI modelFileUri, Document modelDocument) {
		this._modelFileUri = modelFileUri;
		this._modelDocument = modelDocument;
	}
	
	/**
	 * Get the model document.
	 * @return The model document.
	 */
	public Document getModelDocument() {
		return this._modelDocument;
	}
	
	/**
	 * Get the Model object using a model file location.
	 * @param modelFileUri The model file location.
	 * @param modelConfig The model configuration.
	 * @return The Model object.
	 * @throws ModelException 
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
	 * @param modelFileContents The model file contents.
	 * @param modelFileUri The model file location.
	 * @param modelConfig The model configuration.
	 * @return The Model object.
	 * @throws ModelException
	 */
	public static Model fromString(String modelFileContents, URI modelFileUri, ModelConfig modelConfig) throws ModelException {
		try {
			// Parse the XML document.
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			// Use model configuration to set namespace awareness.
			if (modelConfig != null)
				saxParserFactory.setNamespaceAware(modelConfig.isNamespaceAware());
			// Resolve XIncludes.
			saxParserFactory.setXIncludeAware(true);
			// Create the SAX parser using the factory.
            SAXParser saxParser = saxParserFactory.newSAXParser();
            
            // Wrap the XMLReader with a NamespaceContext that ensures the xi prefix is mapped
            // NOTE: I tried creating my own XMLFilterImpl and injecting the needed XInclude namespace on the root element, but this didn't work.
            //XMLReaderWithNamespaceContext xmlReader = new XMLReaderWithNamespaceContext(saxParser.getXMLReader(), "xi", "http://www.w3.org/2001/XInclude");

            // Use SAXSource to process the XML content
            InputSource inputSource = new InputSource(new StringReader(modelFileContents));
            SAXSource saxSource = new SAXSource(saxParser.getXMLReader(), inputSource);
            // Set the system-id to the location of the model file, so it can resolve includes, if needed.
            saxSource.setSystemId(modelFileUri.toString());
            
            // Transform SAX events into a DOM Document
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMResult domResult = new DOMResult();
            transformer.transform(saxSource, domResult);
            
            // Retrieve the Document
            Document modelDocument = (Document) domResult.getNode();
            
            // Return the new Model object.
            return new Model(modelFileUri, modelDocument);
		} catch (ParserConfigurationException | SAXException | TransformerException e) {
			if (e.getCause() != null)
				throw new ModelException(String.format("Error while reading model: %s", e.getCause().getMessage()), e.getCause());
			else
				throw new ModelException(String.format("Error while reading model: %s", e.getMessage()), e);
		}		
	}

	/**
	 * @return the modelFileName
	 */
	public String getModelFileName() {
		return Paths.get(this._modelFileUri).getFileName().toString();
	}
}
