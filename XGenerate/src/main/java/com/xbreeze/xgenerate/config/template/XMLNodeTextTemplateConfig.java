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
package com.xbreeze.xgenerate.config.template;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation;

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

	@Override
	@XmlElementWrapper(name="TextSections")
	@XmlElement(name="TextSection", type=TemplateTextSectionAnnotation.class)
	public ArrayList<? extends TemplateSectionAnnotation> getSectionAnnotations() {
		return super.getSectionAnnotations();
	}
}
