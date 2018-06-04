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
	 * Constructor.
	 */
	public ModelConfig() {
		// Initialize the modelAttributeInjections.
		this._modelAttributeInjections = new ArrayList<ModelAttributeInjection>();
		// Initialize the modelModelNodeRemovals.
		this.setModelNodeRemovals(new ArrayList<ModelNodeRemoval>());		
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
}
