package com.xbreeze.xgenerate.test.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
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
import com.xbreeze.xgenerate.generator.GenerationResult;
import com.xbreeze.xgenerate.generator.GenerationResult.GenerationStatus;
import com.xbreeze.xgenerate.generator.GenerationResults;
import com.xbreeze.xgenerate.generator.Generator;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.test.util.CapturedConsolePrintStream;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class XGenerateLibTestSteps {
	private final URI _outputFolderUri = URI.create("file:///C:/CrossGenerate/Output/");
	// Location of the feature support files.
	private URI _featureSupportFilesLocation;
	
	
	XGenConfig _xGenConfig;
	RawTemplate _rawTemplate;
	Generator _generator;
	GenerationResults _generationResults;
	URI _templateFileUri;
	URI _configFileUri;
	Exception generatorException;
	private ByteArrayOutputStream baos;
	private PrintStream stdOut;
	private PrintStream stdErr;
	
	@Before
	public void beforeScenario(Scenario scenario) {
		
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
		
		// Set the feature support file location using the scenario location.
		// The support file location is the same as the feature file location, but without the .feature extention and the directory 'features' is replaced with 'feature-support-files'.
		String derivedFeatureSupportFileLocation = scenario.getUri().replaceFirst("features", "feature-support-files").replace(".feature", "");
		logger.info(String.format("The feature-support-file location will be set to '%s'.", derivedFeatureSupportFileLocation));
		_featureSupportFilesLocation = Paths.get(derivedFeatureSupportFileLocation).toUri();
		
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
		this._generator.setModel(new Model(null, modelContent));
	}
	
	@Given("^I have the following model file: \"(.*)\"$")
	public void iHaveTheFollowingModelFile(String modelFileLocation) throws Throwable {
		this._generator.setModelFromFile(resolveSupportFile(modelFileLocation));
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
			this._xGenConfig = XGenConfig.fromString(configContent, _featureSupportFilesLocation);
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
			if (this._rawTemplate != null && this._xGenConfig != null) {
				_generationResults = this._generator.generate(this._rawTemplate, this._xGenConfig, this._outputFolderUri, "");
			}
			else if (this._templateFileUri != null && this._configFileUri != null) {
				_generationResults = this._generator.generateFromFiles(this._templateFileUri, this._configFileUri, this._outputFolderUri, "");
			}
			else {
				throw new GeneratorException ("Template and config should both be specified as either content or file(URI) in the feature.");
			}
		
			// If there was an error during generation, store the exception.
			for (GenerationResult generationResult : _generationResults.getGenerationResults()) {
				if (generationResult.getStatus().equals(GenerationStatus.ERROR)) {
					//throw generationResult.getException();
					this.generatorException = generationResult.getException();
				}
			}
		}
		catch(GeneratorException exc) {
			this.generatorException = exc;
		}
	}

	@Then("^I expect (\\d+) generation results?$")
	public void iExpectGenerationResults(int expectedNrOfResults) throws Throwable {
		checkForError();
		int actualNrOfResults = this._generationResults.getGenerationResults().size();
		// Assume the expected number equals the actual number.
		assertEquals(
				String.format("The expected number of results is then the actual (%s : %s)", expectedNrOfResults, actualNrOfResults),
				expectedNrOfResults,
				actualNrOfResults
		);	
	}
	
	@Then("^I expect the following error message:$")
	public void iExpectTheFollowingErrorMessage(String errorMessage) throws Throwable {
		assertNotNull("There is no exception thrown", this.generatorException);
		assertEquals(errorMessage, this.generatorException.getMessage());
		
		
	}
	
	@Then("^I expect the following console message:$")
	public void iExpectTheFollowingLogMessage(String logMessage) throws Throwable {
		String logOutput = this.baos.toString();
		assertTrue(String.format("Log entry containing %s is not found", logMessage), logOutput.contains(logMessage));
	}
	
	@Then("^an output named \"(.*)\" with contents equal to file: \"(.*)\"$")
	public void anOutputNamed(String outputName, String expectedOutputFileUri) throws Throwable {
		checkForError();
		//Open the expected output file and read to string
		FileInputStream fis = new FileInputStream(Paths.get(resolveSupportFile(expectedOutputFileUri)).toFile());
		BOMInputStream bomInputStream = new BOMInputStream(fis);		
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
		for(GenerationResult generationResult : this._generationResults.getGenerationResults()) {
			if (generationResult.getOutputFileLocation() != null && uriEncode(outputName).equals(generationResult.getOutputFileLocation())) {
				outputFound = true;
				assertEquals(
						"The expected and actual result content is different",
						expectedResultContent,
						generationResult.getOutputFileContent()
				);
				//Break out of for loop when result is found
				break;
			}
		}
		//Assume the output with given name was found
		assertTrue(
			"The expected result with name " + outputName + " was not found in the actual results",				
			outputFound
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
