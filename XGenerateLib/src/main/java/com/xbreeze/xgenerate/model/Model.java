package com.xbreeze.xgenerate.model;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.template.xml.XMLUtils;

public class Model {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(Model.class.getName());

	/**
	 * The model file location.
	 */
	private String _modelFileName;
	
	/**
	 * The model Document.
	 */
	private Document _modelDocument;
	
	/**
	 * Constructor.
	 * @param modelFileUri The model file location.
	 */
	public Model(String modelFileName, Document modelDocument) {
		this._modelFileName = modelFileName;
		this._modelDocument = modelDocument;
	}
	
	/**
	 * @return the modelDocument
	 */
	public Document getModelDocument() {
		return _modelDocument;
	}

	/**
	 * @param modelDocument the modelDocument to set
	 */
	public void setModelDocument(Document modelDocument) {
		this._modelDocument = modelDocument;
	}
	
	/**
	 * Get the model document as a DOMSource.
	 * @return
	 */
	public DOMSource getAsDOMSource() {
		return new DOMSource(this._modelDocument);
	}
	
	/**
	 * Get the Model object using a model file location.
	 * @param modelFileUri The model file location.
	 * @return The Model object.
	 * @throws GeneratorException 
	 */
	public static Model fromFile(URI modelFileUri) throws GeneratorException {
		logger.fine(String.format("Creating Model object from '%s'", modelFileUri));
		Document modelDocument;
		DocumentBuilder documentBuilder = XMLUtils.getDocumentBuilder();
		try {
			modelDocument = documentBuilder.parse(modelFileUri.toString());
		}
		// IOException when reading the model file.
		catch (IOException e) {
			throw new GeneratorException(String.format("Couldn't read the model file: %s", e.getMessage()));
		}
		// SAXException when parsing the model file.
		catch (SAXException e) {
			throw new GeneratorException(String.format("Couldn't parse the model file: %s", e.getMessage()));
		}
		return new Model(Paths.get(modelFileUri).getFileName().toString(), modelDocument);
	}

	/**
	 * @return the modelFileName
	 */
	public String getModelFileName() {
		return _modelFileName;
	}

	/**
	 * @param modelFileName the modelFileName to set
	 */
	public void setModelFileName(String modelFileName) {
		this._modelFileName = modelFileName;
	}
}
