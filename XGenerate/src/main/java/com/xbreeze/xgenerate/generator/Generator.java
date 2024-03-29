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
package com.xbreeze.xgenerate.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Logger;

import jakarta.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.FileUtils;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.NamespaceConfig;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.RootTemplateConfig;
import com.xbreeze.xgenerate.generator.GenerationResult.GenerationStatus;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.model.ModelException;
import com.xbreeze.xgenerate.model.ModelPreprocessor;
import com.xbreeze.xgenerate.model.ModelPreprocessorException;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.template.TemplateException;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.XsltTemplate;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationException;
import com.xbreeze.xgenerate.utils.XMLUtils;

import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltTransformer;

public class Generator {
	// The logger for this class.
	private static final Logger logger =  Logger.getLogger(Generator.class.getName());
	
	
	protected boolean _debugMode = false;
	
	protected boolean _testMode = false;

	/**
	 * Constructor.
	 * @param modelFileLocation The model file location.
	 */	
	public Generator() {
		logger.fine("Initializing generator");
	}
	
	public boolean isDebugMode() {
		return _debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this._debugMode = debugMode;
	}
	
	
	public boolean isTestMode() {
		return _testMode;
	}

	public void setTestMode(boolean testMode) {
		this._testMode = testMode;
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
	public GenerationResult generateFromFiles(URI modelFileUri, URI templateFileUri, URI configFileUri, URI outputFolderUri, String relativeTemplateFolderUri) throws GeneratorException {
		// Unmarshal the config file into a XGenConfig object.
		XGenConfig xGenConfig;
		try {
			xGenConfig = XGenConfig.fromFile(configFileUri);
		} catch (ConfigException e) {
			throw new GeneratorException(e);
		}
		
		// Create the model object from the model file.
		Model model;
		try {
			boolean namespaceAware = false;
			if (xGenConfig.getModelConfig() != null)
				namespaceAware = xGenConfig.getModelConfig().isNamespaceAware();
			model = Model.fromFile(modelFileUri, namespaceAware);
		} catch (ModelException me) {
			throw new GeneratorException(me);
		}
		
		// Create a RawTemplate object from the template file.
		RawTemplate rawTemplate;
		try {
			rawTemplate = RawTemplate.fromFile(templateFileUri);
		} catch (TemplateException e) {
			throw new GeneratorException(e);
		}
		
		// Generate using the model, template and config.
		return generate(model, rawTemplate, xGenConfig, outputFolderUri, relativeTemplateFolderUri);
	}
	
	
	public void generateFromFilesAndWriteOutput(URI modelFileUri, URI templateFileUri, URI configFileUri, URI outputFolderUri, String relativeTemplateFolderUri) throws GeneratorException {
		// Run the generation cycle and get the generation result.
		GenerationResult generationResult = generateFromFiles(modelFileUri, templateFileUri, configFileUri, outputFolderUri, relativeTemplateFolderUri);
		
		// If the generation failed, throw the exception.
		if (generationResult.getStatus().equals(GenerationStatus.ERROR))
			throw generationResult.getException();
	}
	
	private void writeToFile(String outputFileLocation, String outputFileContent) throws GeneratorException {
		// Open a write on the output file location.
		BufferedWriter writer;
		
		// If the output folder doesn't exist, create it.
		File outputFolderLocation = new File(outputFileLocation).getParentFile();
		try {
			FileUtils.forceMkdir(outputFolderLocation);
		} catch (IOException e1) {
			throw new GeneratorException(String.format("Error while creating folder '%s': %s.", outputFolderLocation.toString(), e1.getMessage()));
		}
		
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
	 * @param model The model.
	 * @param rawTemplate The raw template.
	 * @param xGenConfig The configuration.
	 * @return The GenerationResults.
	 * @throws IOException
	 * @throws TransformerException
	 * @throws UnhandledException 
	 * @throws UnknownAnnotationException 
	 */
	public GenerationResult generate(Model model, RawTemplate rawTemplate, XGenConfig xGenConfig, URI outputFolderUri, String relativeTemplateFolder) throws GeneratorException {
		
		// Pre-process the model (if model attribute injections are defined.
		if (xGenConfig.getModelConfig() != null) {
			try {
				ModelPreprocessor.preprocessModel(model, xGenConfig.getModelConfig());

				// If debug mode is on, write the pre-processed model to the output.
				if (this.isDebugMode()) {
					// Construct the path to the pre-processed model.
					String preprocessedModelLocation = Paths.get(outputFolderUri).resolve(relativeTemplateFolder).resolve(String.format("preprocessed_%s", model.getModelFileName())).toString();
					logger.info(String.format("Writing preprocessed model to '%s'", preprocessedModelLocation));
					// Write the pre-processed model.
					writeToFile(preprocessedModelLocation, model.getPreprocessedModel());
				}
			} catch (ModelPreprocessorException e) {
				throw new GeneratorException(e);
			}
		}

		// Initialize GenerationResults object.
		GenerationResult generationResult = new GenerationResult(model.getModelFileName(), rawTemplate.getRawTemplateFileName());

		// Perform the pre-processing and XSLT generation.
		{
			logger.info("Begin generator");
			
			try {
				// Get the template configuration.
				RootTemplateConfig templateConfig = xGenConfig.getTemplateConfig();				
				// Pre-process the template.
				String xsltTemplateString;
				{
					logger.info("Begin template pre-processing");
					// Get the template preprocessor for the template type we are dealing with.
					TemplatePreprocessor templatePreprocessor = templateConfig.getTemplatePreprocessor(xGenConfig);
					
					//Get the model namespaces if defined, needed to include in the template XSLT.
					ArrayList<NamespaceConfig> modelNamespaces = null;
					if (xGenConfig.getModelConfig() != null) {
						modelNamespaces = xGenConfig.getModelConfig().getNamespaces();
					}
					// Pre-process the raw template into a XSLT template.					
					XsltTemplate xsltTemplate = templatePreprocessor.preProcess(rawTemplate, relativeTemplateFolder, modelNamespaces);
					xsltTemplateString = xsltTemplate.toString();
					// If in debug mode, write the pre-processed template.
					if (this.isDebugMode()) {
						// Construct the path to the pre-processed template.
						String preprocessedFileLocation = Paths.get(outputFolderUri).resolve(String.format("preprocessed_%s", rawTemplate.getRawTemplateFileName())).toString();
						logger.info(String.format("Writing preprocessed template to '%s'", preprocessedFileLocation));
						writeToFile(preprocessedFileLocation, xsltTemplateString);
					}
					logger.info("End template pre-processing");
				}
				
				// Now the pre-processing is done, we can start the XSLT transformation using the model and the pre-processed template (XSLT).
				{
					logger.info("Begin template transformation");
					
					XsltTransformer xsltTransformer = XMLUtils.getXsltTransformer(xsltTemplateString, model.getPreprocessedModel(), outputFolderUri);
					
					// If running in test mode, cast the XslTransformer to net.sf.saxon.jaxp.TransformerImpl and set our custom
					// output resolver to get the output in GenerationResults instead of files					 
					if (this._testMode) {
						GenerationResultOutputResolver outputResolver = new GenerationResultOutputResolver(generationResult);
						xsltTransformer.getUnderlyingController().setOutputURIResolver(outputResolver);
					}
					
					// Perform the transformation.
					try {
						xsltTransformer.transform();
					}
					// We catch the SaxonApiException here and check for a specific error which occurs when the root node has no matches in the binding.
					catch (SaxonApiException e) {
						if (e.getMessage().equals("Result has no system ID, writer, or output stream defined")) {
							logger.warning("The generation yielded no results because the root node binding has no matches.");
						}
						// Re-throw all other errors.
						else {
							throw e;
						}
					}
					
					logger.info("End template transformation");
				}
			}
			
			// If an exception occurs, wrap it in a GeneratorException and set it on a new GenerationResult.
			catch (TemplatePreprocessorException | UnhandledException | GeneratorException | SaxonApiException e) {
				logger.severe(String.format("Error while generating: %s", e.getMessage()));
				// Store the exception in the generation result.
				generationResult.setException(new GeneratorException(e));
			}
			
			logger.info("End generator");
		}
			
		// Return the generation result.
		return generationResult;
	}
}
