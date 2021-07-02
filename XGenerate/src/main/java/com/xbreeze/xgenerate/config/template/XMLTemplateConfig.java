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
package com.xbreeze.xgenerate.config.template;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateXmlSectionAnnotation;
import com.xbreeze.xgenerate.template.xml.XMLTemplatePreprocessor;

/**
 * A xml template
 * This is a xml template which contains an explicit hierarchy.
 * Examples: Microsoft SSIS (DTSX), Informatica PowerCenter & IBM DataStage.
 * 
 * The Template part of CrossBreeze configuration.
 * It contains configuration for:
 *  - TemplatePlaceholderInjections (TemplatePlaceholderInjection)
 *  - TextTemplates (XMLNodeTextTemplate)
 *  
 * @author Harmen
 */
public class XMLTemplateConfig extends RootTemplateConfig {
	
	/**
	 * The list of template attribute injections
	 */
	private ArrayList<XMLTemplateAttributeInjection> _templateAttributeInjections;
	
	/**
	 * The list of template placeholder injections.
	 * @see XMLTemplatePlaceholderInjection
	 */
	private ArrayList<XMLTemplatePlaceholderInjection> _templatePlaceholderInjections;
	
	/**
	 * The list of template node removals.
	 * @see XMLTemplateNodeRemoval
	 */
	private ArrayList<XMLTemplateNodeRemoval> _templateNodeRemovals;
	
	/**
	 * The list of XML node text templates.
	 * @see XMLTemplatePlaceholderInjection
	 */
	private ArrayList<XMLNodeTextTemplateConfig> xmlNodeTextTemplates;
	
	/**
	 * Default constructor.
	 */
	public XMLTemplateConfig() {
		super();
	}
	
	@Override
	@XmlElementWrapper(name="XmlSections")
	@XmlElement(name="XmlSection", type=TemplateXmlSectionAnnotation.class)
	public ArrayList<? extends TemplateSectionAnnotation> getSectionAnnotations() {
		return super.getSectionAnnotations();
	}
	
	/**
	 * @return the templateAttributeInjections
	 */
	@XmlElement(name="TemplateAttributeInjection")
	@XmlElementWrapper(name="TemplateAttributeInjections")
	public ArrayList<XMLTemplateAttributeInjection> getTemplateAttributeInjections() {
		return _templateAttributeInjections;
	}

	/**
	 * set the set if templateAttributeInjections
	 * @param templateAttributeInjections
	 */
	public void setTemplateAttributeInjections(ArrayList<XMLTemplateAttributeInjection> templateAttributeInjections) {
		this._templateAttributeInjections = templateAttributeInjections;
	}
	
	/**
	 * @return the templatePlaceholderInjections
	 */
	@XmlElement(name="TemplatePlaceholderInjection")
	@XmlElementWrapper(name="TemplatePlaceholderInjections")
	public ArrayList<XMLTemplatePlaceholderInjection> getTemplatePlaceholderInjections() {
		return _templatePlaceholderInjections;
	}

	/**
	 * @param templatePlaceholderInjections the templatePlaceholderInjections to set
	 */
	public void setTemplatePlaceholderInjections(ArrayList<XMLTemplatePlaceholderInjection> templatePlaceholderInjections) {
		this._templatePlaceholderInjections = templatePlaceholderInjections;
	}

	/**
	 * @return the _templateNodeRemovals
	 */
	@XmlElement(name="TemplateNodeRemoval")
	@XmlElementWrapper(name="TemplateNodeRemovals")
	public ArrayList<XMLTemplateNodeRemoval> getTemplateNodeRemovals() {
		return _templateNodeRemovals;
	}

	/**
	 * @param _templateNodeRemovals the _templateNodeRemovals to set
	 */
	public void setTemplateNodeRemovals(ArrayList<XMLTemplateNodeRemoval> _templateNodeRemovals) {
		this._templateNodeRemovals = _templateNodeRemovals;
	}

	/**
	 * @return the xmlNodeTextTemplates
	 */
	@XmlElement(name="TextTemplate")
	@XmlElementWrapper(name="TextTemplates")
	public ArrayList<XMLNodeTextTemplateConfig> getXmlNodeTextTemplates() {
		return xmlNodeTextTemplates;
	}

	/**
	 * @param xmlNodeTextTemplates the xmlNodeTextTemplates to set
	 */
	public void setXmlNodeTextTemplates(ArrayList<XMLNodeTextTemplateConfig> xmlNodeTextTemplates) {
		this.xmlNodeTextTemplates = xmlNodeTextTemplates;
	}
	
	/**
	 * Return the XMLTemplatePreprocessor.
	 */
	@Override
	public TemplatePreprocessor getTemplatePreprocessor(XGenConfig config) {
		return new XMLTemplatePreprocessor(config);
	}
}
