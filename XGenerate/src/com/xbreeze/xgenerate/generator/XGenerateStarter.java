package com.xbreeze.xgenerate.generator;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.xbreeze.license.LicenseException;
import com.xbreeze.license.LicensedClassLoader;
import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.app.XGenAppConfig;

/**
 * Class for starting up generator using licensed classloader
 * @author Willem
 *
 */
public class XGenerateStarter {
	
	//Set the parent logger in this class by creating a logger named com.xbreeze
	private static final Logger logger = Logger.getLogger("com.xbreeze");
	
	/**
	 * The main for running CrossGenerate from command line.
	 * @param args The arguments.
	 */
	public static void main(String[] args) {
		
		// Check whether the number of arguments is correct.
		if (args.length <= 2 || args.length % 2 != 0) {
			if (args.length > 0) {
				System.err.println("Incorrect number of arguments specified");
			}
			System.out.println("Usage: CrossGenerate -config AppConfigFileLocation [-mtc ModelFileLocation::TemplateFileLocation::GenConfigFileLocation]+ [-debug true] [-loglevelfile fine|info|warning] [-loglevelconsole fine|info|warning] [-logdestination path-to-logfile]");
		}
		
		// If correct amount of arguments, go through the arguments.
		else {
			
			// Set debug mode default.
			boolean debugMode = false;
			
			// Create a list for the model-template-config combinations.
			ArrayList<ModelTemplateConfigCombination> modelTemplateConfigCombinations = new ArrayList<ModelTemplateConfigCombination>();
			// Initialize a XGenAppConfig object to be set using the commands.
			XGenAppConfig appConfig = null;
			
			// Try reading the parameters from the console.
			try {
				
				// Set logging defaults
				Level logLevelFile = Level.INFO;
				Level logLevelConsole = Level.INFO;
				String logDestination = null;
				
				// Loop through the arguments as pairs.
				for (int i=0; i<args.length; i+=2) {
					String key = args[i];
					String value = args[i+1];
					
					// Handle the key-value pair.
					switch (key) {
						// config
						case "-config":
							// If the appConfig is already set, print en error.
							if (appConfig != null)
								throw new GeneratorException("The app config is specified multiple times");
							
							try {
								appConfig = XGenAppConfig.fromFile(Paths.get(value).toUri());
							} catch (ConfigException e) {
								throw new GeneratorException(String.format("Error found in app config: %s", e.getMessage()));
							}
							break;
						// mtc (Model - Template - Config)
						case "-mtc":
						case "-ModelTemplateConfig":
							modelTemplateConfigCombinations.add(ModelTemplateConfigCombination.fromString(value));
							break;
						// debug - d
						case "-d":
						case "-debug":
							debugMode = Boolean.parseBoolean(value);
							break;
						// consoleLogLevel - cll
						case "-cll":
						case "-consoleLogLevel":
							logLevelConsole = getLogLevel(value);
							break;
						// fileLogLevel - fll
						case "-fll":
						case "-fileLogLevel":
							logLevelFile = getLogLevel(value);
							break;
						// fileLogDestination - fld
						case "-fld":
						case "-fileLogDestination":
							logDestination = value;
							break;						
						// unrecognized parameter							
						default:
							throw new GeneratorException(String.format("Unexpected parameter specified: '%s'", key));
					}
				}
				
				// If the appConfig is still null, print an error.
				if (appConfig == null)
					throw new GeneratorException("No app config file location was specified");
				
				// If there is no mtc combination specified, print an error.
				if (modelTemplateConfigCombinations.size() == 0)
					throw new GeneratorException("No Model-Template-Config (mtc) combintation(s) specified");
				
				// Setup the logger
				setLogLevelAndDestination(logLevelFile, logLevelConsole, logDestination, debugMode);
			}
			// When an error occurred during reading of all arguments, print an error in the console.
			catch (GeneratorException e) {
				System.err.println(e.getMessage());
				// Return, so the generator won't start when an error occured uptill here.
				return;
			}
			
			// All parameters are read and logging setup, so we can safely start the generator.
			startGenerator(appConfig, modelTemplateConfigCombinations, debugMode);
		}
	}
	
