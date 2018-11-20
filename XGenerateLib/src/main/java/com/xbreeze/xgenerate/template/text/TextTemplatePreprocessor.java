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
import com.xbreeze.xgenerate.template.annotation.TemplateSectionBoundsAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation;
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
	protected PreprocessedTemplate getPreprocessedTemplate(RawTemplate rawTemplate, String rootSectionName) throws TemplatePreprocessorException {
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
		
		// Create a Text Template section annotation for the root section (implicit).
		TemplateTextSectionAnnotation ttsa = new TemplateTextSectionAnnotation(rootSectionName);
		// Create the root template section bounds.
		TemplateSectionBoundsAnnotation tsba = new TemplateSectionBoundsAnnotation(ttsa, 0, rawTemplateContent.length());
		
		// Return the pre-processed template.
		return new PreprocessedTemplate(rawTemplateContent, tsba, templateAnnotations);
	}
	
	/**
	 * Get the template annotations found in templateContent between beginIndex and endIndex.
	 * @param rawTemplateContent The template content.
	 * @param templateConfig The template config.
	 * @param beginIndex The begin index to start searching in the templateContent.
	 * @param endIndex The end index to stop searching in the templateContent.
	 * @return The list with found annotations.
	 * @throws TemplatePreprocessorException
	 */
	public static ArrayList<TemplateAnnotation> getTemplateAnnotations(String rawTemplateContent, AbstractTemplateConfig templateConfig, int beginIndex, int endIndex) throws TemplatePreprocessorException {
	
		// Find the annotations in the template and add them to the templateAnnotations set.
		ArrayList<TemplateAnnotation> templateAnnotations = AnnotationScanner.collectTextAnnotations(rawTemplateContent, templateConfig.getFileFormatConfig(), beginIndex, endIndex);
	
		// Add all template section annotations from the config file.
		if (templateConfig.getSectionAnnotations() != null
				&& templateConfig.getSectionAnnotations().size() > 0)
			templateAnnotations.addAll(templateConfig.getSectionAnnotations());
		
		// Loop through the text section annotations to find the section begin index.
		for (TemplateTextSectionAnnotation textSectionAnnotation : templateAnnotations.stream().filter(sa -> sa instanceof TemplateTextSectionAnnotation).toArray(TemplateTextSectionAnnotation[]::new)) {

			// Get the section begin index.
			int sectionBeginCharIndex = findSectionBeginIndex(textSectionAnnotation, rawTemplateContent, beginIndex, endIndex);
			// If the begin index isn't found yet, and the annotation was defined in the template, throw an exception.
			if (sectionBeginCharIndex == -1 && textSectionAnnotation.isDefinedInTemplate())
				throw new TemplatePreprocessorException(String.format("The begin of the section '%s' can't be found", textSectionAnnotation.getName()));
			
			int sectionEndCharIndex = -1;
			//Look for end index but only if begin was found
			if (sectionBeginCharIndex != -1)
				sectionEndCharIndex = findSectionEndIndex(textSectionAnnotation, rawTemplateContent, sectionBeginCharIndex, endIndex);

			//If the end index isn't found yet, and the annotation was defined in the template, throw an exception.
			if (sectionEndCharIndex == -1 && textSectionAnnotation.isDefinedInTemplate())
				throw new TemplatePreprocessorException(String.format("The end of the section '%s' can't be found", textSectionAnnotation.getName()));
			
			// If begin and end charIndex where found, store the begin index in a new template bounds annotation object.
			if (sectionBeginCharIndex != -1 && sectionEndCharIndex != -1) {
				logger.info(String.format("Found section bounds for '%s' (begin: %d; end: %d", textSectionAnnotation.getName(), sectionBeginCharIndex, sectionEndCharIndex));
				templateAnnotations.add(new TemplateSectionBoundsAnnotation(textSectionAnnotation, sectionBeginCharIndex, sectionEndCharIndex));
			}
			else {
				logger.info(String.format("Either begin (%d) or end (%d) of section %s defined in config is not found, ignoring section definition", sectionBeginCharIndex, sectionEndCharIndex, textSectionAnnotation.getName()));
			}
		}

		// Return the list of template annotations.
		return templateAnnotations;
	}
	
	/**
	 * Get the section begin index.
	 * @param textSectionAnnotation The section annotation to search the begin for.
	 * @param rawTemplateContent The template content.
	 * @param searchBeginIndex The begin index the search from.
	 * @param searchEndIndex The end index to search till.
	 * @return The section begin index if found, otherwise -1.
	 * @throws TemplatePreprocessorException
	 */
	private static int findSectionBeginIndex(TemplateTextSectionAnnotation textSectionAnnotation, String rawTemplateContent, int searchBeginIndex, int searchEndIndex) throws TemplatePreprocessorException {
		// Initialize an int for storing the begin index.
		int sectionBeginCharIndex = -1;
		// Store whether the annotation was found in the template.
		boolean annotationInTemplate = (textSectionAnnotation.getAnnotationEndIndex() != -1);
		
		// If the begin is specified, we use begin.
		if (textSectionAnnotation.getBegin() != null && textSectionAnnotation.getBegin().length() > 0) {
			// If the annotation was found in the template we start scanning from the position the annotation ends.
			if (annotationInTemplate)
				sectionBeginCharIndex = rawTemplateContent.indexOf(textSectionAnnotation.getBegin(), textSectionAnnotation.getAnnotationEndIndex());
			// Otherwise we scan from the beginning of the template, specified by beginIndex
			else
				sectionBeginCharIndex = rawTemplateContent.indexOf(textSectionAnnotation.getBegin(), searchBeginIndex);
			
			// If the result is -1 or larger then the endIndex, then the begin wasn't found.
			if (sectionBeginCharIndex == -1 || sectionBeginCharIndex > searchEndIndex) {
				String msg = String.format("The begin part of the section '%s' can't be found (begin: '%s')", textSectionAnnotation.getName(), textSectionAnnotation.getBegin());
				// If the section was defined in the template, throw an exception, otherwise log an informational message and return -1
				if (textSectionAnnotation.isDefinedInTemplate())
					throw new TemplatePreprocessorException(msg);
				else {
					logger.info(msg);
					return -1;
				}
			}
			else
			// If the section does not need to include the begin, than add the length of the begin to remove begin characters from section.
			if (!textSectionAnnotation.isIncludeBegin())
				sectionBeginCharIndex += textSectionAnnotation.getBegin().length();
		}
		// If literalOnFirstLine is specified, we use literalOnFirstLine
		else if (textSectionAnnotation.getLiteralOnFirstLine() != null && textSectionAnnotation.getLiteralOnFirstLine().length() > 0) {
			// Construct a regex for entire line containing the literal (* matches everything but line terminator)
			 Pattern pattern = Pattern.compile(String.format(".*%s.*", Pattern.quote(textSectionAnnotation.getLiteralOnFirstLine())));
			 Matcher matcher = pattern.matcher(rawTemplateContent);
					 
			 // If annotation is in template, apply regex on raw template, after annotation, otherwise apply on complete raw template
			 if (annotationInTemplate) {
				 matcher.region(textSectionAnnotation.getAnnotationEndIndex(), searchEndIndex);
			 }
			 else {
				 matcher.region(searchBeginIndex, searchEndIndex);
			 }
			 
			 // If pattern is found, section begin charindex is the start of the matching line.
			 if (matcher.find()) {
				sectionBeginCharIndex = matcher.start();
			 } 
			 else {
				 //If pattern is not found then either throw an exception (when section was defined in template, otherwise log info message and return -1
				 String msg = String.format("The literal on first line of the section '%s' can't be found (literalOnFirstLine: '%s')", textSectionAnnotation.getName(), textSectionAnnotation.getLiteralOnFirstLine());
				 if (textSectionAnnotation.isDefinedInTemplate())
					 throw new TemplatePreprocessorException(msg);
				 else {
					 logger.info(msg);
					 return -1;
				 }
			}
		}
		// If begin is not specified and the annotation was specified in the template, we use the end position of the annotation.
		else if (annotationInTemplate) {
			// Set the section begin index to the end index of the annotation.
			sectionBeginCharIndex = textSectionAnnotation.getAnnotationEndIndex();
		}
		
		// Return the sectionBeginCharIndex.
		return sectionBeginCharIndex;
	}
	
	/**
	 * Find the section end index.
	 * @param textSectionAnnotation The TextTemplateSectionAnnotation
	 * @param rawTemplateContent The raw template content.
	 * @param searchBeginIndex The index to start searching for the end from.
	 * @return The position of the end if found, otherwise -1.
	 * @throws TemplatePreprocessorException
	 */
	private static int findSectionEndIndex(TemplateTextSectionAnnotation textSectionAnnotation, String rawTemplateContent, int searchBeginIndex, int searchEndIndex) throws TemplatePreprocessorException {
		// The variable to return at the end, if the end is not found this function will return -1.
		int sectionEndCharIndex = -1;
		
		// If the end is specified, we use end.
		if (textSectionAnnotation.getEnd() != null && textSectionAnnotation.getEnd().length() > 0) {
			// Get the position of the end tag of the section.
			sectionEndCharIndex = rawTemplateContent.indexOf(textSectionAnnotation.getEnd(), searchBeginIndex);
			
			// Check whether the end was found in the range, if not return -1.
			if (sectionEndCharIndex == -1)
				return sectionEndCharIndex;
			
			// If the section includes the end, we add the length of the end to the index.
			if (textSectionAnnotation.isIncludeEnd())
				sectionEndCharIndex += textSectionAnnotation.getEnd().length();
		}
		// literalOnLastLine
		else if (textSectionAnnotation.getLiteralOnLastLine() != null && textSectionAnnotation.getLiteralOnLastLine().length() > 0) {
		    Pattern pattern = Pattern.compile(String.format("%s.*\\r?\\n?", Pattern.quote(textSectionAnnotation.getLiteralOnLastLine())));
		    Matcher matcher = pattern.matcher(rawTemplateContent);
		    // Set the region to search in.
		    matcher.region(searchBeginIndex, searchEndIndex);
			if (matcher.find()) {
				return matcher.end();
			} else {
				return -1;
			}
		}
		// If end was not specified, check nrOfLines, this has a default value of 1 if not explicitly set.	
		else  {
			// Get the nrOfLines from the annotation.
			Integer sectionNrOfLines = textSectionAnnotation.getNrOfLines();
			
			// Loop through the newlines as of the start of the section.
			for (int currentNrOfLines = 0; currentNrOfLines < sectionNrOfLines; currentNrOfLines++) {
				// The end of the section is the first newline we encounter after the begin of the section. We include the newline in the section.
				sectionEndCharIndex = rawTemplateContent.indexOf('\n', (sectionEndCharIndex == -1) ? searchBeginIndex : sectionEndCharIndex + 1);
				
				// If the end of line wasn't found, break out of the loop.
				if (sectionEndCharIndex == -1)
					break;
			}
			
			// Add the 1 to the length to compensate for the \n character.
			if (sectionEndCharIndex != -1)
				sectionEndCharIndex += 1;
		}
		// Otherwise, we can't get the end location, leave the return value at the initial -1.
		
		// If the found index is out of range, return -1.
		if (sectionEndCharIndex > searchEndIndex)
			return -1;
		
		// If the find index is before the start index, throw an exception since this shouldn't happen.
		if (sectionEndCharIndex != -1 && sectionEndCharIndex < searchBeginIndex)
			throw new TemplatePreprocessorException(String.format("The found section end index %d is lower than the starting index %d, this shouldn't happen!", sectionEndCharIndex, searchBeginIndex));
		
		// Otherwise return the found index.
		return sectionEndCharIndex;
	}
}
