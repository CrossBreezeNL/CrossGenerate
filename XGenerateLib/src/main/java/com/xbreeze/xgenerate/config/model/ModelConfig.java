package com.xbreeze.xgenerate.config.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * The Model configuration object.
 * Here the following configuration can be set on the model:
 *  - ModelAttributeInjection 
 * @author Harmen
 */
public class ModelConfig {
	/**
	 * The ModelAttributeInjection elements specified within this Model configuration.
	 */
	private ArrayList<ModelAttributeInjection> _modelAttributeInjections;
	
	/**
	 * The ModelNodeRemoval elements specified within the Model configuration.
	 */
	private ArrayList<ModelNodeRemoval> _modelNodeRemovals;
	
	/**
	 * The namespaces occurring in the model file that are required for XSLT.
	 */	
	private ArrayList<ModelNameSpace> _modelNameSpaces;
	
	/**
	 * Constructor.
	 */
	public ModelConfig() {
		// Initialize the modelAttributeInjections.
		this._modelAttributeInjections = new ArrayList<ModelAttributeInjection>();
		// Initialize the modelModelNodeRemovals.
		this.setModelNodeRemovals(new ArrayList<ModelNodeRemoval>());
		// Initialize the model namespaces
		this._modelNameSpaces = new ArrayList<ModelNameSpace>();
	}

	/**
	 * @return the modelAttributeInjections
	 */
	@XmlElement(name="ModelAttributeInjection")
	@XmlElementWrapper(name="ModelAttributeInjections")
	public ArrayList<ModelAttributeInjection> getModelAttributeInjections() {
		return _modelAttributeInjections;
	}

	/**
	 * @param modelAttributeInjections the modelAttributeInjections to set
	 */
	public void setModelAttributeInjections(ArrayList<ModelAttributeInjection> modelAttributeInjections) {
		this._modelAttributeInjections = modelAttributeInjections;
	}

	/**
	 * @return the modelModelNodeRemovals
	 */
	@XmlElement(name="ModelNodeRemoval")
	@XmlElementWrapper(name="ModelNodeRemovals")
	public ArrayList<ModelNodeRemoval> getModelNodeRemovals() {
		return _modelNodeRemovals;
	}

	/**
	 * @param modelNodeRemovals the modelModelNodeRemovals to set
	 */
	public void setModelNodeRemovals(ArrayList<ModelNodeRemoval> modelNodeRemovals) {
		this._modelNodeRemovals = modelNodeRemovals;
	}
	
	/**
	 * @return the ModelNameSpaces
	 */
	@XmlElement(name="ModelNameSpace")
	@XmlElementWrapper(name="ModelNameSpaces")
	public ArrayList<ModelNameSpace> getModelNameSpaces() {
		return this._modelNameSpaces;
	}

	/**
	 * @param modelNameSpaces the Model Namespaces to set
	 */
	public void setModelNameSpaces(ArrayList<ModelNameSpace> modelNameSpaces) {
		this._modelNameSpaces = modelNameSpaces;
	}
}
