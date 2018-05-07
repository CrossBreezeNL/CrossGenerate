package com.xbreeze.xgenerate.config.template;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation;
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

	@Override
	@XmlElementWrapper(name="TextSections")
	@XmlElement(name="TextSection", type=TemplateTextSectionAnnotation.class)
	public ArrayList<? extends TemplateSectionAnnotation> getSectionAnnotations() {
		return super.getSectionAnnotations();
	}
}
