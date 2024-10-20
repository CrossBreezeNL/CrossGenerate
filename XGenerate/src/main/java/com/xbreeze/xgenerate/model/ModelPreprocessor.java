/*******************************************************************************
 *   Copyright (c) 2021 CrossBreeze
 *
 *   This file is part of CrossGenerate.
 *
 *      CrossGenerate is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      CrossGenerate is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with CrossGenerate.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *      Willem Otten - CrossBreeze
 *      Harmen Wessels - CrossBreeze
 *      Jacob Siemaszko - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import net.sf.saxon.xpath.XPathExpressionImpl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xbreeze.xgenerate.config.model.ModelAttributeInjection;
import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.config.model.ModelNodeRemoval;
import com.xbreeze.xgenerate.config.model.ModelAttributeInjectionValueMapping;
import com.xbreeze.xgenerate.utils.SaxonXMLUtils;
import com.xbreeze.xgenerate.utils.XmlException;


/**
 * The model preprocessor.
 * 
 * @author Harmen
 */
public class ModelPreprocessor {
	private static final Logger logger = Logger.getLogger(ModelPreprocessor.class.getName());

	/**
	 * Pre-process the model.
	 * 
	 * @param model
	 * @param modelConfig
	 * @throws ModelPreprocessorException
	 */
	public static void preprocessModel(Model model, ModelConfig modelConfig) throws ModelPreprocessorException {
		logger.info("Starting model preprocessing");

		// Load the preprocessed model into memory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException exc) {			
			throw new ModelPreprocessorException(
					String.format("Error while reading model XML file: %s", exc.getMessage()));
		}
		Document doc;
		SaxonXMLUtils xmlHelper = new SaxonXMLUtils();
		xmlHelper.setNamespaces(modelConfig.getNamespaces());
		
		try {
			doc = builder.parse(new ByteArrayInputStream(model.getModelFileContent().getBytes()));
		} catch(SAXException | IOException exc) {
			throw new ModelPreprocessorException(
				String.format("Error while reading model XML file: %s", exc.getMessage()));
		}
		
		
		// ModelAttributeInjections
		// First do attribute injection, in case attributes are used as source that are
		// removed in the next step
		if (modelConfig != null && modelConfig.getModelAttributeInjections() != null
				&& modelConfig.getModelAttributeInjections().size() > 0) {
			doc = performModelAttributeInjections(doc, xmlHelper,
					modelConfig.getModelAttributeInjections());
		}

		
		// ModelNodeRemovals
		if (modelConfig != null && modelConfig.getModelNodeRemovals() != null
				&& modelConfig.getModelNodeRemovals().size() > 0) {
			doc = performModelNodeRemovals(doc, xmlHelper, modelConfig.getModelNodeRemovals());
		}
		
		//Transform the preprocessed document to string and store it in the model object.
		String preprocessedModel;
		try {
			preprocessedModel = SaxonXMLUtils.XmlDocumentToString(doc);
		} catch (XmlException e) {
			throw new ModelPreprocessorException(String.format("Error transforming preprocessed model to string: %s", e.getMessage()));
		}
		
		
		// Store the pre-processed model as string in the Model object.
		model.setPreprocessedModel(preprocessedModel);

