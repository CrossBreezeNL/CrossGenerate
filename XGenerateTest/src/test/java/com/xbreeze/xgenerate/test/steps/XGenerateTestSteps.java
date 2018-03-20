package com.xbreeze.xgenerate.test.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.net.URI;

import javax.xml.parsers.DocumentBuilderFactory;

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
	private final URI _outputFileUri = URI.create("file:///C:/CrossGenerate/Output"); 
	
	XGenConfig _xGenConfig;
	RawTemplate _rawTemplate;
	Generator _generator;
	GenerationResults _generationResults;
	
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

	@And("^the following template named (.*):$")
	public void theFollowingTemplateNamedStaging_Tables_system_nameSql(String templateName, String templateContent) throws Throwable {
		// Create the raw template.
		this._rawTemplate = new RawTemplate(templateName, null, templateContent);
	}

	@And("^the following config:$")
	public void theFollowingConfig(String configContent) throws Throwable {
		this._xGenConfig = XGenConfig.fromString(configContent);
	}

	@When("^I run the generator$")
	public void iRunTheGenerator() throws Throwable {
		//Preprocess model first, then invoke generator
		try {
			ModelPreprocessor.preprocessModel(_generator.getModel(), this._xGenConfig.getModelConfig());
		} catch (ModelPreprocessorException e) {
			throw new GeneratorException(e);
		}
		_generationResults = this._generator.generate(this._rawTemplate, this._xGenConfig, _outputFileUri);
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
	
	@Then("^an output named (.*) with content:$")
	public void andAnOutputNamedWithContents(String outputName, String expectedResultContent) throws Throwable {
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
