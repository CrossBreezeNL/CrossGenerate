package com.xbreeze.xgenerate.template.text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.AbstractTemplateConfig;
import com.xbreeze.xgenerate.config.template.TextTemplateConfig;
import com.xbreeze.xgenerate.template.PreprocessedTemplate;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionBoundsAnnotation;
import com.xbreeze.xgenerate.template.scanner.AnnotationScanner;

public class TextTemplatePreprocessor extends TemplatePreprocessor {
	/**
	 * Constructor.
	 * @param templateConfig The template config.
	 */
	public TextTemplatePreprocessor(XGenConfig config) {
		super(config);
	}

	/**
	 * Create the pre-proccesed text template.
	 * @throws TemplatePreprocessorException 
	 */
	@Override
	protected PreprocessedTemplate getPreprocessedTemplate(RawTemplate rawTemplate) throws TemplatePreprocessorException {
		logger.fine(String.format("Creating pre-processed template for '%s'.", rawTemplate.getRawTemplateFileName()));

		// Store the raw template content into a local variable.
		String rawTemplateContent = rawTemplate.getRawTemplateContent();
		
		// If the template config is missing, throw an exception.
		if (_config.getTemplateConfig() == null) {
			throw new TemplatePreprocessorException("No template config defined for TextTemplate");
		}
		// Also check whether the template config is a TextTemplateConfig.
		else if (!(_config.getTemplateConfig() instanceof TextTemplateConfig)) {
			throw new TemplatePreprocessorException("TextTemplatePreprocessor started for a non-Text template, this shouldn't happen!");
		}
		
		// Get the template annotations.
		ArrayList<TemplateAnnotation> templateAnnotations = getTemplateAnnotations(rawTemplateContent, _config.getTemplateConfig(), 0, rawTemplateContent.length());
		
		// Return the pre-processed template.
		return new PreprocessedTemplate(rawTemplateContent, templateAnnotations);
	}
	
	/**
	 * Get the template annotations found in templateContent between beginIndex and endIndex.
	 * @param templateContent The template content.
	 * @param templateConfig The template config.
	 * @param beginIndex The begin index to start searching in the templateContent.
	 * @param endIndex The end index to stop searching in the templateContent.
	 * @return The list with found annotations.
	 * @throws TemplatePreprocessorException
	 */
	public static ArrayList<TemplateAnnotation> getTemplateAnnotations(String templateContent, AbstractTemplateConfig templateConfig, int beginIndex, int endIndex) throws TemplatePreprocessorException {
	
		// Find the annotations in the template and add them to the templateAnnotations set.
		ArrayList<TemplateAnnotation> templateAnnotations = AnnotationScanner.collectTextAnnotations(templateContent, templateConfig.getFileFormatConfig(), beginIndex, endIndex);
	
		// Add all template section annotations from the config file.
		if (templateConfig.getSectionAnnotations() != null
				&& templateConfig.getSectionAnnotations().size() > 0)
			templateAnnotations.addAll(templateConfig.getSectionAnnotations());
		
		// Loop through the section annotations to find the section begin index.
		for (TemplateSectionAnnotation sectionAnnotation : templateAnnotations.stream().filter(sa -> sa instanceof TemplateSectionAnnotation).toArray(TemplateSectionAnnotation[]::new)) {

			// Initialize an int for storing the begin index.
			int sectionBeginCharIndex;
			// Store whether the annotation was found in the template.
			boolean annotationInTemplate = (sectionAnnotation.getAnnotationEndIndex() != -1);
			
			// If the begin is specified, we use begin.
			if (sectionAnnotation.getBegin() != null && sectionAnnotation.getBegin().length() > 0) {
				// If the annotation was found in the template we start scanning from the position the annotation ends.
				if (annotationInTemplate)
					sectionBeginCharIndex = templateContent.indexOf(sectionAnnotation.getBegin(), sectionAnnotation.getAnnotationEndIndex());
				// Otherwise we scan from the beginning of the template.
				else
					sectionBeginCharIndex = templateContent.indexOf(sectionAnnotation.getBegin());
				
				// If the result is -1 or larger then the endIndex, then the begin wasn't found.
				if (sectionBeginCharIndex == -1 || sectionBeginCharIndex > endIndex)
					throw new TemplatePreprocessorException(String.format("The begin part of the section can't be found (%s)", sectionAnnotation.getName()));

				// If the section does not need to include the begin, than add the length of the begin to remove begin characters from section.
				if (!sectionAnnotation.isIncludeBegin())
					sectionBeginCharIndex += sectionAnnotation.getBegin().length();
			}
			// If literalOnFirstLine is specified, we use literalOnFirstLine
			else if (sectionAnnotation.getLiteralOnFirstLine() != null && sectionAnnotation.getLiteralOnFirstLine().length() > 0) {
				// Construct a regex for entire line containing the literal (* matches everything but line terminator)
				 Pattern pattern = Pattern.compile(String.format(".*%s.*", Pattern.quote(sectionAnnotation.getLiteralOnFirstLine())));
				 Matcher matcher = pattern.matcher(templateContent);
						 
				 // If annotation is in template, apply regex on raw template, after annotation, otherwise apply on complete raw template
				 if (annotationInTemplate) {
					 matcher.region(sectionAnnotation.getAnnotationEndIndex(), endIndex);
				 }
				 else {
					 matcher.region(beginIndex, endIndex);
				 }
				 
				 // If pattern is found, section begin charindex is the start of the matching line.
				 if (matcher.find()) {
					sectionBeginCharIndex = matcher.start();
				 } 
				 else {
					throw new TemplatePreprocessorException(String.format("The begin part of the section can't be found (%s)", sectionAnnotation.getName()));
				}
			}
			// If begin is not specified and the annotation was specified in the template, we use the end position of the annotation.
			else if (annotationInTemplate) {
				// Set the section begin index to the end index of the annotation.
				sectionBeginCharIndex = sectionAnnotation.getAnnotationEndIndex();
			}
			// Otherwise, we can't get the begin location.
			else {
				throw new TemplatePreprocessorException(String.format("No begin of section defined (%s)", sectionAnnotation.getName()));
			}
			
			// Store the begin index in a new template bounds annotation object.
			logger.info(String.format("Found section bounds for '%s', begin index: %d", sectionAnnotation.getName(), sectionBeginCharIndex));
			templateAnnotations.add(new TemplateSectionBoundsAnnotation(sectionAnnotation, sectionBeginCharIndex));
		}

		// Return the list of template annotations.
		return templateAnnotations;
	}
}
