package com.xbreeze.xgenerate.template;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.binding.SectionModelBindingConfig;
import com.xbreeze.xgenerate.config.template.TemplateConfig;
import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateCommentAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionBoundsAnnotation;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationException;
import com.xbreeze.xgenerate.template.section.CommentTemplateSection;
import com.xbreeze.xgenerate.template.section.NamedTemplateSection;
import com.xbreeze.xgenerate.template.section.RawTemplateSection;
import com.xbreeze.xgenerate.template.section.SectionedTemplate;
import com.xbreeze.xgenerate.template.xml.XMLUtils;

public abstract class TemplatePreprocessor {
	// The logger for this class.
	protected static final Logger logger = Logger.getLogger(TemplatePreprocessor.class.getName());
	
	/**
	 * The template config to use for pre-processing.
	 */
	protected XGenConfig _config;
	
	/**
	 * Constructor.
	 * Only to be used by child classes.
	 * @param rawTemplate The raw template.
	 * @param config The XGenConfig.
	 */
	public TemplatePreprocessor(XGenConfig config) {
		this._config = config;
	}
	
	/**
	 * Procedure which each specific implementation of the TemplateSectionizer needs to implement to get to the generic SectionizedTemplate.
	 * In this procedure the raw template needs to be split in sections with their respective content.
	 */
	protected abstract SectionedTemplate sectionizeTemplate(RawTemplate rawTemplate, String rootSectionName)  throws TemplatePreprocessorException, UnhandledException;
	
	/**
	 * Perform the pre-processing to get to the pre-processed template.
	 * @return The pre-processed template.
	 * @throws UnhandledException 
	 * @throws UnknownAnnotationException 
	 */
	public PreprocessedTemplate preProcess(RawTemplate rawTemplate, URI outputFileUri) throws TemplatePreprocessorException, UnhandledException {
		TemplateConfig templateConfig = _config.getTemplateConfig();
		
		// Perform the specific sectionizing for the current template.
		// This should detect sections from the raw template and transform it into a SectionedTemplate object.
		String rootSectionName = templateConfig.getRootSectionName();
		
		SectionModelBindingConfig[] rootSectionModelBindings = null;
		if (_config.getBindingConfig() != null)
			rootSectionModelBindings = _config.getBindingConfig().getSectionModelBindingConfigs(rootSectionName);
		
		// Check whether there is only 1 root section model binding. If not, throw an exception.
		if (rootSectionModelBindings == null || rootSectionModelBindings.length != 1) {
			throw new TemplatePreprocessorException("There must and can only be 1 section model binding for the root section.");
		}
		
		// Assign the section model binding for the root section to a local variable.
		SectionModelBindingConfig rootSectionModelBinding = rootSectionModelBindings[0];
		
		// Sectionize the template.
		SectionedTemplate sectionizedTemplate = this.sectionizeTemplate(rawTemplate, rootSectionName);
		
		// Now the templates are pre-processed by their specific preprocessor, we can perform the generic pre-processing here.
		// TODO: Put in right output folder.
		PreprocessedTemplate preprocessedTemplate = new PreprocessedTemplate(rawTemplate.getRawTemplateFileName(), rawTemplate.getRawTemplateFileLocation(), templateConfig, outputFileUri, rootSectionModelBinding);
		
		// Append the Xslt from the section to the pre-processed template.
		sectionizedTemplate.appendTemplateXslt(preprocessedTemplate, _config, rootSectionModelBinding);
		
		// Finalize the template before returning it.
		preprocessedTemplate.finalizeTemplate();
		
		// Return the pre-processed template.
		return preprocessedTemplate;
	}

	/**
	 * @param config the XGenConfig to set
	 */
	public void setConfig(XGenConfig config) {
		this._config = config;
	}
	
