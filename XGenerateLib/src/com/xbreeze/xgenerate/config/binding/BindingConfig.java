package com.xbreeze.xgenerate.config.binding;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The Binding part of the CrossGenerate configuration.
 * The Binding configuration can contain:
 *  - SectionModelBinding's
 *  - Placeholder's.
 *  
 * @author Harmen
 */
public class BindingConfig extends BindingContainer {
	/**
	 * Set whether the creation of implicit placeholder's is enabled.
	 * This will automatically create a placeholder for every element in the model.
	 */
	private Boolean _implicitPlaceholdersEnabled = false;
	
	/**
	 * The prefix for the implicit placeholder's.
	 * The default value is "_s_".
	 */
	private String _implicitPlaceholderPrefix = "_s_";
	
	/**
	 * The suffix for the implicit placeholder's.
	 * The default value is "_e_".
	 */
	private String _implicitPlaceholderSuffix = "_e_";
	
	/**
	 * @return the implicitPlaceholders
	 */
	@XmlAttribute(name="implicitPlaceholders")
	public Boolean isImplicitPlaceholdersEnabled() {
		return _implicitPlaceholdersEnabled;
	}

	/**
	 * @param implicitPlaceholders the implicitPlaceholders to set
	 */
	public void setImplicitPlaceholdersEnabled(Boolean implicitPlaceholdersEnabled) {
		this._implicitPlaceholdersEnabled = implicitPlaceholdersEnabled;
	}

	/**
	 * @return the implicitPlaceholderPrefix
	 */
	@XmlAttribute
	public String getImplicitPlaceholderPrefix() {
		return _implicitPlaceholderPrefix;
	}

	/**
	 * @param implicitPlaceholderPrefix the implicitPlaceholderPrefix to set
	 */
	public void setImplicitPlaceholderPrefix(String implicitPlaceholderPrefix) {
		this._implicitPlaceholderPrefix = implicitPlaceholderPrefix;
	}

	/**
	 * @return the implicitPlaceholderSuffix
	 */
	@XmlAttribute
	public String getImplicitPlaceholderSuffix() {
		return _implicitPlaceholderSuffix;
	}

	/**
	 * @param implicitPlaceholderSuffix the implicitPlaceholderSuffix to set
	 */
	public void setImplicitPlaceholderSuffix(String implicitPlaceholderSuffix) {
		this._implicitPlaceholderSuffix = implicitPlaceholderSuffix;
	}
}