		logger.info("End model preprocessing");
	}

	private static Document performModelNodeRemovals(Document modelDoc, SaxonXMLUtils xmlHelper,
			ArrayList<ModelNodeRemoval> modelModelNodeRemovals)
			throws ModelPreprocessorException {

		logger.fine("Performing model node removals.");
		// Loop through the model node removals and process them.
		for (ModelNodeRemoval mnr : modelModelNodeRemovals) {
			try {
				XPathExpressionImpl expr = xmlHelper.getXPathExpression(mnr.getModelXPath());
				NodeList result = (NodeList) expr.evaluate(modelDoc, XPathConstants.NODESET);
				for (int i = 0; i < result.getLength(); i++) {
					Node node = result.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						node.getParentNode().removeChild(node);
					} else if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
						((Attr)node).getOwnerElement().removeAttributeNode((Attr)node);
					}
				}	
			} catch (XPathExpressionException e) {
				throw new ModelPreprocessorException(String.format("Error processing XPATH expression for node removal %s, %s", mnr.getModelXPath(), e.getMessage()));
			}
		}
		// Return the modified XML document.
		return modelDoc;
	}

	private static Document performModelAttributeInjections(Document modelDoc, SaxonXMLUtils xmlHelper,
			ArrayList<ModelAttributeInjection> modelAttributeInjections)
			throws ModelPreprocessorException {
		logger.fine("Performing model attribute injections.");
		
		// Loop through the model attribute injections and process them.
		for (ModelAttributeInjection mai : modelAttributeInjections) {
			try {
				XPathExpressionImpl expr = xmlHelper.getXPathExpression(mai.getModelXPath());
				
				NodeList result = (NodeList) expr.evaluate(modelDoc, XPathConstants.NODESET);
				for (int i = 0; i < result.getLength(); i++) {
					Node node = result.item(i);
					String targetValue = null;
					//If a target xpath is set, evaluate it to get a value
					if (mai.getTargetXPath() !=null) {
						try {
							XPathExpressionImpl targetExpression = xmlHelper.getXPathExpression(mai.getTargetXPath());
							targetValue = (String)targetExpression.evaluate(node, XPathConstants.STRING);
						} catch (XPathExpressionException e) {
							throw new ModelPreprocessorException(String.format("Error while processing model attribute injection for target XPath ´%s´: %s", mai.getTargetXPath(), e.getMessage()));
						}
					} else if (mai.getTargetValue() != null) {
						targetValue = mai.getTargetValue();
					} else if (mai.getValueMappings() != null ) {
						//Get the input node value to use for finding the mapped output value
						try {
							XPathExpressionImpl valueMappingExpression = xmlHelper.getXPathExpression(mai.getValueMappings().getInputNode());
							String inputNodeValue = (String)valueMappingExpression.evaluate(node, XPathConstants.STRING);
							List<ModelAttributeInjectionValueMapping> foundValueMappings = mai.getValueMappings()
									.getModelAttributeInjectionValueMappings().stream()
									.filter(vm -> vm.getInputValue().equals(inputNodeValue))
									.collect(Collectors.toList());
							// There should only be 1 found value mapping.
							if (foundValueMappings.size() == 1) {
								targetValue = foundValueMappings.get(0).getOutputValue();
								logger.info(String.format(
										"Value mappings defined for attribute injection, input node value: ´%s´, target value: ´%s´",
										inputNodeValue, targetValue));
							}
							// If multiple value mappings are found, throw an exception.
							else if (foundValueMappings.size() > 1) {
								throw new ModelPreprocessorException(String.format(
										"%d value mappings found for input node value ´%s´, expected exactly 1!",
										foundValueMappings.size(), inputNodeValue));
							}
							// If no value mappings are found, print a warning.
							else {
								logger.warning(String.format("%d value mappings found for input node value ´%s´.",
										foundValueMappings.size(), inputNodeValue));
							}
						} catch (XPathExpressionException e) {
							throw new ModelPreprocessorException(String.format("Error evaluating XPATH expression for value mapping %s, %s", mai.getValueMappings().getInputNode(), e.getMessage()));
						}
					}
					
					if (targetValue != null && mai.getTargetAttribute() != null) {
						((Element)node).setAttribute(mai.getTargetAttribute(), targetValue);
					}
				}
			} catch (XPathExpressionException e) {
				throw new ModelPreprocessorException(String.format("Error while processing model attribute injection for model XPath ´%s´: %s", mai.getModelXPath(), e.getMessage()));
			}
		}
		// Return the modified XML document.
		return modelDoc;
	}
}