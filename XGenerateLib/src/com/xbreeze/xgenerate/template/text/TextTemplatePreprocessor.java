package com.xbreeze.xgenerate.template.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.template.FileFormatConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.RawTemplate;
import com.xbreeze.xgenerate.template.SectionedTemplate;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateCommentAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionBoundsAnnotation;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationException;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationParamException;
import com.xbreeze.xgenerate.template.scanner.AnnotationScanner;
import com.xbreeze.xgenerate.template.section.CommentTemplateSection;
import com.xbreeze.xgenerate.template.section.NamedTemplateSection;
import com.xbreeze.xgenerate.template.section.RawTemplateSection;

public class TextTemplatePreprocessor extends TemplatePreprocessor {
	/**
	 * Constructor.
	 * @param templateConfig The template config.
	 */
	public TextTemplatePreprocessor(XGenConfig config) {
		super(config);
	}

	/**
	 * Sectionize the text template and return the SectionedTemplate.
	 * @throws UnknownAnnotationException 
	 * @throws TemplatePreprocessorException 
	 * @throws UnknownAnnotationParamException 
	 */
	@Override
	protected SectionedTemplate sectionizeTemplate(RawTemplate rawTemplate, String rootSectionName) throws TemplatePreprocessorException, UnhandledException {
		// Store the raw template content into a local variable.
		String rawTemplateContent = rawTemplate.getRawTemplateContent();
		// Store the file format config into a local variable.
		FileFormatConfig fileFormatConfig = _config.getTemplateConfig().getFileFormatConfig();
		
		// Find the annotations in the template and add the to the templateAnnotations set.
		ArrayList<TemplateAnnotation> templateAnnotations = AnnotationScanner.collectAnnotations(rawTemplateContent, fileFormatConfig.getSingleLineCommentPrefix(), fileFormatConfig.getAnnotationPrefix(), fileFormatConfig.getAnnotationArgsPrefix(), fileFormatConfig.getAnnotationArgsSuffix(), "");	
	
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

				// If the section includes the begin, than add the length of the begin.
				if (!sectionAnnotation.isIncludeBegin())
					sectionBeginCharIndex += sectionAnnotation.getBegin().length();
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
		
		// Now the section begin indexes are found, we sort the collection based on the section begin index for sections and annotation index for other annotations.
		// This makes the order of set according to where section should be created in the preprocessed template.
		Collections.sort(templateAnnotations);

		// Loop through the template annotations and create the appropriate sections.
		// Initialize previous match end on -1, since it will +1 to get the new section start index.
		{
			// Initialize the sectionized template.
			SectionizedTextTemplate sectionizedTextTemplate = new SectionizedTextTemplate(rootSectionName);
			
			// We use the ListIterator here since we can go forward and backward.
			ListIterator<TemplateAnnotation> taIterator = templateAnnotations.listIterator();
			
			// Create an implicit TemplateSectionAnnotation for the root of the template.
			TemplateSectionAnnotation rootSectionAnnotation = new TemplateSectionAnnotation();
			// Set the name of the root section.
			rootSectionAnnotation.setName(_config.getTemplateConfig().getRootSectionName());
			// Create an implicit template section bounds annotation with the begin index on 0.
			TemplateSectionBoundsAnnotation rootSectionBoundsAnnotation = new TemplateSectionBoundsAnnotation(rootSectionAnnotation, 0);
			
			// Process the content of the section.
			// Pass in 0 for parentPreviousSectionEndIndex.
			processNamedTemplateSection(rootSectionBoundsAnnotation, sectionizedTextTemplate, rawTemplateContent, taIterator, 0, true);
			
			// Return the SectionizedTextTemplate;
			return sectionizedTextTemplate;
		}
	}
	
