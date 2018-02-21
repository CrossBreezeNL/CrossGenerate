package com.xbreeze.xgenerate.generator;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Skeleton Generator class, used for building the project, runtime the Generator class is loaded through the licensedClassLoader, 
 * this class only functions as a placeholder
 * @author Willem
 *
 */
public class GeneratorStub {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(GeneratorStub.class.getName());
	
	private boolean _debugMode = false;

	/**
	 * Constructor.
	 * @param modelFileLocation The model file location.
	 */
	public GeneratorStub() {
		logger.info("Initializing generator");
	}
	
	public boolean getDebugMode() {
		return _debugMode;
	}


	public void setDebugMode(boolean _debugMode) {
		this._debugMode = _debugMode;
	}
	/**
	 * Set the model using a file location.
	 * @param modelFileUri The model file location.
	 * @throws GeneratorException 
	 */
	public void setModelFromFile(URI modelFileUri) throws GeneratorException {
		logger.warning("Invoking placeholder Generator");
	}
		
	public void generateFromFilesAndWriteOutput(URI templateFileUri, URI configFileUri, URI outputFileUri) throws GeneratorException {
		logger.warning("Invoking placeholder Generator");
	}
	
}
