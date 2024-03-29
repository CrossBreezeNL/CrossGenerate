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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.xbreeze.xgenerate.config.NamespaceConfig;
import com.xbreeze.xgenerate.config.model.ModelAttributeInjection;
import com.xbreeze.xgenerate.config.model.ModelAttributeInjectionValueMapping;
import com.xbreeze.xgenerate.config.model.ModelConfig;
import com.xbreeze.xgenerate.config.model.ModelNodeRemoval;
import com.xbreeze.xgenerate.generator.GeneratorException;
import com.xbreeze.xgenerate.utils.XMLUtils;
import com.ximpleware.AutoPilot;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

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

		/// Store the pre-processed model.
		String preprocessedModel = model.getModelFileContent();
		
		
		// ModelAttributeInjections
		// First do attribute injection, in case attributes are used as source that are removed in the next step
		if (modelConfig != null && modelConfig.getModelAttributeInjections() != null && modelConfig.getModelAttributeInjections().size() > 0) {
			preprocessedModel = performModelAttributeInjections(preprocessedModel, modelConfig.getModelAttributeInjections(), modelConfig.getNamespaces());
		}

		// ModelNodeRemovals
		if (modelConfig != null && modelConfig.getModelNodeRemovals() != null && modelConfig.getModelNodeRemovals().size() > 0) {
			preprocessedModel = performModelNodeRemovals(preprocessedModel, modelConfig.getModelNodeRemovals(), modelConfig.getNamespaces());
		}

		// Store the pre-processed model in the Model object.
		model.setPreprocessedModel(preprocessedModel);
		
		logger.info("End model preprocessing");
	}
	
	private static String performModelNodeRemovals(String preprocessedModel, ArrayList<ModelNodeRemoval> modelModelNodeRemovals, ArrayList<NamespaceConfig> namespaces) throws ModelPreprocessorException {
		logger.fine("Performing model node removals.");
		
		// Execute the model XPath on the Document.
		VTDNav nv;
		try {
			nv = XMLUtils.getVTDNav(preprocessedModel, (namespaces != null && namespaces.size() > 0));
		} catch (GeneratorException e) {
			throw new ModelPreprocessorException(String.format("Error while reading model before pre-processing: %s", e.getMessage()), e);
		}
		
		// Create a modifier to modify the model document.
		XMLModifier xm;
		try {
			xm = new XMLModifier(nv);
		} catch (ModifyException e) {
			throw new ModelPreprocessorException(e);
		}
		
		// Loop through the model node removals and process them.
		for (ModelNodeRemoval mnr : modelModelNodeRemovals) {
			// Create an AutoPilot for querying the document.
			AutoPilot ap = new AutoPilot(nv);
			
			// If namespaces are defined, register then on the auto pilot.
			if (namespaces != null && namespaces.size() > 0) {
				// Register the declared namespaces.
				for (NamespaceConfig namespace : namespaces) {
					ap.declareXPathNameSpace(namespace.getPrefix(), namespace.getNamespace());				
				}
			}
			
			try {
				// Set the XPath expression from the config.
				ap.selectXPath(mnr.getModelXPath());
				
				// Execute the XPath expression and loop through the results.
		        while ((ap.evalXPath()) != -1) {
		        	// Remove the node.
		        	xm.remove();
		        }
		        
		        // Output and re-parse the document for the next injection.
		        // This is necessary, otherwise exceptions will be thrown when injecting attributes for the same element.
		        nv = xm.outputAndReparse();
		        // Reset and bind the modifier to the new VTDNav object.
		        xm.reset();
		        xm.bind(nv);
		        
			} catch (XPathParseException | XPathEvalException | NavException | ModifyException | ParseException | TranscodeException | IOException e) {
				throw new ModelPreprocessorException(String.format("Error while processing model node removal for XPath ´%s´: %s", mnr.getModelXPath(),  XMLUtils.getAutopilotExceptionMessage(mnr.getModelXPath(), e)));
			}
		}
		
		// Return the modified XML document.
		try {
			return XMLUtils.getResultingXml(xm);
		} catch (GeneratorException e) {
			throw new ModelPreprocessorException(e);
		}
	}
	
	private static String performModelAttributeInjections(String preprocessedModel, ArrayList<ModelAttributeInjection> modelAttributeInjections, ArrayList<NamespaceConfig> namespaces) throws ModelPreprocessorException {
		logger.fine("Performing model attribute injections.");
		
		// Execute the model XPath on the Document.
		VTDNav nv;
		try {
			// Set the VTD nav to namespace aware if the namespaces are defined.
			nv = XMLUtils.getVTDNav(preprocessedModel, (namespaces != null && namespaces.size() > 0));
		} catch (GeneratorException e) {
			throw new ModelPreprocessorException(String.format("Error while reading model before performing model attribute injections: %s", e.getMessage()), e);
		}
		
		// Create a modifier to modify the model document.
		XMLModifier xm;
		try {
			xm = new XMLModifier(nv);
		} catch (ModifyException e) {
			throw new ModelPreprocessorException(e);
		}
		

		// Loop through the model attribute injections and process them.
		for (ModelAttributeInjection mai : modelAttributeInjections) {
			// Create an AutoPilot for querying the document.
			AutoPilot ap = new AutoPilot(nv);
			
			// If namespaces are defined, register then on the auto pilot.
			if (namespaces != null && namespaces.size() > 0) {
				// Register the declared namespaces.
				for (NamespaceConfig namespace : namespaces) {
					ap.declareXPathNameSpace(namespace.getPrefix(), namespace.getNamespace());				
				}
			}
			
			try {
				
				// Set the XPath expression from the config.
				ap.selectXPath(mai.getModelXPath());
				
				// Execute the XPath expression and loop through the results.
		        while ((ap.evalXPath()) != -1) {
		        	
		        	String targetValue = null;
		        	
					// Set the attribute value, either from XPath or constant value
					if (mai.getTargetXPath() != null) {
						// Execute the XPath on the current node.
						try {
							AutoPilot ap_targetValue = new AutoPilot(nv);
							ap_targetValue.selectXPath(mai.getTargetXPath());
							targetValue = ap_targetValue.evalXPathToString();
							logger.info(String.format("Target XPath defined for attribute injection, value: ´%s´ => ´%s´", mai.getTargetXPath(), targetValue));
						}
						catch (XPathParseException e) {
							throw new ModelPreprocessorException(String.format("Error while processing model attribute injection for target XPath ´%s´: %s", mai.getTargetXPath(),  XMLUtils.getAutopilotExceptionMessage(mai.getTargetXPath(), e)));			
						}
					}
					
					// When the target value is set, write the value.
					else if (mai.getTargetValue() != null) {
						targetValue = mai.getTargetValue();
					}
					
					// When the value mappings are set, process them.
					else if (mai.getValueMappings() != null) {
						try {
							AutoPilot ap_targetValue = new AutoPilot(nv);
							// Get the value of the input node. 
							ap_targetValue.selectXPath(mai.getValueMappings().getInputNode());
							String inputNodeValue = ap_targetValue.evalXPathToString();
							// Using the input node value, find the output value from the value mapping.
							List<ModelAttributeInjectionValueMapping> foundValueMappings = mai.getValueMappings().getModelAttributeInjectionValueMappings().stream().filter(vm -> vm.getInputValue().equals(inputNodeValue)).collect(Collectors.toList());
							// There should only be 1 found value mapping.
							if (foundValueMappings.size() == 1) {
								targetValue = foundValueMappings.get(0).getOutputValue();
								logger.info(String.format("Value mappings defined for attribute injection, input node value: ´%s´, target value: ´%s´", inputNodeValue, targetValue));
							}
							// If multiple value mappings are found, throw an exception.
							else if (foundValueMappings.size() > 1) {
								throw new ModelPreprocessorException(String.format("%d value mappings found for input node value ´%s´, expected exactly 1!", foundValueMappings.size(), inputNodeValue));
							}
							// If no value mappings are found, print a warning.
							else {
								logger.warning(String.format("%d value mappings found for input node value ´%s´.", foundValueMappings.size(), inputNodeValue));
							}
						}
						catch (XPathParseException e) {
							throw new ModelPreprocessorException(String.format("Error while processing model attribute injection for target XPath ´%s´: %s", mai.getTargetXPath(),  XMLUtils.getAutopilotExceptionMessage(mai.getTargetXPath(), e)));			
						}
					}
		        	
		        	// Append the attribute (if it is not null, this can happen if a value mapping returns no value).
					if (targetValue != null)
						XMLUtils.appendAttribute(nv, xm, mai.getTargetAttribute(), targetValue);
		        }
		        
		        // Output and re-parse the document for the next injection.
		        // This is necessary, otherwise exceptions will be thrown when injecting attributes for the same element.
		        nv = xm.outputAndReparse();
		        // Reset and bind the modifier to the new VTDNav object.
		        xm.reset();
		        xm.bind(nv);
				
			} catch (XPathParseException | XPathEvalException | NavException | ModifyException | ParseException | TranscodeException | IOException | GeneratorException e) {
				// Throw a new exception.
				throw new ModelPreprocessorException(String.format("Error while processing model attribute injection for model XPath ´%s´: %s", mai.getModelXPath(),  XMLUtils.getAutopilotExceptionMessage(mai.getModelXPath(), e)));
			}
		}
		
		// Return the modified XML document.
		try {
			return XMLUtils.getResultingXml(xm);
		} catch (GeneratorException e) {
			throw new ModelPreprocessorException(e);
		}
	}
}
