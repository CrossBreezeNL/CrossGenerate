package com.xbreeze.xgenerate.generator;

import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.xbreeze.license.LicenseError;
import com.xbreeze.license.LicensedClassLoader;
import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.app.XGenAppConfig;

/**
 * Class for starting up generator using licensed classloader
 * @author Willem
 *
 */
public class XGenerateStarter {
	
	private static final Logger logger = Logger.getLogger(GeneratorStub.class.getName());
	
	/**
	 * The main for running CrossGenerate from command line.
	 * Usage: CrossGenerate ModelFileLocation ConfigFileLocation TemplateFileLocation
	 * @param args The arguments.
	 */
	
	public static void main(String[] args) {
		if (args.length <= 2 || args.length % 2 != 0) {
			if (args.length > 0) {
				System.err.println("Incorrect number of arguments specified");
			}
			System.out.println("Usage: CrossGenerate -config AppConfigFileLocation [-mtc ModelFileLocation::TemplateFileLocation::GenConfigFileLocation]+ [-debug true]");
		} else {
			
			try {

				// Create a list for the model-template-config combinations.
				ArrayList<ModelTemplateConfigCombination> modelTemplateConfigCombinations = new ArrayList<ModelTemplateConfigCombination>();
				// Initialize a XGenAppConfig object to be set using the commands.
				XGenAppConfig appConfig = null;
				boolean debugMode = false;
				// Loop through the arguments as pairs.
				for (int i=0; i< args.length; i+=2) {
					String key = args[i];
					String value = args[i+1];
					
					// Handle the key-value pair.
					switch (key) {
						// config
						case "-config":
							// If the appConfig is already set, print en error.
							if (appConfig != null)
								System.err.println("The app config is specified multiple times");
							
							try {
								appConfig = XGenAppConfig.fromFile(Paths.get(value).toUri());
							} catch (ConfigException e) {
								System.err.println(String.format("Error found in app config: %s", e.getMessage()));
							}
							break;
						// mtc (Model - Template - Config)
						case "-mtc":
							modelTemplateConfigCombinations.add(ModelTemplateConfigCombination.fromString(value));
							break;
						// debug
						case "-debug":
							debugMode = Boolean.parseBoolean(value);
							break;
						// unrecognized parameter
						default:
							System.err.println(String.format("Unexpected parameter specified: '%s'", key));
					}
				}
				
				// If the appConfig is still null, print an error.
				if (appConfig == null)
					throw new GeneratorException("No app config file location was specified");
				
				// If there is not mtc combination specified, print an error.
				if (modelTemplateConfigCombinations.size() == 0)
					System.err.println("No model-template-cofig (mtc) combintations specified");
				
				// Setup the logger.
				// TODO Properly handle logging of info message to log when debug is on.
				//logger.setLevel(Level.WARNING);
				if (debugMode) {
					System.out.println("Debug mode enabled");
					logger.setLevel(Level.INFO);
				}

				// Loop through the model-template-config combinations and perform the generation.
				for (ModelTemplateConfigCombination modelTemplateConfigCombination : modelTemplateConfigCombinations) {
					// Create the full paths to the needed files.
					URI modelFileLocation = Paths.get(appConfig.getAppConfig().getModelFolder(), modelTemplateConfigCombination.getModelFileLocation()).toUri();
					URI templateFileLocation = Paths.get(appConfig.getAppConfig().getTemplateFolder(), modelTemplateConfigCombination.getTemplateFileLocation()).toUri();
					URI configFileLocation = Paths.get(appConfig.getAppConfig().getConfigFolder(), modelTemplateConfigCombination.getConfigFileLocation()).toUri();
					URI outputFileLocation = Paths.get(appConfig.getAppConfig().getOutputFolder()).toUri();
					
					logger.info("Starting CrossGenerate with the following arguments:");
					logger.info(String.format(" - ModelFileLocation: %s", modelFileLocation));
					logger.info(String.format(" - TemplateFileLocation: %s", templateFileLocation));
					logger.info(String.format(" - ConfigFileLocation: %s", configFileLocation));
					logger.info(String.format(" - OutputFileLocation: %s", outputFileLocation));
				
					//Load Generator from licenseClassLoader
					LicensedClassLoader lcl = new LicensedClassLoader(GeneratorStub.class.getClassLoader(), appConfig.getLicenseConfig(), debugMode);
					
					//Load generator class from licensed classloader
					Class c = lcl.loadClass("com.xbreeze.xgenerate.generator.Generator");                                      
                    //instantiate new object
					GeneratorStub generator = (GeneratorStub)c.newInstance();
					
					generator.setDebugMode(debugMode);
					// Set the model using the file location.
					generator.setModelFromFile(modelFileLocation);
					// Generate the output using the file locations.
					generator.generateFromFilesAndWriteOutput(templateFileLocation, configFileLocation, outputFileLocation);
				}
				
				logger.info("Generation complete");
			} catch (GeneratorException | UnhandledException | LicenseError | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
				logger.severe("Error occured while generating");
				logger.severe(e.getLocalizedMessage());
				System.err.println("Error occured while generating, see log for more information");
			}
		}
	}
}
