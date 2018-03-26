package com.xbreeze.xgenerate.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Class to set the options for the Cucumber runner.
 * @author Harmen
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		// Find the feature files in the root 'features' folder.
		features = "src/test/resources/features",
		glue = "com.xbreeze.xgenerate.test.steps",
		plugin = {"pretty", "json:target/cucumber-integration.json"},
		tags = {"@Integration"}
)
public class RunXGenerateIntegrationTest { }
