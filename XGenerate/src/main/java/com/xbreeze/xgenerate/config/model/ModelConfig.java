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
 *      Jacob Siemaszko - Crossbreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.config.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import com.xbreeze.xgenerate.config.NamespaceConfig;

/**
 * The Model configuration object.
 * Here the following configuration can be set on the model:
 *  - ModelAttributeInjection 
 * @author Harmen
 */
// Set the order of the properties.
@XmlType(propOrder={"namespaces", "modelAttributeInjections", "modelNodeRemovals"})
public class ModelConfig {
	/**
	 * The namespaces occurring in the model file that are required for model modifications and XSLT.
	 */	
	private ArrayList<NamespaceConfig> namespaces;
	
	/**
	 * The ModelAttributeInjection elements specified within this Model configuration.
	 */
	private ArrayList<ModelAttributeInjection> modelAttributeInjections;
	
	/**
	 * The ModelNodeRemoval elements specified within the Model configuration.
	 */
	private ArrayList<ModelNodeRemoval> modelNodeRemovals;
	
	/**
	 * Constructor.
	 */
	public ModelConfig() {
		// Initialize the model namespaces
		this.namespaces = new ArrayList<NamespaceConfig>();
		// Initialize the modelAttributeInjections.
		this.modelAttributeInjections = new ArrayList<ModelAttributeInjection>();
		// Initialize the modelModelNodeRemovals.
		this.setModelNodeRemovals(new ArrayList<ModelNodeRemoval>());
	}
	
	/**
	 * @return the ModelNameSpaces
	 */
	@XmlElement(name="ModelNamespace")
	@XmlElementWrapper(name="ModelNamespaces")
	public ArrayList<NamespaceConfig> getNamespaces() {
		return this.namespaces;
	}

	/**
	 * @param namespaces the namespaces to set for the model.
	 */
	public void setNamespaces(ArrayList<NamespaceConfig> namespaces) {
		this.namespaces = namespaces;
	}

	/**
	 * @return the modelAttributeInjections
	 */
	@XmlElement(name="ModelAttributeInjection")
	@XmlElementWrapper(name="ModelAttributeInjections")
	public ArrayList<ModelAttributeInjection> getModelAttributeInjections() {
		return modelAttributeInjections;
	}

	/**
	 * @param modelAttributeInjections the modelAttributeInjections to set
	 */
	public void setModelAttributeInjections(ArrayList<ModelAttributeInjection> modelAttributeInjections) {
		this.modelAttributeInjections = modelAttributeInjections;
	}

	/**
	 * @return the modelModelNodeRemovals
	 */
	@XmlElement(name="ModelNodeRemoval")
	@XmlElementWrapper(name="ModelNodeRemovals")
	public ArrayList<ModelNodeRemoval> getModelNodeRemovals() {
		return modelNodeRemovals;
	}

	/**
	 * @param modelNodeRemovals the modelModelNodeRemovals to set
	 */
	public void setModelNodeRemovals(ArrayList<ModelNodeRemoval> modelNodeRemovals) {
		this.modelNodeRemovals = modelNodeRemovals;
	}
}
