package com.xbreeze.xgenerate.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.xbreeze.xgenerate.config.binding.BindingConfig;
import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.config.template.RootTemplateConfig;
import com.xbreeze.xgenerate.config.template.TextTemplateConfig;
import com.xbreeze.xgenerate.config.template.XMLTemplateConfig;

/**
 * The XGenConfig class represents the configuration object for CrossGenerate.
 * It contains the model, template and binding configuration.
 * @author Harmen
 */
@XmlRootElement(name="XGenConfig") // , namespace="http://generate.x-breeze.com/XGenConfig"
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
	 * @see XMLTemplateConfig
	 */
    @XmlElements({
        @XmlElement(name="TextTemplate", type=TextTemplateConfig.class),
        @XmlElement(name="XmlTemplate", type=XMLTemplateConfig.class),
    })
	private RootTemplateConfig templateConfig;
	
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
	public RootTemplateConfig getTemplateConfig() {
		return templateConfig;
	}

	/**
	 * @param templateConfig the template to set
	 */
	public void setTemplateConfig(XMLTemplateConfig templateConfig) {
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
	 * Unmarshal a config from a String.
	 * @param configFileContent The String object to unmarshal.
	 * @return The unmarshelled XGenConfig object.
	 * @throws ConfigException
	 */
	public static XGenConfig fromString(String configFileContent) throws ConfigException {
		return fromInputSource(new InputSource(new StringReader(configFileContent)));
	}
	
	/**
	 * Unmarshal a file into a XGenConfig object.
	 * @param configFileUri The file to unmarshal.
	 * @return The unmarshalled XGenConfig object.
	 * @throws ConfigException 
	 */
	public static XGenConfig fromFile(URI configFileUri) throws ConfigException {
		logger.fine(String.format("Creating XGenConfigFile object from '%s'", configFileUri));
		File XGenConfigFile = new File(configFileUri);
		
		XGenConfig xGenConfig;
		try {
			xGenConfig = fromInputSource(new InputSource(new FileReader(XGenConfigFile)));
		} catch (ConfigException e) {
			// Catch the config exception here to add the filename in the exception text.
			throw new ConfigException(String.format("%s (%s)", e.getMessage(), configFileUri.toString()), e.getCause());
		} catch (FileNotFoundException e) {
			throw new ConfigException(String.format("Couldn't find the config file (%s)", configFileUri.toString()), e);
		}
		
		return xGenConfig;
	}
	
	/**
	 * Create a XGenConfig object using a InputSource.
	 * @param inputSource The InputSource.
	 * @return The XGenConfig object.
	 * @throws ConfigException
	 */
	private static XGenConfig fromInputSource(InputSource inputSource) throws ConfigException {
		XGenConfig xGenConfig;
		
		// Create a resource on the schema file.
		// Schema file generated using following tutorial: https://examples.javacodegeeks.com/core-java/xml/bind/jaxb-schema-validation-example/
		String xGenConfigXsdFileName = String.format("%s.xsd", XGenConfig.class.getSimpleName());
		InputStream xGenConfigSchemaAsStream = XGenConfig.class.getResourceAsStream(xGenConfigXsdFileName);
		// If the schema file can't be found, throw an exception.
		if (xGenConfigSchemaAsStream == null) {
			throw new ConfigException(String.format("Can't find the schema file '%s'", xGenConfigXsdFileName));
		}
		// Create the StreamSource for the schema.
		StreamSource xGenConfigXsdResource = new StreamSource(xGenConfigSchemaAsStream);
		
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
			xGenConfig = (XGenConfig) xGenConfigUnmarshaller.unmarshal(inputSource);
		} catch (UnmarshalException e) {
			// If the linked exception is a sax parse exception, it contains the error in the config file.
			if (e.getLinkedException() instanceof SAXParseException) {
				throw new ConfigException(String.format("Error in config file: %s", e.getLinkedException().getMessage()), e);
			} else {
				throw new ConfigException(String.format("Error in config file: %s", e.getMessage()), e);
			}
		} catch (JAXBException e) {
			throw new ConfigException(String.format("Couldn't read the config file"), e);
		}

		return xGenConfig;
	}
}
