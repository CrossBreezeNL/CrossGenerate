package com.xbreeze.xgenerate.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.logging.Logger;

import javax.xml.transform.stream.StreamSource;

public class Model {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(Model.class.getName());

	/**
	 * The model file location.
	 */
	private URI _modelFileUri;
	
	/**
	 * Constructor.
	 * @param modelFileUri The model file location.
	 */
	public Model(URI modelFileUri) {
		this.setModelFileUri(modelFileUri);
	}
	
	/**
	 * Get the model file as stream source.
	 * @return The stream source on the model.
	 * @throws FileNotFoundException
	 */
	public StreamSource getAsStreamSource() throws FileNotFoundException {
		return new StreamSource(new FileInputStream(new File(getModelFileUri())));
	}
	
	/**
	 * Get the Model object using a model file location.
	 * @param modelFileUri The model file location.
	 * @return The Model object.
	 */
	public static Model fromFile(URI modelFileUri) {
		logger.info(String.format("Creating Model object from '%s'", modelFileUri));
		return new Model(modelFileUri);
	}

	/**
	 * @return the modelFileUri
	 */
	public URI getModelFileUri() {
		return _modelFileUri;
	}

	/**
	 * @param modelFileUri the modelFileUri to set
	 */
	public void setModelFileUri(URI modelFileUri) {
		this._modelFileUri = modelFileUri;
	}
}
