package com.xbreeze.xgenerate.model;

import java.util.logging.Logger;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xbreeze.xgenerate.config.model.ModelAttributeInjection;
import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.template.xml.XMLUtils;

/**
 * The model preprocessor.
 * @author Harmen
 */
public class ModelPreprocessor {
	private static final Logger logger = Logger.getLogger(ModelPreprocessor.class.getName());

	/**
	 * Pre-process the model.
	 * @param model
	 * @param modelConfig
	 * @throws ModelPreprocessorException 
	 */
	public static void preprocessModel(Model model, ModelConfig modelConfig) throws ModelPreprocessorException {
		logger.info("Starting model preprocessing");
		
		// ModelAttributeInjections
		if (modelConfig.getModelAttributeInjections() != null && modelConfig.getModelAttributeInjections().size() > 0) {
			logger.info("ModelAttributeInjections defined, so pre-processing the model");
			
			for (ModelAttributeInjection mai : modelConfig.getModelAttributeInjections()) {
				try {
					// Execute the model XPath on the Document.
					NodeList sectionNodes = (NodeList)XMLUtils.getXPath().evaluate(mai.getModelXPath(), model.getModelDocument(), XPathConstants.NODESET);
					// Loop through the found nodes.
					for (int i=0; i<sectionNodes.getLength(); i++) {
						// Store the node in a local variable.
						Node currentNode = sectionNodes.item(i);
						
						// The node found in the model must be an element, cause we are going to inject an Attribute node.
						if (currentNode.getNodeType() != Node.ELEMENT_NODE)
							throw new ModelPreprocessorException(String.format("The ModelAttributeInjection modelXPath does not result in a element (%s -> %s)", mai.getModelXPath(), currentNode.getNodeName()));
						
						logger.info(String.format("Injecting model attribute '%s' to element '%s'", mai.getTargetAttribute(), currentNode.getNodeName()));
						// Create the new Attribute node.
						Attr newAttribute = model.getModelDocument().createAttribute(mai.getTargetAttribute());
						// Set the attribute value.
						newAttribute.setNodeValue(mai.getTargetValue());
						// Add the new attribute to the current node.
						currentNode.getAttributes().setNamedItem(newAttribute);
					}
				}
				// When there is something wrong with the XPath, throw an exception.
				catch (XPathExpressionException e) {
					throw new ModelPreprocessorException(String.format("Error while executing XPath on the model: %s", e.getMessage()));
				}
			}
		}
		
		logger.info("End model preprocessing");
	}
}
