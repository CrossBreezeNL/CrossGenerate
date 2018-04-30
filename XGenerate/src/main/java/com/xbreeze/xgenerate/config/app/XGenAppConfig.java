package com.xbreeze.xgenerate.config.app;

import java.io.File;
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

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.xbreeze.xgenerate.config.ConfigException;
import com.xbreeze.xgenerate.config.UnmarshallValidationEventHandler;
import com.xbreeze.license.LicenseConfig;

@XmlRootElement(name="XGenAppConfig")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"appConfig", "licenseConfig"})
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
	 * The license configuration
	 * 
	 */
	@XmlElement(name="License")
	private LicenseConfig licenseConfig;
	
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
	 * return the license config object
	 * @return
	 */
	public LicenseConfig getLicenseConfig() {
		return licenseConfig;
	}

	/**
	 * Set config object for license
	 * @param licenseConfig
	 */
	public void setLicenseConfig(LicenseConfig licenseConfig) {
		this.licenseConfig = licenseConfig;
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
		
		// Create a resource on the schema file.
		// Schema file generated using following tutorial: https://examples.javacodegeeks.com/core-java/xml/bind/jaxb-schema-validation-example/
		String xGenAppConfigXsdFileName = String.format("%s.xsd", XGenAppConfig.class.getSimpleName());
		URL xGenConfigXsdResource = XGenAppConfig.class.getResource(xGenAppConfigXsdFileName);
		// If the schema file can't be found, throw an exception.
		if (xGenConfigXsdResource == null) {
			throw new ConfigException(String.format("Can't find the schema file '%s'", xGenAppConfigXsdFileName));
		}
		
		// Try to load the schema.
		Schema configSchema;
		try {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			configSchema = sf.newSchema(xGenConfigXsdResource);
		} catch (SAXException e) {
			throw new ConfigException(String.format("Couldn't read the schema file (%s)", xGenConfigXsdResource.toString()), e);
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
			xGenAppConfig = (XGenAppConfig) xGenAppConfigUnmarshaller.unmarshal(xGenAppConfigFile);
		} catch (UnmarshalException e) {
			// If the linked exception is a sax parse exception, it contains the error in the config file.
			if (e.getLinkedException() instanceof SAXParseException) {
				throw new ConfigException(String.format("Error in app config file: %s (%s)", e.getLinkedException().getMessage(), appConfigFileUri.toString()), e);
			} else {
				throw new ConfigException(String.format("Error in app config file: %s (%s)", e.getMessage(), appConfigFileUri.toString()), e);
			}
		} catch (JAXBException e) {
			throw new ConfigException(String.format("Couldn't read the config file (%s)", appConfigFileUri.toString()), e);
		}
		
		// If the config folder isn't set in the app config, we fill it with the folder location of the app config.
		if (xGenAppConfig.getAppConfig().getConfigFolder() == null || xGenAppConfig.getAppConfig().getConfigFolder().length() == 0)
			xGenAppConfig.getAppConfig().setConfigFolder(Paths.get(appConfigFileUri).getParent().toString());

		// Return the app config.
		return xGenAppConfig;
	}
}
