package com.xbreeze.xgenerate.config.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.UnmarshallValidationEventHandler;

@XmlRootElement(name="XGenAppConfig")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"appConfig"})
public class XGenAppConfig {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(XGenAppConfig.class.getName());
	
	/**
	 * The app configuration.
	 * @see AppConfig
	 */
	@XmlElement(name="App")
	private AppConfig appConfig;
	
	/**
	 * @return the app
	 */
	public AppConfig getAppConfig() {
		return appConfig;
	}

	/**
	 * @param appConfig the app to set
	 */
	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	
	
	/**
	 * Unmarshal a app config from a String.
	 * @param AppConfigFileContent The String object to unmarshal.
	 * @return The unmarshelled XGenAppConfig object.
	 * @throws ConfigException
	 */
	public static XGenAppConfig fromString(String appConfigFileContent) throws ConfigException {
		return fromInputSource(new InputSource(new StringReader(appConfigFileContent)));
	}
	
	/**
	 * Unmarshal a file into a XGenAppConfig object.
	 * @param configFileUri The file to unmarshal.
	 * @return The unmarshalled XGenConfig object.
	 * @throws ConfigException 
	 */
	public static XGenAppConfig fromFile(URI appConfigFileUri) throws ConfigException {
		logger.fine(String.format("Creating XGenAppConfigFile object from '%s'", appConfigFileUri));
		File xGenAppConfigFile = new File(appConfigFileUri);
		XGenAppConfig xGenAppConfig;
		try {
			xGenAppConfig = fromInputSource(new InputSource(new FileReader(xGenAppConfigFile)));
		} catch (ConfigException e) {
			// Catch the config exception here to add the filename in the exception text.
			throw new ConfigException(String.format("%s (%s)", e.getMessage(), appConfigFileUri.toString()), e.getCause());
		} catch (FileNotFoundException e) {
			throw new ConfigException(String.format("Couldn't find the config file (%s)", appConfigFileUri.toString()), e);
		}
		
		// If the config folder isn't set in the app config, we fill it with the folder location of the app config.
		if (xGenAppConfig.getAppConfig().getConfigFolder() == null || xGenAppConfig.getAppConfig().getConfigFolder().length() == 0)
			xGenAppConfig.getAppConfig().setConfigFolder(Paths.get(appConfigFileUri).getParent().toString());
		
		return xGenAppConfig;
	}
	
	/**
	 * Create a XGenAppConfig object using a InputSource.
	 * @param inputSource The InputSource.
	 * @return The XGenAppConfig object.
	 * @throws ConfigException
	 */
	private static XGenAppConfig fromInputSource(InputSource inputSource) throws ConfigException {
		XGenAppConfig xGenAppConfig;
		// Create a resource on the schema file.
		// Schema file generated using following tutorial: https://examples.javacodegeeks.com/core-java/xml/bind/jaxb-schema-validation-example/
		String xGenAppConfigXsdFileName = String.format("%s.xsd", XGenAppConfig.class.getSimpleName());
		URL xGenAppConfigXsdResource = XGenAppConfig.class.getResource(xGenAppConfigXsdFileName);
		// If the schema file can't be found, throw an exception.
		if (xGenAppConfigXsdResource == null) {
			throw new ConfigException(String.format("Can't find the schema file '%s'", xGenAppConfigXsdFileName));
		}
		
		// Try to load the schema.
		Schema configSchema;
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			configSchema = sf.newSchema(xGenAppConfigXsdResource);
		} catch (SAXException e) {
			throw new ConfigException(String.format("Couldn't read the schema file (%s)", xGenAppConfigXsdResource.toString()), e);
		}
		
		// Try to unmarshal the config file.
		try {
			// Create the JAXB context.
			JAXBContext jaxbContext = JAXBContext.newInstance(XGenAppConfig.class);
			Unmarshaller xGenAppConfigUnmarshaller = jaxbContext.createUnmarshaller();
			// Set the schema on the unmarshaller.
			xGenAppConfigUnmarshaller.setSchema(configSchema);
			// Set the event handler.
			xGenAppConfigUnmarshaller.setEventHandler(new UnmarshallValidationEventHandler());
			// Unmarshal the config.
			xGenAppConfig = (XGenAppConfig) xGenAppConfigUnmarshaller.unmarshal(inputSource);
		} catch (UnmarshalException e) {
			// If the linked exception is a sax parse exception, it contains the error in the config file.
			if (e.getLinkedException() instanceof SAXParseException) {
				throw new ConfigException(String.format("Error in app config file: %s", e.getLinkedException().getMessage()), e);
			} else {
				throw new ConfigException(String.format("Error in app config file: %s", e.getMessage()), e);
			}
		} catch (JAXBException e) {
			throw new ConfigException(String.format("Couldn't read the config file"), e);
		}
		
		
		// Return the app config.
		return xGenAppConfig;
	}
}
