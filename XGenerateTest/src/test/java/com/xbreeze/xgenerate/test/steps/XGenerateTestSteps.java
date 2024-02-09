package com.xbreeze.xgenerate.test.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.xbreeze.xgenerate.config.app.AppConfig;
import com.xbreeze.xgenerate.config.app.XGenAppConfig;
import com.xbreeze.xgenerate.generator.XGenerateStarter;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class XGenerateTestSteps {
	
	private String modelContent;
	private String templateContent;
	private String configContent;
	private String appConfigContent;
	private LinkedList<String> commandLineArgs;
	private String templateName;	
	private URI logFolderName;
	private URI outputFolderName;
	private String processOutput;
	private int actualExitCode;
	
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
	
	@And("^the log destination directory is empty.$")
	public void theLogDestinationDirectoryIsEmpty() throws Throwable {
		emptyFolder(this.logFolderName);
	}
	
	@And("^the directory \\\"(.*)\\\" is empty.$")
	public void theDirectoryIsEmpty(String folderPath) throws Throwable {
		emptyFolder(pathToURI(folderPath));
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
	
	@And("^the following additional comma separated commandline arguments:$")
	public void theFollowingCommandLineArguments(String commandLineArgs) throws Throwable {
		//Strip single and double quotes
		//Split string on comma
		String[] cmdLineArgs = commandLineArgs.replace("\"", "").replaceAll("'", "").split(",");
		
		boolean setLogFilePathOnNextArgument = false;
		
		for (int i = 0; i < cmdLineArgs.length;i++) {
			String cmdLineArgument = cmdLineArgs[i].trim();
			this.commandLineArgs.add(cmdLineArgument);
			
			//If the boolean setLogFilePathOnNextArgument has been set to true in the previous iteration,
			//This parameter should contain the log file path. Store it in the logFolderName attribute.
			//Also change the boolean to false to avoid rewriting this attribute.
			if(setLogFilePathOnNextArgument == true){
				this.logFolderName = pathToURI(new File(cmdLineArgument).getParentFile().toString());
				setLogFilePathOnNextArgument = false;
			}
			
			//If this parameter is filelogdestination or fld, then the next parameter should be the log file path
			//set a boolean, so the next iteration will store this path.
			if(cmdLineArgument.equalsIgnoreCase("-filelogdestination") || cmdLineArgument.equalsIgnoreCase("-fld")) {
				setLogFilePathOnNextArgument = true;
			}
			
		}		
	}
	
	public URI prepareGenerationAndGetAppConfigFileLocation() throws Throwable {
		//Get AppConfig object		
		AppConfig appConfig = XGenAppConfig.fromString(this.appConfigContent).getAppConfig();
		
		URI templateFolder = pathToURI(appConfig.getTemplateFolder());
		URI configFolder = pathToURI(appConfig.getConfigFolder());
		URI modelFolder = pathToURI(appConfig.getModelFolder());
		
		//Save output folder name for later use during output verification
		this.outputFolderName = pathToURI(appConfig.getOutputFolder());
		
		//Remove all files from the output folder
		emptyFolder(this.outputFolderName);
				
		//Save the template, config and model files to appropriate locations found in config
		String modelFileName = this.writeToFile(modelFolder, "model_", ".xml", this.modelContent);
		String templateFileName = this.writeToFile(templateFolder, this.templateName, "", this.templateContent);
		String configFileName = this.writeToFile(configFolder, "config_", ".xml", configContent);
		
		//Save config file to output folder
		String appConfigFileName = this.writeToFile(outputFolderName, "appConfig_", ".xml", this.appConfigContent);		
		URI fullAppConfigFileURI = outputFolderName.resolve(appConfigFileName);
		
		//Add a commandline argument for the MTC combination
		this.commandLineArgs.add("-mtc");
		this.commandLineArgs.add(modelFileName + "::" + templateFileName + "::" + configFileName);
		//Add app config file to commandline arguments
		this.commandLineArgs.add("-config");
		this.commandLineArgs.add(fullAppConfigFileURI.toString().replace("file:/", ""));
		
		return fullAppConfigFileURI;
	}
	
	@When("^I run the generator$")
	public void iRunTheGeneratorAndCheckExitCode() throws Throwable {
		// Add the first part of the command (in reverse order is java is the first argument).
		Path xgenTargetPath = Paths.get(XGenerateStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		PathMatcher xgenJarMatcher = xgenTargetPath.getFileSystem().getPathMatcher("regex:.*XGenerate-[0-9\\.]+-jar-with-dependencies.jar");
		Optional<Path> optionalXgenJarPath = Files.walk(xgenTargetPath, 1).filter(xgenJarMatcher::matches).findFirst();
		// Throw an exception if the executable jar isn't found.
		if (optionalXgenJarPath.isEmpty())
			throw new Exception("The XGenerate jar with dependencies couldn't be found, make sure the jar is build!");
		this.commandLineArgs.addFirst(xgenTargetPath.resolve(optionalXgenJarPath.get()).toString());
		this.commandLineArgs.addFirst("-jar");
		this.commandLineArgs.addFirst("java");
		
		// Prepare generation.
		URI fullAppConfigFileURI = prepareGenerationAndGetAppConfigFileLocation();
		
		// Print the command array.
		String[] cmdArray = commandLineArgs.toArray(new String[0]);
		System.out.println(String.format("Command array: %s", Arrays.toString(cmdArray)));
		
		// Built and start the process.
		ProcessBuilder pb = new ProcessBuilder().command(cmdArray);
		Process xgProcess = pb.start();

		// Store the output of the process.
		this.processOutput = new String(xgProcess.getInputStream().readAllBytes());
		this.processOutput += new String(xgProcess.getErrorStream().readAllBytes());
		
		// Wait for the process to finish.
		actualExitCode = xgProcess.waitFor();
		
		System.out.println("CrossGenerate process output:");
		System.out.println("==================================================");
		System.out.println(this.processOutput);
		System.out.println("==================================================");
		
		//Remove app config file		
		Files.delete(Paths.get(fullAppConfigFileURI));
	}
	
//	@When("^I run the generator$")
//	public void iRunTheGenerator() throws Throwable {	
//		
//		//Create a arraybuffer and capturedConsolePrintStreams for out and err printstreams
//		baos = new ByteArrayOutputStream();
//		PrintStream stdOut = System.out;
//		PrintStream stdErr = System.err;
//		CapturedConsolePrintStream newOut = new CapturedConsolePrintStream(baos, stdOut);
//		CapturedConsolePrintStream newErr = new CapturedConsolePrintStream(baos, stdErr);
//		
//		//Redirect std and err out to the new captured streams
//		System.setOut(newOut);
//		System.setErr(newErr);
//		
//		// Prepare generation.
//		URI fullAppConfigFileURI = prepareGenerationAndGetAppConfigFileLocation();
//
//		//Invoke generator
//		XGenerateStarter.main(this.commandLineArgs.toArray(new String[0]));
//		
//		//Remove app config file		
//		Files.delete(Paths.get(fullAppConfigFileURI));
//
//		//Restore std and err output streams
//		System.setOut(stdOut);
//		System.setErr(stdErr);
//	}

	@Then("^I expect (\\d+) generation results?$")
	public void iExpectGenerationResults(int expectedNrOfResults) throws Throwable {		
		int actualNrOfResults = new File(this.outputFolderName).listFiles().length;
		// Assume the expected number equals the actual number.
		assertEquals(
				expectedNrOfResults,
				actualNrOfResults,
				String.format("The expected number of results is then the actual (%s : %s)", expectedNrOfResults, actualNrOfResults)
		);	
	}
	
	@Then("^I expect exit code (\\d)$")
	public void iExpectExitCode(int expectedExitCode) throws Throwable {		
		assertEquals(
				expectedExitCode,
				actualExitCode,
				String.format("The actual exit code (%d) differs from the expected exit code (%d)", actualExitCode, expectedExitCode)
		);	
	}
	
	@Then("^no log file$")
	public void andNoLogFile() throws Throwable {
		//
		int nrOfLogFiles = 0;
		
		// If there is a logFolder name set, 
		// count the files in that folder.
		if (this.logFolderName != null) {
			File logDir = new File(this.logFolderName);
			nrOfLogFiles = logDir.listFiles().length;
		}
	
		//Assume no logfile was created
		assertEquals(0, nrOfLogFiles, String.format("The expected number of log files is 0, found %s logfiles", nrOfLogFiles));		
	}
	
	@Then("^a log file containing \"(.*)\" but not containing \"(.*)\"$")
	public void andALogContaining(String textExpected, String textNotExpected) throws Throwable {
		
		int nrOfLogFiles;
		File logDir = null;
		
		try {
			logDir = new File(this.logFolderName);
			nrOfLogFiles = logDir.listFiles().length;
		}
		catch(NullPointerException e){
			nrOfLogFiles = 0;
		}

		//Assume only one logfile was created
		assertEquals(1, nrOfLogFiles, String.format("The expected number of log files is 1, found %s logfiles", nrOfLogFiles));
		
		//Check for expected content
		Boolean found = this.findTextInLog(logDir.listFiles()[0].getAbsolutePath(), textExpected);
		assertTrue(found, String.format("Did not found %s in log file while expected", textExpected));
		
		//Check for unexpected content		
		found = this.findTextInLog(logDir.listFiles()[0].getAbsolutePath(), textNotExpected);
		assertFalse(found, String.format("Found %s in log file while not expected", textNotExpected));
	}
	
	@Then("^a console output containing \"(.*)\" but not containing \"(.*)\"$")
	public void andAConsoleOutputContaining(String textExpected, String textNotExpected) throws Throwable {
		
		//Check for expected content
		Boolean found = this.findTextInString(this.processOutput, textExpected);
		assertTrue(found, String.format("Did not found %s in console output while expected", textExpected));
		
		//Check for unexpected content		
		found = this.findTextInString(this.processOutput, textNotExpected);
		assertFalse(found, String.format("Found %s in console output while not expected", textNotExpected));		
	}	
	
	private String writeToFile(URI location, String filePrefix, String fileSuffix, String fileContents) throws IOException {
		String fileName = filePrefix + String.valueOf(Thread.currentThread().getId()) + fileSuffix;
		URI fileUri = location.resolve(fileName);
		Files.write(Paths.get(fileUri), fileContents.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		return fileName;
	}
	
	private Boolean findTextInLog(String fileName, String textToFind) throws Throwable {	
		//Read file into stringbuffer
		String actualContent = new String(Files.readAllBytes(Paths.get(fileName)));
		return actualContent.contains(textToFind);
	}
	
	private Boolean findTextInString(String content, String textToFind) throws Throwable {
		return content.contains(textToFind);
	}
		
	// Create a URI from a provided string path
	private URI pathToURI(String stringPath) throws Throwable{
		try {
			return new URI("file:///" + stringPath.replace("\\", "/"));
		}
		catch(URISyntaxException e){
			System.out.println("Provided string path could not be parsed into an URI!");
			throw e;
		}
	}
	
	// Clean a folder and handle the exceptions to avoid duplicate code on exception handling.
	private void emptyFolder(URI folderPath) throws Throwable {
		try {
			FileUtils.cleanDirectory(new File(folderPath));
		}
		catch(IOException e){
			System.out.println("Delete of the folder content failed!");
			throw e;
		}
		catch(IllegalArgumentException e){
			System.out.println("Illegal folder path provided!");
			throw e;
		}
	}
}


