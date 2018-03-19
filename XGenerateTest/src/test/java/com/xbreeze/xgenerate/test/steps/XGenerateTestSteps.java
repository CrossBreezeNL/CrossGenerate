package com.xbreeze.xgenerate.test.steps;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.net.URI;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.generator.GenerationResults;
import com.xbreeze.xgenerate.generator.Generator;
import com.xbreeze.xgenerate.model.Model;
import com.xbreeze.xgenerate.template.RawTemplate;

import cucumber.api.PendingException;
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
		_generationResults = this._generator.generate(this._rawTemplate, this._xGenConfig, _outputFileUri);
	}

	@Then("^I expect (\\d+) generation result with the following content:$")
	public void iExpectGenerationResultWithTheFollowingContent(int expectedNrOfResults, String expectedResultsContent) throws Throwable {
		int actualNrOfResults = this._generationResults.getGenerationResults().size();
		// Assume the expected number equals the actual number.
		assertEquals(
				String.format("The expected number of results is then the actual (%s : %s)", expectedNrOfResults, actualNrOfResults),
				expectedNrOfResults,
				actualNrOfResults
		);
		
		if (actualNrOfResults == 1) {
			assertEquals(
					"The expected and actual result content is different",
					expectedResultsContent,
					this._generationResults.getGenerationResults().get(0).getOutputFileContent()
			);
		} else {
			throw new PendingException("Comparing multiple results haven't been build yet.");
		}
	}
}
