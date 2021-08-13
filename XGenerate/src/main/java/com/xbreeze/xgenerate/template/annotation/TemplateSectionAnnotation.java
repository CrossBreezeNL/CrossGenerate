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
package com.xbreeze.xgenerate.template.annotation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The Section annotation
 * With this annotation one can specify a section in a template.
 * This section is identified by its name (which must be unique within a template).
 * 
 * @author Harmen
 */
@XmlTransient
abstract public class TemplateSectionAnnotation extends TemplateAnnotation {
	/**
	 * The name of the section.
	 */
	protected String name;
	
	/**
	 * When specified, the overridden placeholderName
	 */
	protected String placeholderName;
	
	/**
	 * indicates if the section, when defined in the config, is optional or not	 
	 * Default value is false
	 */
	
	protected boolean optional = false;
	/**
	 * Indicator whether the section name was set by the user.
	 */
	protected boolean userDefinedSectionName = true;
	
	/**
	 * @return the name
	 */
	@XmlAttribute(required=true)
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the placeHolderName
	 */
	@XmlAttribute(required=false)
	public String getPlaceholderName() {
		return this.placeholderName;
	}
	
	/**
	 * @return the optional indicator
	 */
	@XmlAttribute(required=false)
	public Boolean isOptional() {
		return this.optional;
	}
	
	/**
	 * @param name the name to set
	 * This must be implemented by the child classes so we can deviate set methods available in annotations from other set methods.
	 */
	abstract public void setName(String name);
	
	
	abstract public void setPlaceholderName(String placeHolderName);
	
	abstract public void setOptional(Boolean optional);
		
	/**
	 * @return Return whether the section name is user defined.
	 */
	public boolean isUserDefinedSectionName() {
		return userDefinedSectionName;
	}
}
