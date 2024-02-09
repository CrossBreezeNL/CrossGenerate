package com.xbreeze.xgenerate.test;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;

/**
 * Class to set the options for the Cucumber runner for unit tests.
 * @author Harmen
 *
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value="pretty,json:target/cucumber-unit.json")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value="@Unit")
public class RunXGenerateUnitTest { }
