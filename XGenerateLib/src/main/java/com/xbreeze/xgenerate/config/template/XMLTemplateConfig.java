package com.xbreeze.xgenerate.config.template;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
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
	 * The list of XML node text templates.
	 * @see XMLTemplatePlaceholderInjection
	 */
	private ArrayList<XMLNodeTextTemplateConfig> xmlNodeTextTemplates;
	
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
