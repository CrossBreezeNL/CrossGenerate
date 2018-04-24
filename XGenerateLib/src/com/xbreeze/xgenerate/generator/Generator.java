package com.xbreeze.xgenerate.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.TemplateConfig;
import com.xbreeze.xgenerate.generator.GenerationResult.GenerationStatus;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.model.ModelPreprocessor;
import com.xbreeze.xgenerate.model.ModelPreprocessorException;
import com.xbreeze.xgenerate.template.XsltTemplate;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.template.TemplateException;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationException;
import com.xbreeze.xgenerate.template.xml.XMLUtils;
import com.xbreeze.logging.LogHelper;
import com.xbreeze.logging.LogLevel;

public class Generator extends GeneratorStub {
	// The logger for this class.
	private static final Logger logger = LogHelper.getLogger();
	
	/**
	 * The Model to work with.
	 */
	private Model _model;

	/**
	 * Constructor.
	 * @param modelFileLocation The model file location.
	 */	
	public Generator() {
		logger.fine("Initializing generator");
		
	}
		
	/**
	 * Set the model using a file location.
	 * @param modelFileUri The model file location.
	 * @throws GeneratorException 
	 */
	public void setModelFromFile(URI modelFileUri) throws GeneratorException {
		this._model = Model.fromFile(modelFileUri);
	}
	
	/**
	 * Set the model for generation.
	 * @param model The model
	 */
	public void setModel(Model model) {
		this._model = model;
	}
	
	/**
	 * Get the model that is set on the generator.
	 */
	public Model getModel() {
		return this._model;
	}
	

	

	@Override
	public void setLogLevelAndDestination(LogLevel logLevel, URI logDestination) throws GeneratorException {
		this._logDestination = logDestination;
		this._logLevel = logLevel;
		//if loglevel is set to verbose, and a log destination is given, make sure that already existing log handlers are set to informational
		if (logLevel == LogLevel.VERBOSE && logDestination != null) {
			for (Handler h: logger.getHandlers()) {
				h.setLevel(Level.INFO);				
			}
		}
		//if logDestination is not null, assign a file handler to the logger
		if (logDestination != null) {
			try {
				FileHandler fh = new FileHandler(logDestination.toString(), true);
				fh.setLevel(LogHelper.translateLogLevel(logLevel));
				logger.addHandler(fh);
			} catch (SecurityException | IOException e) {
				throw new GeneratorException(String.format("Error setting log destination: %s", e.getMessage()));
			}			
		}
	}

	/**
	 * Generate the output using the raw-template and the config file locations.
	 * @param templateFileUri The template-file location.
	 * @param configFileUri The config-file location.
	 * @return The GenerationResults.
	 * @throws IOException
	 * @throws JAXBException
	 * @throws TransformerException
	 * @throws UnhandledException 
	 * @throws UnknownAnnotationException 
	 */
	public GenerationResults generateFromFiles(URI templateFileUri, URI configFileUri, URI outputFileUri) throws GeneratorException {
		// Unmarshal the config file into a XGenConfig object.
		XGenConfig xGenConfig;
		try {
			xGenConfig = XGenConfig.fromFile(configFileUri);
		} catch (ConfigException e) {
			throw new GeneratorException(e);
		}
		
		// Create a RawTemplate object from the template file.
		RawTemplate rawTemplate;
		try {
			rawTemplate = RawTemplate.fromFile(templateFileUri);
		} catch (TemplateException e) {
			throw new GeneratorException(e);
		}
		
		// Generate using the template and config.
		return generate(rawTemplate, xGenConfig, outputFileUri);
	}
	
	@Override
	public void generateFromFilesAndWriteOutput(URI templateFileUri, URI configFileUri, URI outputFileUri) throws GeneratorException {
		GenerationResults generationResults = generateFromFiles(templateFileUri, configFileUri, outputFileUri);
		
		// For each generation result, write the results.
		for (GenerationResult generationResult : generationResults.getGenerationResults()) {
			
			// If debug mode is on, also write the pre-processed template to the output.
			if (this._debugMode) {
				// Construct the path to the pre-processed model.
				File preprocessedModelLocation = Paths.get(outputFileUri).resolve(String.format("preprocessed_%s", generationResult.getModelFileName())).toFile();
				logger.info(String.format("Writing preprocessed model to '%s'", preprocessedModelLocation));
				try {
					Transformer xmlTransformer = XMLUtils.getXmlTransformer().newTransformer();
					xmlTransformer.transform(_model.getAsDOMSource(), new StreamResult(preprocessedModelLocation));
				}
				// Error while getting the XML Transformer.
				catch (TransformerConfigurationException e) {
					throw new GeneratorException(e);
				}
				// Error while transforming the model into a XML.
				catch (TransformerException e) {
					throw new GeneratorException(e);
				}
				
				// If there is a pre-processed template, write it to the output folder.
				if (generationResult.getPreprocessedTemplate() != null) {
					// Construct the path to the pre-processed template.
					String preprocessedFileLocation = Paths.get(outputFileUri).resolve(String.format("preprocessed_%s", generationResult.getTemplateFileName())).toString();
					logger.info(String.format("Writing preprocessed template to '%s'", preprocessedFileLocation));
					writeToFile(preprocessedFileLocation, generationResult.getPreprocessedTemplate());
				}
			}
			
			// If there is output available, write it to the output folder.
//			if (generationResult.getOutputFileContent() != null) {
//				// Write the output to a file.
//				String outputFileLocation = Paths.get(outputFileUri).resolve(generationResult.getOutputFileLocation()).toString();
//				logger.info(String.format("Writing result to '%s'", outputFileLocation));
//				// Write the output file content to a file.
//				writeToFile(outputFileLocation, generationResult.getOutputFileContent());
//			}
			
			// If the generation failed, throw the exception.
			if (generationResult.getStatus().equals(GenerationStatus.ERROR))
				throw generationResult.getException();
		}
	}
	
