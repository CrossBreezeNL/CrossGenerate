package com.xbreeze.xgenerate.generator;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.TemplateConfig;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.template.PreprocessedTemplate;
import com.xbreeze.xgenerate.template.PreprocessorException;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.template.TemplateException;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationException;
import com.xbreeze.xgenerate.generator.GeneratorStub;

import net.sf.saxon.TransformerFactoryImpl;

public class Generator extends GeneratorStub {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(Generator.class.getName());
	
	/**
	 * The Model to work with.
	 */
	private Model _model;

	private boolean _debugMode = false;
	
	/**
	 * Constructor.
	 * @param modelFileLocation The model file location.
	 */	
	public Generator() {
		logger.info("Initializing generator");
	}
	
	
	/**
	 * Set the model using a file location.
	 * @param modelFileUri The model file location.
	 */
	public void setModelFromFile(URI modelFileUri) {
		this._model = Model.fromFile(modelFileUri);
	}
	
	/**
	 * Set the model for generation.
	 * @param model The model
	 */
	public void setModel(Model model) {
		this._model = model;
	}
	
	@Override
	public boolean getDebugMode() {
		return _debugMode;
	}

	@Override
	public void setDebugMode(boolean _debugMode) {
		this._debugMode = _debugMode;
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
	public GenerationResults generateFromFiles(URI templateFileUri, URI configFileUri) throws GeneratorException, UnhandledException {
		// Create a RawTemplate object from the template file.
		RawTemplate rawTemplate;
		try {
			rawTemplate = RawTemplate.fromFile(templateFileUri);
		} catch (TemplateException e) {
			throw new GeneratorException(e);
		}
		
		// Unmarshal the config file into a XGenConfig object.
		XGenConfig xGenConfig;
		try {
			xGenConfig = XGenConfig.fromFile(configFileUri);
		} catch (ConfigException e) {
			throw new GeneratorException(e);
		}
		
		// Generate using the template and config.
		return generate(rawTemplate, xGenConfig);
	}
	
	@Override
	public void generateFromFilesAndWriteOutput(URI templateFileUri, URI configFileUri, URI outputFileUri) throws GeneratorException, UnhandledException {
		GenerationResults generationResults = generateFromFiles(templateFileUri, configFileUri);
		
		// For each generation result, write the results.
		for (GenerationResult generationResult : generationResults.getGenerationResults()) {
			
			// If debug mode is on, also write the pre-processed template to the output.
			if (this._debugMode) {
				String preprocessedFileLocation = Paths.get(outputFileUri).resolve(String.format("preprocessed_%s", generationResult.getTemplateFileName())).toString();
				logger.info(String.format("Writing preprocessed template to '%s'", preprocessedFileLocation));
				writeToFile(preprocessedFileLocation, generationResult.getPreprocessedTemplate());
			}
			
			// Write the output to a file.
			String outputFileLocation = Paths.get(outputFileUri).resolve(generationResult.getOutputFileLocation()).toString();
			logger.info(String.format("Writing result to '%s'", outputFileLocation));
			// Write the output file content to a file.
			writeToFile(outputFileLocation, generationResult.getOutputFileContent());

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
	public GenerationResults generate(RawTemplate rawTemplate, XGenConfig xGenConfig) throws GeneratorException, UnhandledException {
		try {
			logger.info("Begin generator");
			
			// Initialize GenerationResults object.
			GenerationResults generationResults = new GenerationResults();
			
			// Get the template configuration.
			TemplateConfig templateConfig = xGenConfig.getTemplateConfig();
			// Get the template preprocessor for the template type we are dealing with.
			TemplatePreprocessor templatePreprocessor = templateConfig.getFileFormatConfig().getTemplateType().getTemplatePreprocessor(xGenConfig);
			// Pre-process the raw template into a pre-processed template.
			logger.info("Begin template pre-processing");
			PreprocessedTemplate preprocessedTemplate = templatePreprocessor.preProcess(rawTemplate);
			logger.info("End template pre-processing");
			
			String preprocessedTemplateString = preprocessedTemplate.toString();
			// TODO: Disable the following when generation is stable.
			/**
			System.out.println("Pre-processed template:");
			System.out.println("--------------------------------------------------");
			System.out.println(preprocessedTemplateString);
			System.out.println("--------------------------------------------------");
			*/
			
			// Now the pre-processing is done, we can start the XSLT transformation using the model and the pre-processed template (XSLT).
			logger.info("Begin XSLT transformation");
			StreamSource modelStreamSource;
			try {
				modelStreamSource = _model.getAsStreamSource();
			} catch (FileNotFoundException e) {
				throw new GeneratorException(String.format("The model file was not found (%s)", _model.getModelFileUri().toString()));
			}
			
			// Create a string reader on the pre-processed template.
			StringReader xslStringReader = new StringReader(preprocessedTemplateString);
			StreamSource xslSource = new StreamSource(xslStringReader);
			
			// Create a stream writer, to write the resulting output.
			StringWriter xslResultWriter = new StringWriter();
			StreamResult outputResult = new StreamResult(xslResultWriter);
			
			// Create the transformer objects to transform the XSLT with the Model into the output.
			String xslResult;
			try {
				TransformerFactory xsltFactory = TransformerFactoryImpl.newInstance();
				Transformer xslTransformer = xsltFactory.newTransformer(xslSource);
				xslTransformer.transform(modelStreamSource, outputResult);
				xslResult = xslResultWriter.toString();
			} catch (TransformerException e) {
				throw new GeneratorException(e);
			}
			
			// Add the resulting output to the GenerationResults.
			// TODO Split the output in multiple files based on the Output type.
			// TODO Apply the placeholders of the root section on the file name.
			generationResults.addGenerationResult(new GenerationResult(rawTemplate.getRawTemplateFileName(), preprocessedTemplateString, xslResult, rawTemplate.getRawTemplateFileName()));
			logger.info("End XSLT transformation");
			
			// TODO: Disable the following when generation is stable.
			/**
			System.out.println("Result:");
			System.out.println("--------------------------------------------------");
			System.out.println(xslResult);
			System.out.println("--------------------------------------------------");
			*/
			
			logger.info("End generator");
			
			// Return the generation results.
			return generationResults;
		} catch (PreprocessorException e) {
			throw new GeneratorException(e);
		}
	}
}
