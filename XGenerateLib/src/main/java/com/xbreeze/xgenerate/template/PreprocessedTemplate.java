package com.xbreeze.xgenerate.template;

import java.util.ArrayList;
import java.util.Collections;

import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;

/**
 * This object represents a pre-processed template
 * where all modifications on the raw content are done so it can be sectionized.
 * @author Harmen
 */
public class PreprocessedTemplate {
	/**
	 * The raw pre-processed template as a String.
	 */
	private String _preprocessedRawTemplate;
	
	/**
	 * The collection of template annotations for this template.
	 */
	private ArrayList<TemplateAnnotation> _templateAnnotations;
	
	/**
	 * Constructor.
	 * Template annotations are sorted here so its guaranteed they are sorted when consuming.
	 * @param preprocessedRawTemplate The raw pre-processed template.
	 * @param templateAnnotations The template annotations.
	 */
	public PreprocessedTemplate(String preprocessedRawTemplate, ArrayList<TemplateAnnotation> templateAnnotations) {
		this._preprocessedRawTemplate = preprocessedRawTemplate;
		this._templateAnnotations = templateAnnotations;
		
		// Now the section begin indexes are found, we sort the collection based on the section begin index for sections and annotation index for other annotations.
		// This makes the order of set according to where section should be created in the preprocessed template.
		Collections.sort(this._templateAnnotations);
	}
	
	/**
	 * Get the raw pre-processed template.
	 * @return
	 */
	public String getPreprocessedRawTemplate() {
		return this._preprocessedRawTemplate;
	}
	
	/**
	 * Get the template annotations.
	 * @return The template annotations.
	 */
	public ArrayList<TemplateAnnotation> getTemplateAnnotations() {
		return this._templateAnnotations;
	}
}
