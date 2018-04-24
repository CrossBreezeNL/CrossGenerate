package com.xbreeze.xgenerate.test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * Class to set the options for the Cucumber runner for debug mode.
 * @author Harmen
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(
		// Find the feature files in the root 'features' folder.
		features = "src/test/resources/features",
		glue = "com.xbreeze.xgenerate.test.steps",
		plugin = {"pretty", "html:target/cucumber"},
		tags = {"@Debug"}
)
public class RunXGenerateDebugTest { }
