package com.xbreeze.xgenerate.generator;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

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
	 * Usage: CrossGenerate ModelFileLocation ConfigFileLocation TemplateFileLocation
	 * @param args The arguments.
	 */
	
	public static void main(String[] args) {
		if (args.length <= 2 || args.length % 2 != 0) {
			if (args.length > 0) {
				System.err.println("Incorrect number of arguments specified");
			}
			System.out.println("Usage: CrossGenerate -config AppConfigFileLocation [-mtc ModelFileLocation::TemplateFileLocation::GenConfigFileLocation]+ [-debug true] [-loglevelfile fine|info|warning] [-loglevelconsole fine|info|warning] [-logdestination path-to-logfile]");
		} else {
			
			try {

				// Create a list for the model-template-config combinations.
				ArrayList<ModelTemplateConfigCombination> modelTemplateConfigCombinations = new ArrayList<ModelTemplateConfigCombination>();
				// Initialize a XGenAppConfig object to be set using the commands.
				XGenAppConfig appConfig = null;
				boolean debugMode = false;
				
				//Set logging defaults
				Level logLevelFile = Level.INFO;
				Level logLevelConsole = Level.INFO;
				String logDestination = null;
				
				//Unlink logger from parent handlers and instantiate a new console handler so loglevel can be specified for the console hander
				logger.setUseParentHandlers(false);
				logger.addHandler(new ConsoleHandler());
				
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
								throw new GeneratorException("The app config is specified multiple times");
							
							try {
								appConfig = XGenAppConfig.fromFile(Paths.get(value).toUri());
							} catch (ConfigException e) {
								throw new GeneratorException(String.format("Error found in app config: %s", e.getMessage()));
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
						// loglevel file
						case "-loglevelfile":
							logLevelFile = getLogLevel(value);
							break;
						case "-loglevelconsole":
							logLevelConsole = getLogLevel(value);
							break;
						//log destination
						case "-logdestination":
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
				
				// If there is not mtc combination specified, print an error.
				if (modelTemplateConfigCombinations.size() == 0)
					System.err.println("No model-template-cofig (mtc) combintations specified");
				
				// Setup the logger
				setLogLevelAndDestination(logLevelFile, logLevelConsole, logDestination, debugMode);
				
				if (debugMode) {
					logger.warning("Debug mode enabled");
					
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
				//Close all existing loghandlers
				for(Handler h:logger.getHandlers())
				{
				    h.close();   
				}
			}
		}		
	}
	
	private static void setLogLevelAndDestination(Level logLevelFile, Level logLevelConsole, String logDestination, Boolean debugMode) throws GeneratorException {
		//Set loggers loglevel to finest necessary (fine = 500, info=800, etc)
		if (logLevelFile.intValue() < logLevelConsole.intValue()) {
			logger.setLevel(logLevelFile);
		}
		else {
			logger.setLevel(logLevelConsole);
		}
		
		//Set level for existing handler(s) to console loglevel
		for (Handler h: logger.getHandlers()) {
			h.setLevel(logLevelConsole);
		}
		
		//if logDestination is not null, assign a file handler to the logger
		if (logDestination != null) {
			try {
				FileHandler fh = new FileHandler(logDestination.toString(), true);
				fh.setLevel(logLevelFile);
				logger.addHandler(fh);
			} catch (SecurityException | IOException e) {
				throw new GeneratorException(String.format("Error setting log destination: %s", e.getMessage()));
			}			
		}
		
		GeneratorLogFormatter logFormatter;
		
		//When running in debug mode, output with debug log formatting, otherwise use non-debug log formatting
		if (debugMode) {
			logFormatter = new GeneratorDebugLogFormatter();
		}
		else {
			logFormatter = new GeneratorLogFormatter();
		}
		
		//Apply log formatter on all loghandlers
		for (Handler h: logger.getHandlers()) {
			h.setFormatter(logFormatter);
		}
	}
	
	private static Level getLogLevel(String level) throws GeneratorException {
		switch (level.toLowerCase()) {
		case "warning":
			return Level.WARNING;			
		case "fine":
			return Level.FINE;			
		case "info":
			return Level.INFO;			
		default:
			throw new GeneratorException(String.format("Unknown loglevel specified: '%s'", level));
		}		
	}
}