	/**
	 * Process the named template section.
	 * @param namedTemplateSection
	 * @param rawTemplateContent
	 * @throws TemplatePreprocessorException 
	 */
	private int processNamedTemplateSection(TemplateSectionBoundsAnnotation parentSectionBounds, NamedTemplateSection parentTemplateSection, String rawTemplateContent, ListIterator<TemplateAnnotation> taIterator, int parentPreviousSectionEndIndex, boolean isRootSection) throws TemplatePreprocessorException {
		logger.info(String.format("processNamedTemplateSection called for section '%s', parentPreviousSectionEndIndex=%d", parentTemplateSection.getSectionName(), parentPreviousSectionEndIndex));
		// Loop through the template annotations.
		int previousSectionEndIndex = parentPreviousSectionEndIndex; 
		
		// Loop through the annotations.
		while (taIterator.hasNext()) {
			// Store the next annotation in a local variable (also moves the cursor one forward).
			TemplateAnnotation templateAnnotation = taIterator.next();
			
			// If the templateAnnotation start index is not the next number after the previous section end, we add a RawTemplateSection to the parent NamedTemplateSection.
			// Let's check whether there is some template section which is after the last section and before the next (a raw template section).
			int expectedSectionBeginIndex = previousSectionEndIndex;
			if (templateAnnotation.getAnnotationBeginIndex() > expectedSectionBeginIndex) {
				int parentSectionEndIndex = -1;
				// Only check for end of section when it is not the root section.
				if (!isRootSection) {
					// Before we add the raw template, we first check whether the end of the section is in this part of raw template.
					// If so we set the end index of this section, add the template up till that part as raw template, set the iterator one back and return the end index (so the parent will pickup the remaining annotations).
					logger.info(String.format("Searching for section end index for '%s' between index %d and %d", parentTemplateSection.getSectionName(), expectedSectionBeginIndex, templateAnnotation.getAnnotationBeginIndex()));
					parentSectionEndIndex = findSectionEndIndex(parentSectionBounds, rawTemplateContent, expectedSectionBeginIndex, templateAnnotation.getAnnotationBeginIndex());
				}
				// Check whether the end index is found (-1 means not found),
				if (parentSectionEndIndex != -1) {
					// Store the section end index on the parent annotation.
					parentSectionBounds.setAnnotationEndIndex(parentSectionEndIndex);
					parentTemplateSection.setSectionEndIndex(parentSectionEndIndex);
					logger.info(String.format("Successfully found begin and end position of section (%s -> %d:%d)", parentTemplateSection.getSectionName(), parentTemplateSection.getSectionBeginIndex(), parentTemplateSection.getSectionEndIndex()));
					
					// If the parent section end index is later then the expected beginning of the next section we create a raw template with the part between.
					if (parentSectionEndIndex > expectedSectionBeginIndex) {
						addRawTemplate(parentTemplateSection, rawTemplateContent, expectedSectionBeginIndex, parentSectionEndIndex);
					}
					
					// Move the cursor on the iterator one back so the parent will handle the current annotation.
					taIterator.previous();
					
					// Return the parent section end index as the previousSectionEndIndex and let the parent section handle the rest of the annotations (since this one is closed).
					return parentSectionEndIndex;
				// The end of section wasn't found, so all before the new section is raw template of the parent.
				} else {
					addRawTemplate(parentTemplateSection, rawTemplateContent, expectedSectionBeginIndex, templateAnnotation.getAnnotationBeginIndex());
					// Set the end index to the end of the raw template.
					previousSectionEndIndex = templateAnnotation.getAnnotationBeginIndex();
				}
			}
			
			// Based on the type of annotation we can have different actions.
			// For example when it is a TemplateCommentAnnotation, we just add it to the current NamedTemplate.
			// Here we go through the annotation types one by one.
			
			// TemplateCommentAnnotation
			if (templateAnnotation instanceof TemplateCommentAnnotation) {
				// Add a CommentTemplateSection for each TemplateCommentAnnotation.
				TemplateCommentAnnotation templateCommentAnnotation = ((TemplateCommentAnnotation) templateAnnotation);
				parentTemplateSection.addTemplateSection(new CommentTemplateSection(templateCommentAnnotation.getComment(), templateCommentAnnotation.getAnnotationBeginIndex(), templateCommentAnnotation.getAnnotationEndIndex()));
				logger.info(String.format("Added CommentTemplateSection to SectionizedTextTemplate (%d:%d)", templateCommentAnnotation.getAnnotationBeginIndex(), templateCommentAnnotation.getAnnotationEndIndex()));
			}
			
			else if (templateAnnotation instanceof TemplateSectionAnnotation) {
				// If it is a template section annotation we skip this section.
				// It's not represented in the preprocessed template.
			}
			
			// TemplateSectionAnnotation
			else if (templateAnnotation instanceof TemplateSectionBoundsAnnotation) {
				// Add a NamedTemplateSection for each TemplateSectionAnnotation.
				TemplateSectionBoundsAnnotation templateSectionBoundsAnnotation = (TemplateSectionBoundsAnnotation) templateAnnotation;
				logger.info(String.format("Start of processing NamedTemplateSection %s", templateSectionBoundsAnnotation.getName()));
				// Create the named template section.
				NamedTemplateSection namedTemplateSection = new NamedTemplateSection(templateSectionBoundsAnnotation.getName(), templateSectionBoundsAnnotation.getAnnotationBeginIndex());
				// Process the content of the named template (recursively).
				// This process will return the end index of the section (or throw an exception if not found).
				processNamedTemplateSection(templateSectionBoundsAnnotation, namedTemplateSection, rawTemplateContent, taIterator, previousSectionEndIndex, false);
				// Add the named template section to the sectionized template.
				parentTemplateSection.addTemplateSection(namedTemplateSection);
				logger.info(String.format("Added NamedTemplateSection to SectionizedTextTemplate (%s -> %d:%d)", namedTemplateSection.getSectionName(), namedTemplateSection.getSectionBeginIndex(), namedTemplateSection.getSectionEndIndex()));
			}
			
			// If there is some other annotation found we didn't handle, let's throw an exception.
			else {
				throw new TemplatePreprocessorException(String.format("Unhandled annotation found: %s", templateAnnotation.getAnnotationName()));
			}
			
			// Set the ending index of the current section for the next cycle.
			previousSectionEndIndex = templateAnnotation.getAnnotationEndIndex();
		}
		
		// After the last annotation match there might still be some raw template. If so check for the section end and add a raw template section.
		int rawTemplateContentEndIndex = rawTemplateContent.length();
		if (previousSectionEndIndex < rawTemplateContentEndIndex) {
			// TODO: For later: This code overlaps with the code at the beginning of the function. Can we somehow re-use it? Maybe not iterating on while, but on index and do an extra loop.
			int parentSectionEndIndex = -1;
			// Only check for end of section when it is not the root section.
			if (!isRootSection) {
				// Search for the section end index
				logger.info(String.format("Searching for section end index for '%s' from index %d", parentTemplateSection.getSectionName(), previousSectionEndIndex));
				parentSectionEndIndex = findSectionEndIndex(parentSectionBounds, rawTemplateContent, previousSectionEndIndex, rawTemplateContent.length());
			}
			if (parentSectionEndIndex != -1) {
				// Store the section end index on the parent annotation.
				parentSectionBounds.setAnnotationEndIndex(parentSectionEndIndex);
				parentTemplateSection.setSectionEndIndex(parentSectionEndIndex);
				logger.info(String.format("Successfully found begin and end position of section (%s -> %d:%d)", parentSectionBounds.getName(), parentSectionBounds.getAnnotationBeginIndex(), parentSectionBounds.getAnnotationEndIndex()));
				
				// If the parent section end index is later then the expected beginning of the next section we create a raw template with the part between.
				if (parentSectionEndIndex > previousSectionEndIndex) {
					addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, parentSectionEndIndex);
				}
				
				// Return the parent section end index as the previousSectionEndIndex and let the parent section handle the rest of the template (since this one is closed).
				return parentSectionEndIndex;
			} else {
				// When there is still some raw template after the last annotation, add it to the sectionized template.
				addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, rawTemplateContentEndIndex);
			}
		}
		
		// If the current section is not the root section the end of the section should've been found by now.
		// So we throw and exception.
		if (!isRootSection && previousSectionEndIndex < rawTemplateContentEndIndex) {
			throw new TemplatePreprocessorException(String.format("The end of section '%s' can't be found!", parentTemplateSection.getSectionName()));
		}
		
		// If we reached this point, we reached the end of the template, so we return the end index of the template.
		// This can only happen for the root section.
		parentTemplateSection.setSectionEndIndex(rawTemplateContentEndIndex);
		return rawTemplateContentEndIndex;
	}
	
	/**
	 * Find the section end index.
	 * @param sectionAnnotationBounds The TemplateSectionBoundsAnnotation
	 * @param rawTemplateContent The raw template content.
	 * @param sectionEndSearchBeginIndex The index to start searching for the end from.
	 * @return The position of the end if found, otherwise -1.
	 * @throws TemplatePreprocessorException
	 */
	private int findSectionEndIndex(TemplateSectionBoundsAnnotation sectionAnnotationBounds, String rawTemplateContent, int sectionEndSearchBeginIndex, int sectionEndSearchEndIndex) throws TemplatePreprocessorException {
		// The variable to return at the end, if the end is not found this function will return -1.
		int sectionEndCharIndex = -1;
		
		TemplateSectionAnnotation templateSectionAnnotation = sectionAnnotationBounds.getTemplateSectionAnnotation();
		
		// If the end is specified, we use end.
		if (templateSectionAnnotation.getEnd() != null && templateSectionAnnotation.getEnd().length() > 0) {
			// Get the position of the end tag of the section.
			sectionEndCharIndex = rawTemplateContent.indexOf(templateSectionAnnotation.getEnd(), sectionEndSearchBeginIndex);
			
			// Check whether the end was found in the range, if not return -1.
			if (sectionEndCharIndex == -1)
				return sectionEndCharIndex;
			
			// If the section includes the end, we add the length of the end to the index.
			if (templateSectionAnnotation.isIncludeEnd())
				sectionEndCharIndex += templateSectionAnnotation.getEnd().length();
		}
		// placeholderOnLastLine
		if (templateSectionAnnotation.getPlaceholderOnLastLine() != null && templateSectionAnnotation.getPlaceholderOnLastLine().length() > 0) {
		    Pattern pattern = Pattern.compile(String.format("%s.*\\r?\\n?", Pattern.quote(templateSectionAnnotation.getPlaceholderOnLastLine())));
		    Matcher matcher = pattern.matcher(rawTemplateContent);
			if (matcher.find(sectionEndSearchBeginIndex) && matcher.end() <= sectionEndSearchEndIndex) {
				return matcher.end();
			} else {
				return -1;
			}
		}
		// If end was not specified, and the annotation was specified in the template the nrOfLines=1 by default.
		else if (templateSectionAnnotation.isDefinedInTemplate()) {
			// The end of the section is the first newline we encounter after the begin of the section. We include the newline in the section.
			sectionEndCharIndex = rawTemplateContent.indexOf('\n', sectionEndSearchBeginIndex);
			
			if (sectionEndCharIndex == -1)
				return -1;
			
			// Add the 1 to the length to compensate for the \n character.
			sectionEndCharIndex += 1;
		}
		// Otherwise, we can't get the end location.
		else {
			throw new TemplatePreprocessorException(String.format("No end of section defined (%s)", sectionAnnotationBounds.getName()));
		}
		
		// If the found index is out of range, return -1.
		if (sectionEndCharIndex > sectionEndSearchEndIndex)
			return -1;
		
		// Otherwise return the found index.
		return sectionEndCharIndex;
	}
	
	/**
	 * Add a raw template to the NamedTemplateSection
	 * @param parentTemplateSection The parent NamedTemplateSection
	 * @param rawTemplateContent The raw template content
	 * @param startIndex The starting index of the raw template.
	 * @param endIndex The ending index of the raw template.
	 */
	private void addRawTemplate(NamedTemplateSection parentTemplateSection, String rawTemplateContent, int startIndex, int endIndex) {
		String rawTemplateSectionContent = rawTemplateContent.substring(startIndex, endIndex);
		logger.info(String.format("Found a raw template section in section '%s' between index %d and %d: '%s'", parentTemplateSection.getSectionName(), startIndex, endIndex, rawTemplateSectionContent));
		parentTemplateSection.addTemplateSection(new RawTemplateSection(rawTemplateSectionContent, startIndex, endIndex));
	}
}
