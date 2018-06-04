package com.xbreeze.xgenerate.template;

import java.util.ListIterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.binding.SectionModelBindingConfig;
import com.xbreeze.xgenerate.config.template.RootTemplateConfig;
import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateCommentAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionBoundsAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionAction;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionStyle;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionType;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationException;
import com.xbreeze.xgenerate.template.section.CommentTemplateSection;
import com.xbreeze.xgenerate.template.section.NamedTemplateSection;
import com.xbreeze.xgenerate.template.section.RawTemplateSection;
import com.xbreeze.xgenerate.template.section.RepetitionTemplateSection;
import com.xbreeze.xgenerate.template.section.SectionedTemplate;
import com.xbreeze.xgenerate.utils.XMLUtils;

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
	 * @param config the XGenConfig to set
	 */
	public void setConfig(XGenConfig config) {
		this._config = config;
	}
	
	/**
	 * Perform the pre-processing to get to the pre-processed template.
	 * @return The pre-processed template.
	 * @throws UnhandledException 
	 * @throws UnknownAnnotationException 
	 */
	public XsltTemplate preProcess(RawTemplate rawTemplate, String relativeOutputFileUri) throws TemplatePreprocessorException, UnhandledException {
		RootTemplateConfig templateConfig = _config.getTemplateConfig();
		
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
		
		// Pre-process the template.
		PreprocessedTemplate preprocessedTemplate = this.getPreprocessedTemplate(rawTemplate, rootSectionName);
		
		// Sectionize the template.
		SectionedTemplate sectionizedTemplate = this.sectionizeTemplate(preprocessedTemplate);
		
		// Now the templates are pre-processed by their specific preprocessor, we can perform the generic pre-processing here.
		XsltTemplate xsltTemplate = new XsltTemplate(rawTemplate.getRawTemplateFileName(), rawTemplate.getRawTemplateFileLocation(), templateConfig, relativeOutputFileUri, rootSectionModelBinding);
		
		// Append the Xslt from the section to the pre-processed template.
		sectionizedTemplate.appendTemplateXslt(xsltTemplate, _config, rootSectionModelBinding);
		
		// Finalize the template before returning it.
		xsltTemplate.finalizeTemplate();
		
		// Return the pre-processed template.
		return xsltTemplate;
	}
	
	/**
	 * Procedure which each specific implementation of the TemplatePreprocessor needs to implement to get to the generic PreprocessedTemplate.
	 * In this procedure the raw template needs to be pre-processed and annotations gathered.
	 */
	protected abstract PreprocessedTemplate getPreprocessedTemplate(RawTemplate rawTemplate, String rootSectionName) throws TemplatePreprocessorException;
	
	/**
	 * Sectionize the template after its annotations and configurations are processed into a list of annotations.
	 * @param preprocessedTemplate The pre-processed template.
	 * @param rootSectionName The root section name.
	 * @return The sectionized template.
	 * @throws TemplatePreprocessorException
	 */
	private SectionedTemplate sectionizeTemplate(PreprocessedTemplate preprocessedTemplate) throws TemplatePreprocessorException {
		// Initialize the sectionized template.
		SectionedTemplate sectionizedTextTemplate = new SectionedTemplate(preprocessedTemplate.getRootTemplateSectionBoundsAnnotation().getTemplateSectionAnnotation());
		// Set the root section end index on the sectionized template.
		sectionizedTextTemplate.setSectionEndIndex(preprocessedTemplate.getRootTemplateSectionBoundsAnnotation().getAnnotationEndIndex());
		
		// We use the ListIterator here since we can go forward and backward.
		ListIterator<TemplateAnnotation> taIterator = preprocessedTemplate.getTemplateAnnotations().listIterator();
		
		// Process the content of the section.
		// Pass in 0 for parentPreviousSectionEndIndex.
		processNamedTemplateSection(preprocessedTemplate.getRootTemplateSectionBoundsAnnotation(), sectionizedTextTemplate, preprocessedTemplate.getPreprocessedRawTemplate(), taIterator, 0, true, -1);
		
		// Return the SectionizedTextTemplate;
		return sectionizedTextTemplate;
	}
	
	/**
	 * Process the named template section.
	 * @param namedTemplateSection
	 * @param rawTemplateContent
	 * @throws TemplatePreprocessorException
	 */
	private int processNamedTemplateSection(TemplateSectionBoundsAnnotation parentSectionBounds, NamedTemplateSection parentTemplateSection, String rawTemplateContent, ListIterator<TemplateAnnotation> taIterator, int parentPreviousSectionEndIndex, boolean isRootSection, int parentMaxSectionEndIndex) throws TemplatePreprocessorException {
		logger.fine(String.format("processNamedTemplateSection called for section '%s', parentPreviousSectionEndIndex=%d", parentTemplateSection.getSectionName(), parentPreviousSectionEndIndex));
		// Loop through the template annotations.
		int previousSectionEndIndex = parentPreviousSectionEndIndex; 
		
		// Before processing the content, we first check whether a prefix is set on the annotation.
		if (parentSectionBounds.getTemplateSectionAnnotation() instanceof TemplateTextSectionAnnotation
				&& ((TemplateTextSectionAnnotation)parentSectionBounds.getTemplateSectionAnnotation()).getPrefix() != null
				&& ((TemplateTextSectionAnnotation)parentSectionBounds.getTemplateSectionAnnotation()).getPrefix().length() > 0
		) {
			// If the prefix is set, we scan for any whitespace at the beginning of the section and store it as a separate RawTemplate part after which we add the RepetitionTemplateSection for the prefix.
			logger.fine(String.format("Prefix is defined for section '%s', searching for whitespace and creating appropriate sections.", parentTemplateSection.getSectionName()));
			// Store the text template section annotation in a variable.
			TemplateTextSectionAnnotation textSectionAnnotaton = ((TemplateTextSectionAnnotation)parentSectionBounds.getTemplateSectionAnnotation());
			// Now we scan for any whitespace at the end of the found raw-template and add the suffix section before the whitespace.
			// So <raw-template><prefix>
			// \A     -> The begin of the input
			// [ \t]+ -> Any space or tab: [ \t\n\x0B\f\r]
		    Pattern pattern = Pattern.compile(String.format("\\A[ \\t]+"));
		    Matcher matcher = pattern.matcher(rawTemplateContent.substring(previousSectionEndIndex));
			if (matcher.find()) {
				// Get the begin and end position of the whitespace at the begin of the section.
				int whitespaceStartIndex = previousSectionEndIndex + matcher.start();
				int whitespaceEndIndex = previousSectionEndIndex + matcher.end();
				logger.fine(String.format("Whitespace found between %d and %d, so creating seperate sections.", whitespaceStartIndex, whitespaceEndIndex));
				// Add the template content before the prefix position as a raw template.
				addRawTemplate(parentTemplateSection, rawTemplateContent, whitespaceStartIndex, whitespaceEndIndex);
				// Add the repetition template section for the prefix.
				addRepetitionTemplate(parentTemplateSection, textSectionAnnotaton.getPrefix(), whitespaceEndIndex, RepetitionType.prefix, textSectionAnnotaton.getPrefixStyle(), textSectionAnnotaton.getPrefixAction());
			}
			// No whitespace found, so add the prefix to the start.
			else {
				logger.fine("No whitespace found, so creating prefix repetition section at the start.");
				// Add the repetition template section for the prefix.
				addRepetitionTemplate(parentTemplateSection, textSectionAnnotaton.getPrefix(), previousSectionEndIndex, RepetitionType.prefix, textSectionAnnotaton.getPrefixStyle(), textSectionAnnotaton.getPrefixAction());
			}
		}
		
		// Loop till we reached the end of the template.
		// If we reach the end of a section in between its handled within the loop.
		while (previousSectionEndIndex < rawTemplateContent.length()) {
			
			// Initialize the template annotation variable. This will only be assigned when there is still an annotation available.
			TemplateAnnotation templateAnnotation = null;
			
			// Check whether there is something between the previous and current annotation.
			{
				int nextSectionBeginIndex;
				// If there is another annotation, store the next annotation begin index.
				if (taIterator.hasNext()) {
					// Store the next annotation in the variable.
					templateAnnotation = taIterator.next();
					// Store the next annotation in a local variable (also moves the cursor one forward).
					nextSectionBeginIndex = templateAnnotation.getAnnotationBeginIndex();
				}
				// Otherwise we are at the end of the template and there might be some raw template left.
				else {
					// Set the actual 'next section' index to the end of the template.
					nextSectionBeginIndex = rawTemplateContent.length();
				}
				
				// If the next section begin index is greater then the parent its parent end index, set it back.
				if (parentMaxSectionEndIndex != -1 && nextSectionBeginIndex > parentMaxSectionEndIndex) {
					nextSectionBeginIndex = parentMaxSectionEndIndex;
					// To be safe, nullify the templateAnnotation if set and set the iterator one back.
					if (templateAnnotation != null) {
						taIterator.previous();
						templateAnnotation = null;
					}
				}
				
				// If the templateAnnotation start index is not the next number after the previous section end, we add a RawTemplateSection to the parent NamedTemplateSection.
				// Let's check whether there is some template section which is after the last section and before the next (a raw template section).
				if (nextSectionBeginIndex >= previousSectionEndIndex) {
					
					// Let's check whether the current parent section ends before the next annotation.
					int parentSectionEndIndex = parentSectionBounds.getAnnotationEndIndex();
					
					// If the end index isn't set, something is wrong.
					if (parentSectionEndIndex == -1)
						throw new TemplatePreprocessorException(String.format("The end index of the section '%s' is not set, this shouldn't happen!", parentTemplateSection.getSectionName()));
					
					// Check whether the end index is found (-1 means not found), and whether the end index is before the next annotation.
					if (parentSectionEndIndex <= nextSectionBeginIndex) {
						
						// If the parent section end index is later then the expected beginning of the next section we create a raw template with the part between.
						if (parentSectionEndIndex > previousSectionEndIndex) {
							// If there is a suffix defined for the parent section, add a repetition section before the newline.
							if (parentSectionBounds.getTemplateSectionAnnotation() instanceof TemplateTextSectionAnnotation
									&& ((TemplateTextSectionAnnotation)parentSectionBounds.getTemplateSectionAnnotation()).getSuffix() != null
									&& ((TemplateTextSectionAnnotation)parentSectionBounds.getTemplateSectionAnnotation()).getSuffix().length() > 0
							) {
								logger.fine(String.format("Suffix is defined for section '%s', searching for whitespace and creating appropriate sections.", parentTemplateSection.getSectionName()));
								TemplateTextSectionAnnotation textSectionAnnotation = (TemplateTextSectionAnnotation)parentSectionBounds.getTemplateSectionAnnotation();
								// Now we scan for any whitespace at the end of the found raw-template and add the suffix section before the whitespace.
								// So <raw-template><suffix><whitespace>
								// \s  -> Any whitespace character: [ \t\n\x0B\f\r]
								// \z  -> The end of the input
							    Pattern pattern = Pattern.compile(String.format("\\s+\\z"));
							    Matcher matcher = pattern.matcher(rawTemplateContent.substring(0, parentSectionEndIndex));
								if (matcher.find(previousSectionEndIndex)) {
									// Get the begin and end position of the whitespace at the end of the section.
									int whitespaceStartIndex = matcher.start();
									int whitespaceEndIndex = matcher.end();
									logger.fine(String.format("Whitespace found between %d and %d, so creating seperate sections.", whitespaceStartIndex, whitespaceEndIndex));
									// Add the template content before the suffix position as a raw template.
									addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, whitespaceStartIndex);
									// Add the repetition template section for the suffix.
									addRepetitionTemplate(parentTemplateSection, textSectionAnnotation.getSuffix(), whitespaceStartIndex, RepetitionType.suffix, textSectionAnnotation.getSuffixStyle(), textSectionAnnotation.getSuffixAction());
									// Add the raw template containing the ending whitespace.
									addRawTemplate(parentTemplateSection, rawTemplateContent, whitespaceStartIndex, whitespaceEndIndex);
								}
								// No whitespace found, so add the suffix to the end.
								else {
									logger.fine("No whitespace found, so creating repetition section at the end.");
									// Add the whole template part as raw template, since there is no whitespace at the end.
									addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, parentSectionEndIndex);
									// Add the repetition template section for the suffix.
									addRepetitionTemplate(parentTemplateSection, textSectionAnnotation.getSuffix(), parentSectionEndIndex, RepetitionType.suffix, textSectionAnnotation.getSuffixStyle(), textSectionAnnotation.getSuffixAction());
								}
							}
							// No suffix defined, so all remaining is raw template.
							else {
								addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, parentSectionEndIndex);
							}
						}
						
						// Move the cursor on the iterator one back so the parent will handle the current annotation.
						if (templateAnnotation != null)
							taIterator.previous();
						
						// Return the parent section end index as the previousSectionEndIndex and let the parent section handle the rest of the annotations (since this one is closed).
						return parentSectionEndIndex;
					}
					// The end of section wasn't found, so all before the new section is raw template of the parent.
					else {
						addRawTemplate(parentTemplateSection, rawTemplateContent, previousSectionEndIndex, nextSectionBeginIndex);
						// Set the end index to the end of the raw template.
						previousSectionEndIndex = nextSectionBeginIndex;
						
						// When the templateAnnotation is null, this is the last piece of raw template.
						// So return the index of the end here.
						if (templateAnnotation == null)
							return previousSectionEndIndex;
					}
				}
			}
			
			
			// Get the next template annotation and handle it.
			{
				// When the templateAnnotation is null here we have a problem. This shouldn't occur.
				if (templateAnnotation == null)
					throw new TemplatePreprocessorException(String.format("An illegal state has been reached while pre-processing the template, trying to process an annotation in the last loop (parentSection='%s'; previousSectionEndIndex=%d).", parentTemplateSection.getSectionName(), previousSectionEndIndex));
				
				// Based on the type of annotation we can have different actions.
				// For example when it is a TemplateCommentAnnotation, we just add it to the current NamedTemplate.
				// Here we go through the annotation types one by one.
				
				// TemplateCommentAnnotation
				if (templateAnnotation instanceof TemplateCommentAnnotation) {
					// Add a CommentTemplateSection for each TemplateCommentAnnotation.
					TemplateCommentAnnotation templateCommentAnnotation = ((TemplateCommentAnnotation) templateAnnotation);
					parentTemplateSection.addTemplateSection(new CommentTemplateSection(templateCommentAnnotation.getComment(), templateCommentAnnotation.getAnnotationBeginIndex(), templateCommentAnnotation.getAnnotationEndIndex()));
					logger.fine(String.format("Added CommentTemplateSection to SectionizedTextTemplate (%d:%d)", templateCommentAnnotation.getAnnotationBeginIndex(), templateCommentAnnotation.getAnnotationEndIndex()));
				}
				
				else if (templateAnnotation instanceof TemplateSectionAnnotation) {
					// If it is a template section annotation we skip this section.
					// It's not represented in the preprocessed template.
				}
				
				// TemplateSectionBoundsAnnotation
				else if (templateAnnotation instanceof TemplateSectionBoundsAnnotation) {
					// Add a NamedTemplateSection for each TemplateSectionAnnotation.
					TemplateSectionBoundsAnnotation templateSectionBoundsAnnotation = (TemplateSectionBoundsAnnotation) templateAnnotation;
					logger.info(String.format("Start of processing section '%s'", templateSectionBoundsAnnotation.getName()));
					// Create the named template section.
					NamedTemplateSection namedTemplateSection = new NamedTemplateSection(templateSectionBoundsAnnotation.getTemplateSectionAnnotation(), templateSectionBoundsAnnotation.getAnnotationBeginIndex(), templateSectionBoundsAnnotation.getAnnotationEndIndex());
					
					// Process the content of the named template (recursively).
					// This process will return the end index of the section (or throw an exception if not found).
					processNamedTemplateSection(templateSectionBoundsAnnotation, namedTemplateSection, rawTemplateContent, taIterator, previousSectionEndIndex, false, parentTemplateSection.getSectionEndIndex());
					
					// Check the end index of the section, this should exceed the parent bounds, if set already.
					if (namedTemplateSection.getSectionEndIndex() > parentTemplateSection.getSectionEndIndex())
						throw new TemplatePreprocessorException(String.format("The found end-index of section '%s' is after the parent section '%s' end index.", namedTemplateSection.getSectionEndIndex(), parentTemplateSection.getSectionEndIndex()));
					
					// Add the named template section to the sectionized template.
					parentTemplateSection.addTemplateSection(namedTemplateSection);
					logger.fine(String.format("Added NamedTemplateSection to SectionizedTextTemplate (%s -> %d:%d)", namedTemplateSection.getSectionName(), namedTemplateSection.getSectionBeginIndex(), namedTemplateSection.getSectionEndIndex()));
				}
				
				// If there is some other annotation found we didn't handle, let's throw an exception.
				else {
					throw new TemplatePreprocessorException(String.format("Unhandled annotation found: %s", templateAnnotation.getAnnotationName()));
				}
				
				// Set the ending index of the current section for the next cycle, only if current annotation is not a TemplateSectionAnnotation defined in the config.
				if (!(templateAnnotation instanceof TemplateSectionAnnotation && !templateAnnotation.isDefinedInTemplate())) {
					previousSectionEndIndex = templateAnnotation.getAnnotationEndIndex();
				}
			}
		}
		
		// If this is the root section, return the end index.
		if (isRootSection)
			return parentSectionBounds.getAnnotationEndIndex();
		
		// If we reach the code here we haven't found the end of a section for some reason.
		// So we throw an exception.
		throw new TemplatePreprocessorException(String.format("The end of section '%s' can't be found!", parentTemplateSection.getSectionName()));
	}
	
	/**
	 * Add a raw template to the NamedTemplateSection
	 * @param parentTemplateSection The parent NamedTemplateSection
	 * @param rawTemplateContent The raw template content
	 * @param startIndex The starting index of the raw template.
	 * @param endIndex The ending index of the raw template.
	 */
	private void addRawTemplate(NamedTemplateSection parentTemplateSection, String rawTemplateContent, int startIndex, int endIndex) {
		// Escape XML chars, since the raw template will be put in an XSLT transformation.
		String rawTemplateSectionContent = XMLUtils.excapeXMLChars(doubleEntityEncode(rawTemplateContent.substring(startIndex, endIndex)));
		logger.fine(String.format("Found a raw template section in section '%s' between index %d and %d: '%s'", parentTemplateSection.getSectionName(), startIndex, endIndex, rawTemplateSectionContent));
		parentTemplateSection.addTemplateSection(new RawTemplateSection(rawTemplateSectionContent, startIndex, endIndex));
	}
	
	/**
	 * Add a raw template to the NamedTemplateSection
	 * @param parentTemplateSection The parent NamedTemplateSection
	 * @param rawTemplateContent The raw template content
	 * @param startIndex The starting index of the raw template.
	 * @param endIndex The ending index of the raw template.
	 */
	private void addRepetitionTemplate(NamedTemplateSection parentTemplateSection, String repetitionContent, int sectionIndex, RepetitionType repetitionType, RepetitionStyle repetitionStyle, RepetitionAction repetitionAction) {
		logger.fine(String.format("Found a repetition template section in section '%s' at index %d: %s", parentTemplateSection.getSectionName(), sectionIndex, repetitionType.name()));
		parentTemplateSection.addTemplateSection(new RepetitionTemplateSection(repetitionContent, sectionIndex, repetitionType, repetitionStyle, repetitionAction));
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
