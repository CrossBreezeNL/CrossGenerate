package com.xbreeze.xgenerate.model;

import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xbreeze.xgenerate.config.model.ModelAttributeInjection;
import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.config.model.ModelNodeRemoval;
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
		
		// ModelNodeRemovals
		// Perform these first, so the injections aren't performed on nodes which will be removed afterwards.
		if (modelConfig != null && modelConfig.getModelModelNodeRemovals() != null && modelConfig.getModelModelNodeRemovals().size() > 0) {
			logger.fine("ModelNodeRemovals defined, so removal is being done now");
			
			// Loop through the model node removals and process them.
			for (ModelNodeRemoval mnr : modelConfig.getModelModelNodeRemovals()) {
				try {
					// Execute the model XPath on the Document.
					NodeList nodesToRemove = (NodeList)XMLUtils.getXPath().evaluate(mnr.getModelXPath(), model.getModelDocument(), XPathConstants.NODESET);
					
					// Loop through the found nodes.
					for (int i=0; i<nodesToRemove.getLength(); i++) {
						logger.fine(String.format("Removing model node %s[%d]", mnr.getModelXPath(), i));
						// Store the node in a local variable.
						Node currentNode = nodesToRemove.item(i);
						// Remove an attribute is done using a different function then for elements.
						if (currentNode instanceof Attr) {
							Attr attr = (Attr)currentNode;
							attr.getOwnerElement().removeAttributeNode(attr);
						}
						else {
							// Remove the current node from the parent.
							currentNode.getParentNode().removeChild(currentNode);
						}
					}
					
				} catch (XPathExpressionException e) {
					throw new ModelPreprocessorException(String.format("Error while executing ModelModelNodeRemoval XPath on the model: %s", e.getMessage()));
				}
			}
		}
		
		// ModelAttributeInjections
		if (modelConfig != null && modelConfig.getModelAttributeInjections() != null && modelConfig.getModelAttributeInjections().size() > 0) {
			logger.fine("ModelAttributeInjections defined, so injection will be done now");
			
			// Loop through the model attribute injections and process them.
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
						
						logger.fine(String.format("Injecting model attribute '%s' to element '%s'", mai.getTargetAttribute(), currentNode.getNodeName()));
						// Create the new Attribute node.
						Attr newAttribute = model.getModelDocument().createAttribute(mai.getTargetAttribute());

						// Set the attribute value, either from XPath or constant value
						if (mai.getTargetXPath()!= null) {
							XPath xp = XMLUtils.getXPath();
							String value = (String)xp.evaluate(mai.getTargetXPath(), currentNode, XPathConstants.STRING);
							newAttribute.setNodeValue(value);
						}
						if (mai.getTargetValue()!= null) {
							newAttribute.setNodeValue(mai.getTargetValue());
						}
						// Add (or replace) the new attribute to the current node.
						currentNode.getAttributes().setNamedItem(newAttribute);
					}
				}
				// When there is something wrong with the XPath, throw an exception.
				catch (XPathExpressionException e) {
					throw new ModelPreprocessorException(String.format("Error while executing ModelAttributeInjection XPath on the model: %s", e.getMessage()));
				}
			}
		}
		
		logger.info("End model preprocessing");
	}
}
