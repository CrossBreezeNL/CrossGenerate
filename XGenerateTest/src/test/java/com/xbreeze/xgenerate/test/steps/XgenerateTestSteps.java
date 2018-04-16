package com.xbreeze.xgenerate.test.steps;

import static org.junit.Assert.assertEquals;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import org.apache.commons.io.FileUtils;

import com.xbreeze.xgenerate.config.app.AppConfig;
import com.xbreeze.xgenerate.config.app.XGenAppConfig;
import com.xbreeze.xgenerate.generator.XGenerateStarter;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class XgenerateTestSteps {
	private final URI _workingFolderUri = URI.create("file:///C:/CrossGenerate/Test/Config");
	private final URI _logFolderUri = URI.create("file:///C:/CrossGenerate/Test/Log");
	
	private String modelContent;
	private String templateContent;
	private String configContent;
	private String appConfigContent;
	private LinkedList<String> commandLineArgs;
	private String templateName;
	private URI outputFolderName;
	private URI logFolderName;

	@Before
	public void beforeScenario()
	{
		this.commandLineArgs = new LinkedList<String>();
		
	}
	@Given("^I have the following model:$")
	public void iHaveTheFollowingModel(String modelContent) throws Throwable {
		//Save the model to file, create a unique file name in case of parallel processing
		this.modelContent = modelContent;
	}
	
	@And("^the following template named \"(.*)\":$")
	public void theFollowingTemplateNamed(String templateName, String templateContent) throws Throwable {	
		// Create the raw template.
		this.templateContent = templateContent;
		this.templateName = templateName;
	}

	@And("^the following config:$")
	public void theFollowingConfig(String configContent) throws Throwable {
		this.configContent = configContent;
	}

	@And("^the following app config:$")
	public void theFollowingAppConfig(String appConfigContent) throws Throwable {
		this.appConfigContent = appConfigContent;
	}
	
	@And("^the following additional (comma separated) commandline arguments:$")
	public void theFollowingCommandLineArguments(String commandLineArgs) throws Throwable {
		//Strip single and double quotes
		//Split string on comma
		String[] cmdLineArgs = commandLineArgs.replace("\"", "").replaceAll("'", "").split(",");
		for (int i = 0; i < cmdLineArgs.length;i++) {
			this.commandLineArgs.add(cmdLineArgs[i]);
		}		
	}
	
	@When("^I run the generator$")
	public void iRunTheGenerator() throws Throwable {	
		//Save the app config to a file
		String appConfigFileName = this.writeToFile(_workingFolderUri, "AppConfig_", ".xml", this.appConfigContent);
		URI appConfigUri = _workingFolderUri.resolve(appConfigFileName);
		//Read back the file into Config object to obtain template and output folders specified
		AppConfig appConfig = XGenAppConfig.fromFile(appConfigUri).getAppConfig();
		URI templateFolder = new URI("file:///" + appConfig.getTemplateFolder().replace("\\", "/"));
		URI configFolder = new URI("file:///" + appConfig.getConfigFolder().replace("\\", "/"));
		URI modelFolder = new URI("file:///" + appConfig.getModelFolder().replace("\\", "/"));
		
		//Save output folder name for later use during output verification
		this.outputFolderName = new URI("file:///" + appConfig.getOutputFolder().replace("\\", "/"));
		
		//TODO change this when logfolder is supported from app config
		this.logFolderName = this._logFolderUri;
		
		//Remove all files from the output and log folder
		FileUtils.cleanDirectory(new File(this.outputFolderName));
		FileUtils.cleanDirectory(new File(this.logFolderName));
		
		//Save the template, config and model files to appropriate locations found in config
		String modelFileName = this.writeToFile(modelFolder, "model_", ".xml", this.modelContent);
		String templateFileName = this.writeToFile(templateFolder, this.templateName, "", this.templateContent);
		String configFileName = this.writeToFile(configFolder, "config_", ".xml", configContent);
		//Add a commandline argument for the MTC combination
		this.commandLineArgs.add("-mtc");
		this.commandLineArgs.add(modelFileName + "::" + templateFileName + "::" + configFileName);
		//Add app config file to commandline arguments
		this.commandLineArgs.add("-config");
		this.commandLineArgs.add(appConfigUri.toString().replace("file:/", ""));

		//Invoke generator
		XGenerateStarter.main(this.commandLineArgs.toArray(new String[0]));
		
	}

	@Then("^I expect (\\d+) generation results?$")
	public void iExpectGenerationResults(int expectedNrOfResults) throws Throwable {		
		int actualNrOfResults = new File(this.outputFolderName).listFiles().length;
		// Assume the expected number equals the actual number.
		assertEquals(
				String.format("The expected number of results is then the actual (%s : %s)", expectedNrOfResults, actualNrOfResults),
				expectedNrOfResults,
				actualNrOfResults
		);	
	}
	
	@Then("^no log file$")
	public void andNoLogFile() throws Throwable {
		File logDir = new File(this.logFolderName);
		int nrOfLogFiles = logDir.listFiles().length;
		//Assume no logfile was created
		assertEquals(String.format("The expected number of log files is 0, found %s logfiles", nrOfLogFiles), 0, nrOfLogFiles);		
	}
	
	@Then("^a log file with content:$")
	public void andALogNamedWithContents(String expectedLogContent) throws Throwable {
		File logDir = new File(this.logFolderName);
		int nrOfLogFiles = logDir.listFiles().length;
		//Assume only one logfile was created
		assertEquals(String.format("The expected number of log files is 1, found %s logfiles", nrOfLogFiles), 1, nrOfLogFiles);
		
		//Get log file name		
		this.compareActualAndExpectedOutput(logDir.listFiles()[0].getAbsolutePath(), expectedLogContent);
	}
	
	
	private String writeToFile(URI location, String filePrefix, String fileSuffix, String fileContents) throws IOException {
		String fileName = filePrefix + String.valueOf(Thread.currentThread().getId()) + fileSuffix;
		URI fileUri = location.resolve(fileName);
		Files.write(Paths.get(fileUri), fileContents.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		return fileName;
	}
	
	private void compareActualAndExpectedOutput(String fileName, String expectedContent) throws Throwable {	
		//Read file into stringbuffer
		String actualContent = new String(Files.readAllBytes(Paths.get(fileName)));
		assertEquals(
				"The expected and actual log content is different",
				expectedContent,
				actualContent
		);		
	}
		
}


