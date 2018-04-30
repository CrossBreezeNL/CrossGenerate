package com.xbreeze.xgenerate.template.text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.FileFormatConfig;
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
		// Store the file format config into a local variable.
		FileFormatConfig fileFormatConfig = _config.getTemplateConfig().getFileFormatConfig();
		
		// Find the annotations in the template and add them to the templateAnnotations set.
		ArrayList<TemplateAnnotation> templateAnnotations = AnnotationScanner.collectTextAnnotations(rawTemplateContent, fileFormatConfig);
	
		// Add all template section annotations from the config file.
		if (_config.getTemplateConfig() != null
				&& _config.getTemplateConfig().getSectionAnnotations() != null
				&& _config.getTemplateConfig().getSectionAnnotations().size() > 0)
			templateAnnotations.addAll(_config.getTemplateConfig().getSectionAnnotations());
		
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
					sectionBeginCharIndex = rawTemplateContent.indexOf(sectionAnnotation.getBegin(), sectionAnnotation.getAnnotationEndIndex());
				// Otherwise we scan from the beginning of the template.
				else
					sectionBeginCharIndex = rawTemplateContent.indexOf(sectionAnnotation.getBegin());
				
				// If the result is -1, then the begin wasn't found.
				if (sectionBeginCharIndex == -1)
					throw new TemplatePreprocessorException(String.format("The begin part of the section can't be found (%s)", sectionAnnotation.getName()));

				// If the section does not need to include the begin, than add the length of the begin to remove begin characters from section.
				if (!sectionAnnotation.isIncludeBegin())
					sectionBeginCharIndex += sectionAnnotation.getBegin().length();
			}
			// If literalOnFirstLine is specified, we use literalOnFirstLine
			else if (sectionAnnotation.getLiteralOnFirstLine() != null && sectionAnnotation.getLiteralOnFirstLine().length() > 0) {
				// Construct a regex for entire line containing the literal (* matches everything but line terminator)
				 Pattern pattern = Pattern.compile(String.format(".*%s.*", Pattern.quote(sectionAnnotation.getLiteralOnFirstLine())));
				 Matcher matcher = null;
						 
				 // If annotation is in template, apply regex on raw template, after annotation, otherwise apply on complete raw template
				 if (annotationInTemplate) {
					 matcher = pattern.matcher(rawTemplateContent.substring(sectionAnnotation.getAnnotationEndIndex()));
				 }
				 else {
					 matcher = pattern.matcher(rawTemplateContent);
				 }
				 
				 // If pattern is found, section begin charindex is the start of the matching line.
				 if (matcher.find()) {
					sectionBeginCharIndex = matcher.start();
					// If annotation was specified in the template, ensure we add the annotation end index to the start position found
					if (annotationInTemplate) {
						sectionBeginCharIndex += sectionAnnotation.getAnnotationEndIndex();
					}
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
		
		// Return the pre-processed template.
		return new PreprocessedTemplate(rawTemplateContent, templateAnnotations);
	}
}
