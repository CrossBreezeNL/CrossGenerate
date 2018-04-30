package com.xbreeze.xgenerate.test.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.http.client.utils.URIBuilder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.generator.GenerationResult;
import com.xbreeze.xgenerate.generator.GenerationResult.GenerationStatus;
import com.xbreeze.xgenerate.generator.GenerationResults;
import com.xbreeze.xgenerate.generator.Generator;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.template.RawTemplate;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class XGenerateLibTestSteps {
	private final URI _outputFolderUri = URI.create("file:///C:/CrossGenerate/Output/");
	private final URI _featureSupportFilesLocation = URI.create("file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateLibTest/src/test/resources/feature-support-files/");
	
	XGenConfig _xGenConfig;
	RawTemplate _rawTemplate;
	Generator _generator;
	GenerationResults _generationResults;
	URI _templateFileUri;
	URI _configFileUri;
	
	@Before
	public void beforeScenario(Scenario scenario) {
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
			System.setProperty("java.util.logging.SimpleFormatter.format", "@%2$s ->%n [%1$tF %1$tT] [%4$-7s] %5$s %n");
		}
		
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
				return record.getLevel().intValue() < Level.WARNING.intValue();
			}
		});
		logger.addHandler(outputConsoleHandler);
		
		// Initialize the generator.
		this._generator = new Generator();
		this._generator.setTestMode(true);
		
		// Enable debug mode if there is a tag @Debug.
		if (scenario.getSourceTagNames().contains("@Debug")) {
			this._generator.setDebugMode(true);
		}
	}

	@Given("^I have the following model:$")
	public void iHaveTheFollowingModel(String modelContent) throws Throwable {
		// Create the model object.
		Document modelDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(modelContent)));		
		this._generator.setModel(new Model(null, modelDocument));
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
		this._xGenConfig = XGenConfig.fromString(configContent);
	}

	@And("^the following config file: \"(.*)\"$")
	public void theFollowingConfigFile(String configFileLocation) throws Throwable {
		// Create the raw template.
		this._configFileUri = resolveSupportFile(configFileLocation);
	}	

	@When("^I run the generator$")
	public void iRunTheGenerator() throws Throwable {	
		//check if generator needs to be invoked with files or with template and config string
		if (this._rawTemplate != null && this._xGenConfig != null) {
			_generationResults = this._generator.generate(this._rawTemplate, this._xGenConfig, _outputFolderUri);
		}
		else if (this._templateFileUri != null && this._configFileUri != null) {
			_generationResults = this._generator.generateFromFiles(this._templateFileUri, this._configFileUri, this._outputFolderUri);
		}
		else {
			throw new GeneratorException ("Template and config should both be specified as either content or file(URI) in the feature.");
		}
		
		// If there was an error during generation, throw the exception.
		for (GenerationResult generationResult : _generationResults.getGenerationResults()) {
			if (generationResult.getStatus().equals(GenerationStatus.ERROR)) {
				throw generationResult.getException();
			}
		}
	}

	@Then("^I expect (\\d+) generation results?$")
	public void iExpectGenerationResults(int expectedNrOfResults) throws Throwable {
		int actualNrOfResults = this._generationResults.getGenerationResults().size();
		// Assume the expected number equals the actual number.
		assertEquals(
				String.format("The expected number of results is then the actual (%s : %s)", expectedNrOfResults, actualNrOfResults),
				expectedNrOfResults,
				actualNrOfResults
		);	
	}
	
	@Then("^an output named \"(.*)\" with contents equal to file: \"(.*)\"$")
	public void anOutputNamed(String outputName, String expectedOutputFileUri) throws Throwable {
		//Open the expected output file and read to string
		FileInputStream fis = new FileInputStream(Paths.get(resolveSupportFile(expectedOutputFileUri)).toFile());
		BOMInputStream bomInputStream = new BOMInputStream(fis);		
		String expectedResultContent = IOUtils.toString(bomInputStream, bomInputStream.getBOMCharsetName());
		this.compareActualAndExpectedOutput(outputName, expectedResultContent);
	}
	
	@Then("^an output named \"(.*)\" with content:$")
	public void andAnOutputNamedWithContents(String outputName, String expectedResultContent) throws Throwable {
		this.compareActualAndExpectedOutput(outputName, expectedResultContent);
	}
	
	private void compareActualAndExpectedOutput(String outputName, String expectedResultContent) throws Throwable {
		Boolean outputFound = false;
		for(GenerationResult generationResult : this._generationResults.getGenerationResults()) {
			if (generationResult.getOutputFileLocation() != null && _outputFolderUri.resolve(uriEncode(outputName)).equals(URI.create(generationResult.getOutputFileLocation()))) {
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
}
