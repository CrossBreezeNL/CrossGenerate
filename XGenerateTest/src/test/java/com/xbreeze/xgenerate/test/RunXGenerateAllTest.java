package com.xbreeze.xgenerate.test;

import org.junit.runner.RunWith;
import org.testng.annotations.Test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;

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
		plugin = {"pretty", "html:target/cucumber"}
)
@Test
public class RunXGenerateAllTest extends AbstractTestNGCucumberTests { }
