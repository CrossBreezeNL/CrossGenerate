package com.xbreeze.xgenerate.config.template;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.text.TextTemplatePreprocessor;

/**
 * A text template
 * This is plain text and doesn't contain an explicit hierarchy.
 * Examples: SQL
 * 
 * @author Harmen
 */
public class TextTemplateConfig extends RootTemplateConfig {
	
	/**
	 * Default constructor.
	 */
	public TextTemplateConfig() {
		super();
	}
	
	/**
	 * Return the TextTemplatePreprocessor.
	 */
	@Override
	public TemplatePreprocessor getTemplatePreprocessor(XGenConfig config) {
		return new TextTemplatePreprocessor(config);
	}
}
