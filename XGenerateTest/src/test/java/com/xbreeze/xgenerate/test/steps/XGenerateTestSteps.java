package com.xbreeze.xgenerate.test.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.generator.GenerationResult;
import com.xbreeze.xgenerate.generator.GenerationResults;
import com.xbreeze.xgenerate.generator.Generator;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.model.ModelPreprocessor;
import com.xbreeze.xgenerate.model.ModelPreprocessorException;
import com.xbreeze.xgenerate.template.RawTemplate;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class XGenerateTestSteps {
	private final URI _outputFolderUri = URI.create("file:///C:/CrossGenerate/Output"); 
	
	XGenConfig _xGenConfig;
	RawTemplate _rawTemplate;
	Generator _generator;
	GenerationResults _generationResults;
	URI _templateFileUri;
	URI _configFileUri;
	
	@Before
	public void beforeScenario() {
		// Initialize the generator.
		this._generator = new Generator();
		this._generator.setTestMode(true);
	}

	@Given("^I have the following model:$")
	public void iHaveTheFollowingModel(String modelContent) throws Throwable {
		// Create the model object.
		Document modelDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(modelContent)));		
		this._generator.setModel(new Model(null, modelDocument));
	}
	@Given("^I have the following model file:$")
	public void iHaveTheFollowingModelFile(URI modelFileURI) throws Throwable {
		this._generator.setModelFromFile(modelFileURI);
	}

	@And("^the following template named (.*):$")
	public void theFollowingTemplateNamed(String templateName, String templateContent) throws Throwable {
		// Create the raw template.
		this._rawTemplate = new RawTemplate(templateName, null, templateContent);
	}

	@And("^the following template file:$")
	public void theFollowingTemplateFile(URI templateFileURI) throws Throwable {
		// Create the raw template.
		this._templateFileUri = templateFileURI;
	}
	
	@And("^the following config:$")
	public void theFollowingConfig(String configContent) throws Throwable {
		this._xGenConfig = XGenConfig.fromString(configContent);
	}

	@And("^the following config file:$")
	public void theFollowingConfigFile(URI configFileURI) throws Throwable {
		// Create the raw template.
		this._configFileUri = configFileURI;
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
	}

	@Then("^I expect (\\d+) generation result\\(s\\)$")
	public void iExpectGenerationResults(int expectedNrOfResults) throws Throwable {
		int actualNrOfResults = this._generationResults.getGenerationResults().size();
		// Assume the expected number equals the actual number.
		assertEquals(
				String.format("The expected number of results is then the actual (%s : %s)", expectedNrOfResults, actualNrOfResults),
				expectedNrOfResults,
				actualNrOfResults
		);	
	}
	
	@Then("^an output named (.*) with contents equal to file:$") 
	public void anOutputNamed(String outputName, URI expectedOutputFileUri) throws Throwable {
		//Open the expected output file and read to string
		FileInputStream fis = new FileInputStream(Paths.get(expectedOutputFileUri).toFile());
		BOMInputStream bomInputStream = new BOMInputStream(fis);		
		String expectedResultContent = IOUtils.toString(bomInputStream, bomInputStream.getBOMCharsetName());
		this.compareActualAndExpectedOutput(outputName, expectedResultContent);
	}
	
	@Then("^an output named (.*) with content:$")
	public void andAnOutputNamedWithContents(String outputName, String expectedResultContent) throws Throwable {
		this.compareActualAndExpectedOutput(outputName, expectedResultContent);
	}
	
	private void compareActualAndExpectedOutput(String outputName, String expectedResultContent) throws Throwable {
		Boolean outputFound = false;
		for(GenerationResult generationResult:this._generationResults.getGenerationResults()) {
			if (generationResult.getOutputFileLocation().equalsIgnoreCase(outputName)) {
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
}