	/**
	 * Procedure for starting the generator.
	 * @param appConfig The XGenAppConfig.
	 * @param modelTemplateConfigCombinations The ModelTemplateConfig combination(s).
	 * @param debugMode Debug mode indicator.
	 */
	private static void startGenerator(XGenAppConfig appConfig, ArrayList<ModelTemplateConfigCombination> modelTemplateConfigCombinations, boolean debugMode) {
		try {
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
				if (debugMode) {
					logger.warning("Debug mode enabled");
				}
			
				//Load Generator from licenseClassLoader
				LicensedClassLoader lcl = new LicensedClassLoader(GeneratorStub.class.getClassLoader(), appConfig.getLicenseConfig(), debugMode);
				
				// Load generator class from licensed ClassLoader
				Class<?> c = lcl.loadClass("com.xbreeze.xgenerate.generator.Generator");                                      
                // Instantiate new object
				GeneratorStub generator = (GeneratorStub)c.newInstance();
				
				generator.setDebugMode(debugMode);
				// Set the model using the file location.
				generator.setModelFromFile(modelFileLocation);
				// Generate the output using the file locations.
				generator.generateFromFilesAndWriteOutput(templateFileLocation, configFileLocation, outputFileLocation);
			}
			
			logger.info("Generation complete");
		} catch (GeneratorException | LicenseException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			logger.severe("Error occured while generating");
			logger.severe(e.getMessage());
			System.err.println("Error occured while generating, see log for more information");
		}
		finally {
			// Close all existing log handlers
			for(Handler h : logger.getHandlers())
			{
			    h.close();   
			}
		}
	}
	
	/**
	 * Set the log level and destination.
	 * @param consoleLogLevel The console log level.
	 * @param fileLogLevel The file log level.
	 * @param fileLogDestination The file log destination.
	 * @param debugMode Debug mode indicator.
	 * @throws GeneratorException
	 */
	private static void setLogLevelAndDestination(Level consoleLogLevel, Level fileLogLevel, String fileLogDestination, Boolean debugMode) throws GeneratorException {
		// Unlink logger from parent handlers and instantiate a new console handler so loglevel can be specified for the console hander
		logger.setUseParentHandlers(false);
		
		// Set logger log level to finest necessary (fine = 500, info=800, etc)
		logger.setLevel((fileLogLevel.intValue() < consoleLogLevel.intValue()) ? fileLogLevel : consoleLogLevel);
		
		// Create a log formatter.
		GeneratorLogFormatter logFormatter;
		// When running in debug mode, output with debug log formatting.
		if (debugMode) {
			logFormatter = new GeneratorDebugLogFormatter();
		}
		// Otherwise use non-debug log formatting
		else {
			logFormatter = new GeneratorLogFormatter();
		}
		
		// Create a console handler for console logging.
		ConsoleHandler consoleHandler = new ConsoleHandler();
		// Set the console handler log level.
		consoleHandler.setLevel(consoleLogLevel);
		// Set the formatter for the console log.
		consoleHandler.setFormatter(logFormatter);
		// Add the console log handler.
		logger.addHandler(consoleHandler);
		
		// If logDestination is not null, assign a file handler to the logger
		if (fileLogDestination != null && fileLogDestination.length() > 0) {
			try {
				// Create the file handler for the file logger.
				FileHandler fh = new FileHandler(fileLogDestination.toString(), true);
				// Set the level of the file log.
				fh.setLevel(fileLogLevel);
				// Set the formatter on the file log.
				fh.setFormatter(logFormatter);
				// Add the file log handler.
				logger.addHandler(fh);
			} catch (SecurityException | IOException e) {
				throw new GeneratorException(String.format("Error setting log destination: %s", e.getMessage()));
			}
		}
	}
	
	/**
	 * Get the log level using the textual representation from the config.
	 * @param level The log level
	 * @return The Level constant.
	 * @throws GeneratorException
	 */
	private static Level getLogLevel(String level) throws GeneratorException {
		try {
			return Level.parse(level.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new GeneratorException(String.format("Unknown LogLevel specified: '%s'", level), e);
		}		
	}
}