	private void writeToFile(String outputFileLocation, String outputFileContent) throws GeneratorException {
		// Open a write on the output file location.
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(outputFileLocation));
		} catch (IOException e) {
			throw new GeneratorException(String.format("Couldn't open writer to output location '%s': %s", outputFileLocation, e.getMessage()));
		}
		// Write the result to the file.
		try {
			writer.write(outputFileContent);
		} catch (IOException e) {
			throw new GeneratorException(String.format("Couldn't write result file to output location '%s': %s", outputFileLocation, e.getMessage()));
		} finally {
			// Close the writer.
			try {
				writer.close();
			} catch (IOException e) {
				throw new GeneratorException(String.format("Couldn't close writer for output location '%s': %s", outputFileLocation, e.getMessage()));
			}
		}
	}
	
	/**
	 * Generate the output using the raw-template and the config.
	 * @param rawTemplate The raw template.
	 * @param xGenConfig The configuration.
	 * @return The GenerationResults.
	 * @throws IOException
	 * @throws TransformerException
	 * @throws UnhandledException 
	 * @throws UnknownAnnotationException 
	 */
	public GenerationResults generate(RawTemplate rawTemplate, XGenConfig xGenConfig, URI outputFileUri) throws GeneratorException {
		
		// Pre-process the model (if model attribute injections are defined.
		if (xGenConfig.getModelConfig() != null) {
			try {
				ModelPreprocessor.preprocessModel(_model, xGenConfig.getModelConfig());
			} catch (ModelPreprocessorException e) {
				throw new GeneratorException(e);
			}
		}

		// Initialize GenerationResults object.
		GenerationResults generationResults = new GenerationResults();

		// Perform the pre-processing and XSLT generation.
		{
			logger.info("Begin generator");
			
			try {
				// Get the template configuration.
				TemplateConfig templateConfig = xGenConfig.getTemplateConfig();				
				// Pre-process the template.
				String xsltTemplateString;
				{
					// Get the template preprocessor for the template type we are dealing with.
					TemplatePreprocessor templatePreprocessor = templateConfig.getFileFormatConfig().getTemplateType().getTemplatePreprocessor(xGenConfig);
					// Pre-process the raw template into a xslt template.
					logger.info("Begin template pre-processing");
					XsltTemplate xsltTemplate = templatePreprocessor.preProcess(rawTemplate, outputFileUri);
					xsltTemplateString = xsltTemplate.toString();
					// If in debug mode, write the preprocessed template.
					if (this.isDebugMode()) {
						System.out.println("---------- Preprocessed template: ----------");
						System.out.println(xsltTemplateString);
						System.out.println("--------------------------------------------");
					}
					//generationResult.setPreprocessedTemplate(preprocessedTemplateString);
					logger.info("End template pre-processing");
				}
				
				// Now the pre-processing is done, we can start the XSLT transformation using the model and the pre-processed template (XSLT).
				{
					logger.info("Begin XSLT transformation");
					
					// Create a string reader on the pre-processed template.
					StringReader xslStringReader = new StringReader(xsltTemplateString);
					StreamSource xslSource = new StreamSource(xslStringReader);
					
					// Create a stream writer, to write the resulting output.
					StringWriter xslResultWriter = new StringWriter();
					StreamResult outputResult = new StreamResult(xslResultWriter);
					
					// Create the transformer objects to transform the XSLT with the Model into the output.
					Transformer xslTransformer = XMLUtils.getXmlTransformer().newTransformer(xslSource);
					
					// If running in test mode, cast the xslTransformer to net.sf.saxon.jaxp.TransformerImpl and set our custom
					// output resolver to get the output in GenerationResults instead of files					 
					if (this._testMode) {
						GenerationResultsOutputResolver outputResolver = new GenerationResultsOutputResolver(generationResults,_model.getModelFileName(), rawTemplate.getRawTemplateFileName());
						((net.sf.saxon.jaxp.TransformerImpl)xslTransformer).getUnderlyingController().setOutputURIResolver(outputResolver);
					}
					
					//Invoke transform on the model
					xslTransformer.transform(_model.getAsDOMSource(), outputResult);								
					
					logger.info("End XSLT transformation");
				}
			}
			
			// If an exception occurs, wrap it in a GeneratorException and set it on a new GenerationResult.
			catch (TemplatePreprocessorException | TransformerException | UnhandledException e) {
				logger.severe(String.format("Error while generating: %s", e.getMessage()));
				GenerationResult generationExeptionResult = new GenerationResult(_model.getModelFileName(), rawTemplate.getRawTemplateFileName());				
				generationExeptionResult.setException(new GeneratorException(e));
				generationResults.addGenerationResult(generationExeptionResult);
			}
			
			logger.info("End generator");
		}
			
		// Return the generation results.
		return generationResults;
	}
}
