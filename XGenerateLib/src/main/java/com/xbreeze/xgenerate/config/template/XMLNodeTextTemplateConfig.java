package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * The XMLNodeTextTemplateConfig configuration within a RootTemplateConfig.
 * This element can be specified when the parent template fileformat is XML.
 * XML can sometimes contain elements where its text part is a template in itself.
 * Where this TextTemplate resides is specified using the 'node' property of the element.
 * It contains configuration for:
 *  - FileFormat
 *  - Sections (TemplateSectionAnnotation)
 *  
 * @author Harmen
 */
@XmlSeeAlso(AbstractTemplateConfig.class)
public class XMLNodeTextTemplateConfig extends AbstractTemplateConfig {
	
	/**
	 * The XPath pointing to the node(s) which contain the text template.
	 */
	private String _node;

	/**
	 * @return the node
	 */
	@XmlAttribute
	public String getNode() {
		return _node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this._node = node;
	}
}
