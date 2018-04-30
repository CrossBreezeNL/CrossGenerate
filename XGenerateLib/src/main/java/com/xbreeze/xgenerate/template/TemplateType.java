package com.xbreeze.xgenerate.template;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.text.TextTemplatePreprocessor;
import com.xbreeze.xgenerate.template.xml.XMLTemplatePreprocessor;

/**
 * The type of the template.
 * 
 * @author Harmen
 */
public enum TemplateType {
	
	/**
	 * A text template
	 * This is plain text and doesn't contain an explicit hierarchy.
	 * Examples: SQL
	 */
	text {
		@Override
		public TemplatePreprocessor getTemplatePreprocessor(XGenConfig config) {
			return new TextTemplatePreprocessor(config);
		}
	},
	
	/**
	 * A xml template
	 * This is a xml template which contains an explicit hierarchy.
	 * Examples: Microsoft SSIS (DTSX), Informatica PowerCenter & IBM DataStage.
	 */
	xml {
		@Override
		public TemplatePreprocessor getTemplatePreprocessor(XGenConfig config) {
			return new XMLTemplatePreprocessor(config);
		}
	};
	
	/**
	 * Function to get the preprocessor for the template type.
	 * @param templateConfig The configuration of the template.
	 * @return The template pre-processor for the TemplateType.
	 */
	public abstract TemplatePreprocessor getTemplatePreprocessor(XGenConfig config);
}
