package com.xbreeze.xgenerate.config;

import java.io.File;
import java.net.URI;
import java.net.URL;
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

import com.xbreeze.xgenerate.config.binding.BindingConfig;
import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.config.template.TemplateConfig;

/**
 * The XGenConfig class represents the configuration object for CrossGenerate.
 * It contains the model, template and binding configuration.
 * @author Harmen
 */
@XmlRootElement(name="XGenConfig")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder={"modelConfig","templateConfig","bindingConfig"})
public class XGenConfig {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(XGenConfig.class.getName());
	
	/**
	 * The model configuration.
	 * @see ModelConfig
	 */
	@XmlElement(name="Model")
	private ModelConfig modelConfig;
	
	// 
	/**
	 * The template configuration
	 * @see TemplateConfig
	 */
	@XmlElement(name="Template")
	private TemplateConfig templateConfig;
	
	/**
	 * The binding configuration.
	 * @see BindingConfig
	 */
	@XmlElement(name="Binding")
	private BindingConfig bindingConfig;

	/**
	 * @return the model
	 */
	public ModelConfig getModelConfig() {
		return modelConfig;
	}

	/**
	 * @param modelConfig the model to set
	 */
	public void setModelConfig(ModelConfig modelConfig) {
		this.modelConfig = modelConfig;
	}

	/**
	 * @return the template
	 */
	public TemplateConfig getTemplateConfig() {
		return templateConfig;
	}

	/**
	 * @param templateConfig the template to set
	 */
	public void setTemplateConfig(TemplateConfig templateConfig) {
		this.templateConfig = templateConfig;
	}

	/**
	 * @return the binding
	 */
	public BindingConfig getBindingConfig() {
		return bindingConfig;
	}

	/**
	 * @param bindingConfig the binding to set
	 */
	public void setBindingConfig(BindingConfig bindingConfig) {
		this.bindingConfig = bindingConfig;
	}
	
	/**
	 * Unmarshal a file into a XGenConfig object.
	 * @param configFileUri The file to unmarshal.
	 * @return The unmarshalled XGenConfig object.
	 * @throws ConfigException 
	 */
	public static XGenConfig fromFile(URI configFileUri) throws ConfigException {
		logger.info(String.format("Creating XGenConfigFile object from '%s'", configFileUri));
		File XGenConfigFile = new File(configFileUri);
		XGenConfig xGenConfig;
		
		// Create a resource on the schema file.
		// Schema file generated using following tutorial: https://examples.javacodegeeks.com/core-java/xml/bind/jaxb-schema-validation-example/
		String xGenConfigXsdFileName = "XGenConfig.xsd";
		URL xGenConfigXsdResource = XGenConfig.class.getResource(xGenConfigXsdFileName);
		// If the schema file can't be found, throw an exception.
		if (xGenConfigXsdResource == null) {
			throw new ConfigException(String.format("Can't find the schema file '%s'", xGenConfigXsdFileName));
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
			JAXBContext jaxbContext = JAXBContext.newInstance(XGenConfig.class);
			Unmarshaller xGenConfigUnmarshaller = jaxbContext.createUnmarshaller();
			// Set the schema on the unmarshaller.
			xGenConfigUnmarshaller.setSchema(configSchema);
			// Set the event handler.
			xGenConfigUnmarshaller.setEventHandler(new UnmarshallValidationEventHandler());
			// Unmarshal the config.
			xGenConfig = (XGenConfig) xGenConfigUnmarshaller.unmarshal(XGenConfigFile);
		} catch (UnmarshalException e) {
			// If the linked exception is a sax parse exception, it contains the error in the config file.
			if (e.getLinkedException() instanceof SAXParseException) {
				throw new ConfigException(String.format("Error in config file: %s (%s)", e.getLinkedException().getMessage(), configFileUri.toString()), e);
			} else {
				throw new ConfigException(String.format("Error in config file: %s (%s)", e.getMessage(), configFileUri.toString()), e);
			}
		} catch (JAXBException e) {
			throw new ConfigException(String.format("Couldn't read the config file (%s)", configFileUri.toString()), e);
		}

		return xGenConfig;
	}
}
