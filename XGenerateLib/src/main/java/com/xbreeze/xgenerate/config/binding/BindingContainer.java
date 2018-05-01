package com.xbreeze.xgenerate.config.binding;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.xbreeze.xgenerate.template.TemplatePreprocessorException;

/**
 * The BindingContainer can contains a list of SectionModelBindingConfig's and PlaceholderConfig's.
 * 
 * @author Harmen
 */
public abstract class BindingContainer {
	/**
	 * The SectionModelBinding elements specified within this SectionModelBinding configuration (so nested).
	 */
	protected ArrayList<SectionModelBindingConfig> _sectionModelBindingConfigs;
	
	/**
	 * A list of placeholder's which can be used inside the section of this binding.
	 */
	protected ArrayList<PlaceholderConfig> _placeholderConfigs;
	
	/**
	 * Get the list of placeholder config's.
	 * @return The list of placeholder config's.
	 */
	@XmlElement(name="Placeholder")
	@XmlElementWrapper(name="Placeholders")
	public ArrayList<PlaceholderConfig> getPlaceholderConfigs() {
		return _placeholderConfigs;
	}

	/**
	 * Set the list of placeholder config's.
	 * @param placeholderConfigs the placeholder config's to set
	 */
	public void setPlaceholderConfigs(ArrayList<PlaceholderConfig> placeholderConfigs) {
		this._placeholderConfigs = placeholderConfigs;
	}
	
	/**
	 * @return the sectionModelBindings
	 */
	@XmlElement(name="SectionModelBinding")
	public ArrayList<SectionModelBindingConfig> getSectionModelBindingConfigs() {
		return _sectionModelBindingConfigs;
	}

	/**
	 * @param sectionModelBindingConfigs the sectionModelBindingConfigs to set
	 */
	public void setSectionModelBindingConfigs(ArrayList<SectionModelBindingConfig> sectionModelBindingConfigs) {
		this._sectionModelBindingConfigs = sectionModelBindingConfigs;
	}
	
	/**
	 * Get a SectionModelBindings by name of section.
	 * @param sectionName The name of the section.
	 * @return Get the section model bindings bound to the section.
	 */
	public SectionModelBindingConfig[] getSectionModelBindingConfigs(String sectionName) {
		if (this._sectionModelBindingConfigs != null)
			return this._sectionModelBindingConfigs.stream().filter(smb -> smb.getSectionName().equalsIgnoreCase(sectionName)).toArray(SectionModelBindingConfig[]::new);
		return null;
	}
	
	/**
	 * Get the unique placeholder name for a section based on the binding(s).
	 * @param sectionName The section name.
	 * @return The unique placeholder name
	 * @throws TemplatePreprocessorException
	 */
	public String getUniqueSectionPlaceholderName(String sectionName) throws TemplatePreprocessorException {
		SectionModelBindingConfig[] sectionModelBindingConfigs = getSectionModelBindingConfigs(sectionName);
		if (sectionModelBindingConfigs != null && sectionModelBindingConfigs.length > 0) {
			// Store the first placeholder name.
			String placeholderName = sectionModelBindingConfigs[0].getPlaceholderName();
			
			// If the placeholder name is not unique, throw an exception.
			if (Arrays.stream(sectionModelBindingConfigs).filter(smb -> !smb.getPlaceholderName().equals(placeholderName)).count() > 0)
				throw new TemplatePreprocessorException(String.format("Found different placeholder names for bindings on Section '%s'", sectionName));
			
			return placeholderName;
		}
		return null;
	}
}