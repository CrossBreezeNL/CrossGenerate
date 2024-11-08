package com.xbreeze.xgenerate.test.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.http.client.utils.URIBuilder;

import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.generator.GenerationOutput;
import com.xbreeze.xgenerate.generator.GenerationResult;
import com.xbreeze.xgenerate.generator.GenerationResult.GenerationStatus;
import com.xbreeze.xgenerate.generator.Generator;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.model.ModelException;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.test.util.CapturedConsolePrintStream;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class XGenerateLibTestSteps {
	private final URI _outputFolderUri = URI.create("file:///C:/CrossGenerate/Output/");
	// Location of the feature support files.
	private URI _featureSupportFilesLocation;
	
	XGenConfig _xGenConfig;
	Model _model;
	RawTemplate _rawTemplate;
	Generator _generator;
	GenerationResult _generationResults;
	URI _modelFileUri;
	URI _templateFileUri;
	URI _configFileUri;
	Exception generatorException;
	private ByteArrayOutputStream baos;
	private PrintStream stdOut;
	private PrintStream stdErr;
	
	@Before
	public void beforeScenario(Scenario scenario) throws Exception {
		
		//Create a arraybuffer and capturedConsolePrintStreams for out and err printstreams
		baos = new ByteArrayOutputStream();
		stdOut = System.out;
		stdErr = System.err;
		CapturedConsolePrintStream newOut = new CapturedConsolePrintStream(baos, stdOut);
		CapturedConsolePrintStream newErr = new CapturedConsolePrintStream(baos, stdErr);
		
		//Redirect std and err out to the new captured streams
		System.setOut(newOut);
		System.setErr(newErr);
		
		LogManager logManager = LogManager.getLogManager();
		// Read the logging configuration from the resource file.
		try {
			logManager.readConfiguration(XGenerateLibTestSteps.class.getResourceAsStream("logging.properties"));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		Logger logger = logManager.getLogger("");
		
		Level logLevel = Level.INFO;
		// When running in debug mode, output with debug log formatting.
		if (scenario.getSourceTagNames().contains("@Debug")) {
			// Set the console handler log level.
			logLevel = Level.FINE;
			// Set the log format for debug mode.
			System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$-7s] %5$s [%2$s] %n");
		}
		
		// Set log level for the logger.
		logger.setLevel(logLevel);
		// Add a logger for the console to log message below warning (and error).
		ConsoleHandler outputConsoleHandler = new ConsoleHandler() {
			@Override
			protected synchronized void setOutputStream(OutputStream out) throws SecurityException {
				super.setOutputStream(System.out);
			}
		};
		
		outputConsoleHandler.setLevel(logLevel);
		// Only log message with a lower level then warning.
		outputConsoleHandler.setFilter(new Filter() {
			@Override
			public boolean isLoggable(LogRecord record) {
				return record.getLevel().intValue() < Level.SEVERE.intValue();
			}
		});
		logger.addHandler(outputConsoleHandler);
		
		// Get the class loader.
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// Get the feature-file location.
		String featureFileLocation = loader.getResource(scenario.getUri().getRawSchemeSpecificPart()).toString();
		logger.info(String.format("Feature file location: '%s'.", featureFileLocation));
		
		// Set the feature support file location using the scenario location.
		// The support file location is the same as the feature file location, but without the .feature extension and the directory 'features' is replaced with 'feature-support-files'.
		String derivedFeatureSupportFileLocation = featureFileLocation.replaceFirst("features", "feature-support-files").replace(".feature", "/");
		logger.info(String.format("The feature-support-file location will be set to '%s'.", derivedFeatureSupportFileLocation));
		_featureSupportFilesLocation = URI.create(derivedFeatureSupportFileLocation);
		
		//Clear the exception variable
		this.generatorException = null;
		
		// Initialize the generator.
		this._generator = new Generator();
		this._generator.setTestMode(true);
		
		// Enable debug mode if there is a tag @Debug.
		if (scenario.getSourceTagNames().contains("@Debug")) {
			logger.info("Enabling debug mode");
			this._generator.setDebugMode(true);
		}
	}
	
	@After
	private void RestoreSystemStreams() {
		//Restore std and err output streams
		System.setOut(stdOut);
		System.setErr(stdErr);
	}

	@Given("^I have the following model:$")
	public void iHaveTheFollowingModel(String modelContent) throws Throwable {
		try {
			this._model = Model.fromString(modelContent, this._featureSupportFilesLocation.resolve("inline-model.xml"), (_xGenConfig != null) ? _xGenConfig.getModelConfig() : null);
		} catch (ModelException mex) {
			this.generatorException = mex;
		}
	}
	
	@Given("^I have the following model file: \"(.*)\"$")
	public void iHaveTheFollowingModelFile(String modelFileLocation) throws Throwable {
		this._modelFileUri = resolveSupportFile(modelFileLocation);
	}

	@And("^the following template named \"(.*)\":$")
	public void theFollowingTemplateNamed(String templateName, String templateContent) throws Throwable {
		// Create the raw template.
		this._rawTemplate = new RawTemplate(templateName, null, templateContent);
	}

	@And("^the following template file: \"(.*)\"$")
	public void theFollowingTemplateFile(String templateFileLocation) throws Throwable {
		// Create the raw template.
		this._templateFileUri = resolveSupportFile(templateFileLocation);
	}
	
	@And("^the following config:$")
	public void theFollowingConfig(String configContent) throws Throwable {
		try {
			this._xGenConfig = XGenConfig.fromString(configContent.replace("{{support-file-location}}", new File(this._featureSupportFilesLocation).toString()), this._featureSupportFilesLocation);
		} catch(ConfigException exc) {
			this.generatorException = exc;
		}
	}

	@And("^the following config file: \"(.*)\"$")
	public void theFollowingConfigFile(String configFileLocation) throws Throwable {
		// Create the raw template.
		this._configFileUri = resolveSupportFile(configFileLocation);
	}	

	@When("^I run the generator$")
	public void iRunTheGenerator() throws Throwable {	
		checkForError();
		
		//check if generator needs to be invoked with files or with template and config string
		try {
			if (this._model != null && this._rawTemplate != null && this._xGenConfig != null) {
				_generationResults = this._generator.generate(this._model, this._rawTemplate, this._xGenConfig, this._outputFolderUri, "");
			}
			else if (this._modelFileUri != null && this._templateFileUri != null && this._configFileUri != null) {
				_generationResults = this._generator.generateFromFiles(this._modelFileUri, _templateFileUri, this._configFileUri, this._outputFolderUri, "");
			}
			else {
				throw new GeneratorException ("Model, template and config should all be specified as either content or file(URI) in the feature.");
			}
			
			if (_generationResults.getStatus().equals(GenerationStatus.ERROR)) {
				// Store the exception message (for later checking).
				this.generatorException = _generationResults.getException();
			}
		}
		catch(GeneratorException exc) {
			this.generatorException = exc;
		}
	}

	@Then("^I expect (\\d+) generation results?$")
	public void iExpectGenerationResults(int expectedNrOfResults) throws Throwable {
		checkForError();
		int actualNrOfResults = this._generationResults.getGenerationOutputs().size();
		// Assume the expected number equals the actual number.
		assertEquals(
				expectedNrOfResults,
				actualNrOfResults,
				String.format("The expected number of results is then the actual (%s : %s)", expectedNrOfResults, actualNrOfResults)
		);	
	}
	
	@Then("^I expect the following error message:$")
	public void iExpectTheFollowingErrorMessage(String errorMessage) throws Throwable {
		assertNotNull(this.generatorException, "There is no exception thrown");
		assertEquals(errorMessage.replace("{{support-file-location}}", this._featureSupportFilesLocation.toURL().toString().replace("file:/", "file:///")), this.generatorException.getMessage());
	}
	
	@Then("^I expect the following console message:$")
	public void iExpectTheFollowingLogMessage(String logMessage) throws Throwable {
		String logOutput = this.baos.toString();
		assertTrue(logOutput.contains(logMessage), String.format("Log entry containing %s is not found", logMessage));
	}
	
	@Then("^an output named \"(.*)\" with contents equal to file: \"(.*)\"$")
	public void anOutputNamed(String outputName, String expectedOutputFileUri) throws Throwable {
		checkForError();
		//Open the expected output file and read to string
		FileInputStream fis = new FileInputStream(Paths.get(resolveSupportFile(expectedOutputFileUri)).toFile());
		BOMInputStream bomInputStream = BOMInputStream.builder().setInputStream(fis).get();		
		String expectedResultContent = IOUtils.toString(bomInputStream, bomInputStream.getBOMCharsetName());
		this.compareActualAndExpectedOutput(outputName, expectedResultContent);
	}
	
	@Then("^an output named \"(.*)\" with content:$")
	public void andAnOutputNamedWithContents(String outputName, String expectedResultContent) throws Throwable {
		checkForError();
		this.compareActualAndExpectedOutput(outputName, expectedResultContent);
	}
	
	private void compareActualAndExpectedOutput(String outputName, String expectedResultContent) throws Throwable {
		Boolean outputFound = false;
		for(GenerationOutput generationResult : this._generationResults.getGenerationOutputs()) {
			if (generationResult.getOutputFileLocation() != null && uriEncode(outputName).equals(generationResult.getOutputFileLocation())) {
				outputFound = true;
				assertEquals(
						expectedResultContent,
						generationResult.getOutputFileContent(),
						"The expected and actual result content is different"
				);
				//Break out of for loop when result is found
				break;
			}
		}
		//Assume the output with given name was found
		assertTrue(
			outputFound,
			"The expected result with name " + outputName + " was not found in the actual results"				
		);		
	}
	
	private URI resolveSupportFile(String relativeFileLocation) {
		return this._featureSupportFilesLocation.resolve(relativeFileLocation);
	}
	
	private String uriEncode(String uriPart) {
		return new URIBuilder().setPath(uriPart).toString();
	}
	
	// if an exception was thrown this method is invoked, throw the exception
	private void checkForError() throws Throwable {
		if (this.generatorException != null) 
			throw this.generatorException;
	
	}
}
