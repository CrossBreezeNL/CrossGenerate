package com.xbreeze.xgenerate.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.xbreeze.xgenerate.config.model.ModelAttributeInjection;
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
		
		// ModelNodeRemovals
		// Perform these first, so the injections aren't performed on nodes which will be removed afterwards.
		if (modelConfig != null && modelConfig.getModelNodeRemovals() != null && modelConfig.getModelNodeRemovals().size() > 0) {
			preprocessedModel = performModelNodeRemovals(preprocessedModel, modelConfig.getModelNodeRemovals());
		}
		
		// ModelAttributeInjections
		if (modelConfig != null && modelConfig.getModelAttributeInjections() != null && modelConfig.getModelAttributeInjections().size() > 0) {
			preprocessedModel = performModelAttributeInjections(preprocessedModel, modelConfig.getModelAttributeInjections());
		}
		
		// Store the pre-processed model in the Model object.
		model.setPreprocessedModel(preprocessedModel);
		
		logger.info("End model preprocessing");
	}
	
	private static String performModelNodeRemovals(String preprocessedModel, ArrayList<ModelNodeRemoval> modelModelNodeRemovals) throws ModelPreprocessorException {
		logger.fine("Performing model node removals.");
		
		// Execute the model XPath on the Document.
		VTDNav nv;
		try {
			nv = XMLUtils.getVTDNav(preprocessedModel);
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
				throw new ModelPreprocessorException(String.format("Error while processing model node removal for XPath %s: %s", mnr.getModelXPath(),  e.getMessage()));
			}
		}
		
		// Return the modified XML document.
		try {
			return XMLUtils.getResultingXml(xm);
		} catch (GeneratorException e) {
			throw new ModelPreprocessorException(e);
		}
	}
	
	private static String performModelAttributeInjections(String preprocessedModel, ArrayList<ModelAttributeInjection> modelAttributeInjections) throws ModelPreprocessorException {
		logger.fine("Performing model attribute injections.");
		
		// Execute the model XPath on the Document.
		VTDNav nv;
		try {
			nv = XMLUtils.getVTDNav(preprocessedModel);
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
			
			try {
				
				// Set the XPath expression from the config.
				ap.selectXPath(mai.getModelXPath());
				
				// Execute the XPath expression and loop through the results.
		        while ((ap.evalXPath()) != -1) {
		        	
		        	String targetValue = null;
		        	
					// Set the attribute value, either from XPath or constant value
					if (mai.getTargetXPath() != null) {
						// Execute the XPath on the current node.
						AutoPilot ap_targetValue = new AutoPilot(nv);
						ap_targetValue.selectXPath(mai.getTargetXPath());
						targetValue = ap_targetValue.evalXPathToString();
						logger.info(String.format("Target XPath defined for attribute injection, value: '%s' => '%s'", mai.getTargetXPath(), targetValue));
					}
					else if (mai.getTargetValue() != null) {
						targetValue = mai.getTargetValue();
					}
		        	
		        	// Append the attribute.
		        	XMLUtils.appendAttribute(nv, xm, mai.getTargetAttribute(), targetValue);
		        }
		        
		        // Output and re-parse the document for the next injection.
		        // This is necessary, otherwise exceptions will be thrown when injecting attributes for the same element.
		        nv = xm.outputAndReparse();
		        // Reset and bind the modifier to the new VTDNav object.
		        xm.reset();
		        xm.bind(nv);
				
			} catch (XPathParseException | XPathEvalException | NavException | ModifyException | ParseException | TranscodeException | IOException | GeneratorException e) {
				throw new ModelPreprocessorException(String.format("Error while processing model attribute injection for XPath %s: %s", mai.getModelXPath(),  e.getMessage()));
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