	/**
	 * Sectionize the template after its annotations and configurations are processed into a list of annotations.
	 * @param preprocessedTemplate The preprocessed template.
	 * @param rootSectionName The root section name.
	 * @param templateAnnotations The template annotations.
	 * @return The sectionized template.
	 * @throws TemplatePreprocessorException
	 */
	protected SectionedTemplate sectionizeTemplate(String preprocessedTemplate, String rootSectionName, ArrayList<TemplateAnnotation> templateAnnotations) throws TemplatePreprocessorException {
		// Now the section begin indexes are found, we sort the collection based on the section begin index for sections and annotation index for other annotations.
		// This makes the order of set according to where section should be created in the preprocessed template.
		Collections.sort(templateAnnotations);

		// Loop through the template annotations and create the appropriate sections.
		{
			// Initialize the sectionized template.
			SectionedTemplate sectionizedTextTemplate = new SectionedTemplate(rootSectionName);
			
			// We use the ListIterator here since we can go forward and backward.
			ListIterator<TemplateAnnotation> taIterator = templateAnnotations.listIterator();
			
			// Create an implicit template section bounds annotation with the begin index on 0.
			TemplateSectionBoundsAnnotation rootSectionBoundsAnnotation = new TemplateSectionBoundsAnnotation(sectionizedTextTemplate.getTemplateSectionAnnotation(), 0);
			// Set the root begin and end index.
			rootSectionBoundsAnnotation.setAnnotationEndIndex(preprocessedTemplate.length());
			
			// Process the content of the section.
			// Pass in 0 for parentPreviousSectionEndIndex.
			processNamedTemplateSection(rootSectionBoundsAnnotation, sectionizedTextTemplate, preprocessedTemplate, taIterator, 0, true);
			
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
	protected int processNamedTemplateSection(TemplateSectionBoundsAnnotation parentSectionBounds, NamedTemplateSection parentTemplateSection, String rawTemplateContent, ListIterator<TemplateAnnotation> taIterator, int parentPreviousSectionEndIndex, boolean isRootSection) throws TemplatePreprocessorException {
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
				
				// Let's check whether the current parent section ends before the next annotation.
				int parentSectionEndIndex = parentSectionBounds.getAnnotationEndIndex();
				
				// Only check for end of section when it is not the root section and its not set yet.
				if (!isRootSection && parentSectionEndIndex == -1) {
					// Before we add the raw template, we first check whether the end of the section is in this part of raw template.
					// If so we set the end index of this section, add the template up till that part as raw template, set the iterator one back and return the end index (so the parent will pickup the remaining annotations).
					logger.info(String.format("Searching for section end index for '%s' between index %d and %d", parentTemplateSection.getSectionName(), expectedSectionBeginIndex, templateAnnotation.getAnnotationBeginIndex()));
					parentSectionEndIndex = findSectionEndIndex(parentSectionBounds, rawTemplateContent, expectedSectionBeginIndex, templateAnnotation.getAnnotationBeginIndex());
					if (parentSectionEndIndex != -1) {
						// Store the section end index on the parent annotation.
						parentSectionBounds.setAnnotationEndIndex(parentSectionEndIndex);
						// Store the end index on the parent template section.
						parentTemplateSection.setSectionEndIndex(parentSectionEndIndex);
						logger.info(String.format("Successfully found begin and end position of section (%s -> %d:%d)", parentTemplateSection.getSectionName(), parentTemplateSection.getSectionBeginIndex(), parentTemplateSection.getSectionEndIndex()));
					}
				}
				
				// Check whether the end index is found (-1 means not found), and whether the end index is before the next annotation.
				if (parentSectionEndIndex != -1 && parentSectionEndIndex < templateAnnotation.getAnnotationBeginIndex()) {
					
					// If the parent section end index is later then the expected beginning of the next section we create a raw template with the part between.
					if (parentSectionEndIndex > expectedSectionBeginIndex) {
						addRawTemplate(parentTemplateSection, rawTemplateContent, expectedSectionBeginIndex, parentSectionEndIndex);
					}
					
					// Move the cursor on the iterator one back so the parent will handle the current annotation.
					taIterator.previous();
					
					// Return the parent section end index as the previousSectionEndIndex and let the parent section handle the rest of the annotations (since this one is closed).
					return parentSectionEndIndex;
				}
				// The end of section wasn't found, so all before the new section is raw template of the parent.
				else {
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
			
			// TemplateSectionBoundsAnnotation
			else if (templateAnnotation instanceof TemplateSectionBoundsAnnotation) {
				// Add a NamedTemplateSection for each TemplateSectionAnnotation.
				TemplateSectionBoundsAnnotation templateSectionBoundsAnnotation = (TemplateSectionBoundsAnnotation) templateAnnotation;
				logger.info(String.format("Start of processing NamedTemplateSection %s", templateSectionBoundsAnnotation.getName()));
				// Create the named template section.
				NamedTemplateSection namedTemplateSection = new NamedTemplateSection(templateSectionBoundsAnnotation.getName(), templateSectionBoundsAnnotation.getAnnotationBeginIndex(), templateSectionBoundsAnnotation.getTemplateSectionAnnotation());
				// If the end index is already known, also set it.
				if (templateSectionBoundsAnnotation.getAnnotationEndIndex() != -1)
					namedTemplateSection.setSectionEndIndex(templateSectionBoundsAnnotation.getAnnotationEndIndex());
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
			
			// Set the ending index of the current section for the next cycle. only if current annotation is not a templateSectionAnnotation
			// Do not set ending index if the current section is a templateSectionAnnotation specified in a config.
			if ((templateAnnotation.isDefinedInTemplate()) || !(templateAnnotation instanceof TemplateSectionAnnotation)) {
				previousSectionEndIndex = templateAnnotation.getAnnotationEndIndex();
			}
		}
		
		// After the last annotation match there might still be some raw template. If so check for the section end and add a raw template section.
		int rawTemplateContentEndIndex = rawTemplateContent.length();
		if (previousSectionEndIndex < rawTemplateContentEndIndex) {
			// TODO: For later: This code overlaps with the code at the beginning of the function. Can we somehow re-use it? Maybe not iterating on while, but on index and do an extra loop.
			int parentSectionEndIndex = parentSectionBounds.getAnnotationEndIndex();
			
			// Only check for end of section when it is not the root section and its not set yet.
			if (!isRootSection && parentSectionEndIndex == -1) {
				// Before we add the raw template, we first check whether the end of the section is in this part of raw template.
				// If so we set the end index of this section, add the template up till that part as raw template, set the iterator one back and return the end index (so the parent will pickup the remaining annotations).
				logger.info(String.format("Searching for section end index for '%s' between index %d and %d", parentTemplateSection.getSectionName(), previousSectionEndIndex, rawTemplateContentEndIndex));
				parentSectionEndIndex = findSectionEndIndex(parentSectionBounds, rawTemplateContent, previousSectionEndIndex, rawTemplateContentEndIndex);
				if (parentSectionEndIndex != -1) {
					// Store the section end index on the parent annotation.
					parentSectionBounds.setAnnotationEndIndex(parentSectionEndIndex);
					// Store the end index on the parent template section.
					parentTemplateSection.setSectionEndIndex(parentSectionEndIndex);
					logger.info(String.format("Successfully found begin and end position of section (%s -> %d:%d)", parentTemplateSection.getSectionName(), parentTemplateSection.getSectionBeginIndex(), parentTemplateSection.getSectionEndIndex()));
				}
			}
			
			// Check whether the end index is found (-1 means not found), and whether the end index is before the next annotation.
			if (parentSectionEndIndex != -1 && parentSectionEndIndex <= rawTemplateContentEndIndex) {
				
				// If the parent section end index is later then the expected beginning of the next section we create a raw template with the part between.
				if (parentSectionEndIndex > previousSectionEndIndex) {
					addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, parentSectionEndIndex);
				}
				
				// Return the parent section end index as the previousSectionEndIndex and let the parent section handle the rest of the annotations (since this one is closed).
				return parentSectionEndIndex;
			}
			// The end of section wasn't found, so all before the new section is raw template of the parent.
			else {
				addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, rawTemplateContentEndIndex);
			}
		}
		
		// If the current section is not the root section the end of the section should've been found by now.
		// So we throw an exception.
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
	protected int findSectionEndIndex(TemplateSectionBoundsAnnotation sectionAnnotationBounds, String rawTemplateContent, int sectionEndSearchBeginIndex, int sectionEndSearchEndIndex) throws TemplatePreprocessorException {
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
		// literalOnLastLine
		else if (templateSectionAnnotation.getLiteralOnLastLine() != null && templateSectionAnnotation.getLiteralOnLastLine().length() > 0) {
		    Pattern pattern = Pattern.compile(String.format("%s.*\\r?\\n?", Pattern.quote(templateSectionAnnotation.getLiteralOnLastLine())));
		    Matcher matcher = pattern.matcher(rawTemplateContent);
			if (matcher.find(sectionEndSearchBeginIndex) && matcher.end() <= sectionEndSearchEndIndex) {
				return matcher.end();
			} else {
				return -1;
			}
		}
		// If end was not specified, check nrOfLines, this has a default value of 1 if not explicitly set.	
		else  {
			// Get the nrOfLines from the annotation.
			Integer sectionNrOfLines = templateSectionAnnotation.getNrOfLines();
			
			// Loop through the newlines as of the start of the section.
			for (int currentNrOfLines = 0; currentNrOfLines < sectionNrOfLines; currentNrOfLines++) {
				// The end of the section is the first newline we encounter after the begin of the section. We include the newline in the section.
				sectionEndCharIndex = rawTemplateContent.indexOf('\n', (sectionEndCharIndex == -1) ? sectionEndSearchBeginIndex : sectionEndCharIndex + 1);
				
				// If the end of line wasn't found, break out of the loop.
				if (sectionEndCharIndex == -1)
					break;
			}
			
			if (sectionEndCharIndex == -1)
				return -1;
			
			// Add the 1 to the length to compensate for the \n character.
			sectionEndCharIndex += 1;
		}
		// Otherwise, we can't get the end location.
		//else {
		//	throw new TemplatePreprocessorException(String.format("No end of section defined (%s)", sectionAnnotationBounds.getName()));
		//}
		
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
	protected void addRawTemplate(NamedTemplateSection parentTemplateSection, String rawTemplateContent, int startIndex, int endIndex) {
		// Escape XML chars, since the raw template will be put in an XSLT transformation.
		String rawTemplateSectionContent = XMLUtils.excapeXMLChars(doubleEntityEncode(rawTemplateContent.substring(startIndex, endIndex)));
		logger.info(String.format("Found a raw template section in section '%s' between index %d and %d: '%s'", parentTemplateSection.getSectionName(), startIndex, endIndex, rawTemplateSectionContent));
		parentTemplateSection.addTemplateSection(new RawTemplateSection(rawTemplateSectionContent, startIndex, endIndex));
	}
	
	/**
	 * Double entity encode a String.
	 * @param input
	 * @return
	 */
	private static String doubleEntityEncode(String input) {
		return input.replaceAll("&([a-zA-Z0-9]+;)", "&amp;$1");
	}
}
