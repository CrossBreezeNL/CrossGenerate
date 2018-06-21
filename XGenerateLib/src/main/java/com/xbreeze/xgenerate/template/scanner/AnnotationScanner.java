package com.xbreeze.xgenerate.template.scanner;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.template.FileFormatConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;
import com.xbreeze.xgenerate.template.annotation.AnnotationException;
import com.xbreeze.xgenerate.template.annotation.TemplateAnnotation;

/***
 * 
 * @author Willem
 * Helper class for scanning a (partial) template for annotations
 *
 */
public class AnnotationScanner {
	protected static final Logger logger = Logger.getLogger(TemplatePreprocessor.class.getName());
	
	/**
	 * The following regex part is for identifying annotations with is parameters.
	 * OLD
	 * %s           -> 1nd parameter for String.format, the annotation prefix
	 * ([a-zA-Z]+)  -> The name of the annotation (region 1)
	 * [ \t]*       -> Again, any space or tab characters
	 * %s           -> 2rd parameter for String.format, the annotation args prefix
	 * (.*[^ \t])   -> The arguments for the annotation, not ending with a space or tab (region 2).
	 * [ \t]*       -> Again, any space or tab characters
	 * %s           -> 3th parameter for String.format, the annotation args suffix
	 * 
	 * NEW per 2018-06-21:
	 * %s           -> 1nd parameter for String.format, the annotation prefix
	 * ([a-zA-Z]+)  -> The name of the annotation (region 1)
	 * [ \t]*       -> Again, any space or tab characters
	 * %s           -> 2rd parameter for String.format, the annotation args prefix
	 * ([^@]*)   -> The arguments for the annotation, being everything except another annotationPrefix, marked by @ (region 2).
	 * [ \t]*       -> Again, any space or tab characters
	 * %s           -> 3th parameter for String.format, the annotation args suffix
	 */
	private static String ANNOTATION_REGEX(String annotationPrefix, String annotationArgsPrefix, String annotationArgsSuffix) {
		return String.format(
				/* "%s([a-zA-Z]+)[ \\t]*%s(.*[^ \\t])[ \\t]*%s", */
				"%s([a-zA-Z]+)[ \\\\t]*%s([^@]*)[ \\\\t]*%s",
				Pattern.quote(annotationPrefix),
				Pattern.quote(annotationArgsPrefix),
				Pattern.quote(annotationArgsSuffix)
		);
	}
	
	/**
	 * The following regex part is the overlapping part of the single and multi-line comment pattern.
	 * If there is nothing but empty characters between line start and the comment start we take it into the match.
	 * ^?              -> The beginning of a line (optionally)
	 * [ \t]*          -> Any space or tab characters (optionally)
	 * %s              -> 1st parameter for String.format, the single-line comment prefix
	 * [ \t]*          -> Again, any space or tab characters
	 * (.*%s.+)        -> The content of the comment section, any character but the annotation prefix must occur (2nd parameter for String.format). (region 1)
	 * [ \t]*          -> Again, any space or tab characters 
	 * The remainder of the pattern is specified per single or multi-line comment pattern.
	 */
	private static String COMMENT_REGEX(String commentPrefix, String annotationPrefix) {
		return String.format("^?[ \\t]*%s[ \\t]*(.*%s.*)[ \\t]*",
				Pattern.quote(commentPrefix),
				Pattern.quote(annotationPrefix)
		);
	}
	
	/**
	 * Collection annotations which are in-line in a String. Some parts may be a annotation and other not.
	 * @param templateContent The template part to scan.
	 * @param fileFormatConfig The file format config.
	 * @return A list of annotations found.
	 * @throws TemplatePreprocessorException
	 */
	public static ArrayList<TemplateAnnotation> collectInlineAnnotations(String templateContent, FileFormatConfig fileFormatConfig) throws TemplatePreprocessorException {
		return collectInlineAnnotations(templateContent, fileFormatConfig, 0, templateContent.length());
	}
	
	/**
	 * Collection annotations which are in-line in a String. Some parts may be a annotation and other not.
	 * @param templateContent The template part to scan.
	 * @param fileFormatConfig The file format config.
	 * @param beginIndex The character index of the templateContent to start scanning.
	 * @param endIndex The character index of the templateContent to stop scanning.
	 * @return A list of annotations found.
	 * @throws TemplatePreprocessorException
	 */
	public static ArrayList<TemplateAnnotation> collectInlineAnnotations(String templateContent, FileFormatConfig fileFormatConfig, int beginIndex, int endIndex) throws TemplatePreprocessorException {
		ArrayList<TemplateAnnotation> annotations = new ArrayList<>();
		
		/**
		 * [ \t]*       -> Any space or tab characters before the annotation.
		 * %s           -> The annotation regex.
		 */
		Pattern annotationPattern = Pattern.compile(
				String.format(
						"[ \\t]*%s",
						ANNOTATION_REGEX(
								fileFormatConfig.getAnnotationPrefix(), 
								fileFormatConfig.getAnnotationArgsPrefix(), 
								fileFormatConfig.getAnnotationArgsSuffix()
						)
				),
				// Match case insensitive.
				Pattern.CASE_INSENSITIVE
		);
		
		// Create the matcher.
		Matcher matcher = annotationPattern.matcher(templateContent);
		// Set the region to search.
		matcher.region(beginIndex, endIndex);
		// Loop through the results.
		while (matcher.find()) {
			
			// Group 1: The name of the annotation.
			String annotationName = matcher.group(1);
			// Group 2: The arguments for the annotation.
			String annotationParams = matcher.group(2);
			logger.info(String.format("Found annotation (name: '%s'; params: '%s'; start: %d; end: %d", annotationName, annotationParams, matcher.start(), matcher.end()));
			
			try {
				// Get the TemplateAnnotation using the name and the params.
				TemplateAnnotation templateAnnotation;
				try {
					templateAnnotation = TemplateAnnotation.fromName(annotationName, annotationParams, matcher.start(), matcher.end());
				}
				// When the UnhandledException occurs, wrap it into a TemplatePreprocessorException.
				catch (UnhandledException e) {
					throw new TemplatePreprocessorException(e);
				}
				// Add the template annotation to the list.
				annotations.add(templateAnnotation);
			} catch (AnnotationException e) {
				throw new TemplatePreprocessorException(e);
			}
		}
		
		return annotations;
	}
	
	/**
	 * Collection annotations found in comment parts of a template.
	 * @param templateContent The template content.
	 * @param fileFormatConfig The file format config.
	 * @param commentPattern The comment pattern.
	 * @param commentContentRegion The comment content region (pattern group nr).
	 * @param annotations The annotations list to add new found annotations in.
	 * @param beginIndex The begin index of the templateContent to search from.
	 * @param endIndex The end index of the templateContent to search till.
	 * @throws TemplatePreprocessorException
	 */
	private static void collectCommentAnnotations(String templateContent, FileFormatConfig fileFormatConfig, Pattern commentPattern, int commentContentRegion, ArrayList<TemplateAnnotation> annotations, int beginIndex, int endIndex) throws TemplatePreprocessorException {
		// Create the matcher.
		Matcher commentMatcher = commentPattern.matcher(templateContent);
		// Set the region to search.
		commentMatcher.region(beginIndex, endIndex);
		// Loop through the results.
		while (commentMatcher.find()) {
			logger.fine(String.format("Found comment with annotation. (start: %d; end: %d; commentStart: %d; commentEnd: %d; comment: '%s')", commentMatcher.start(), commentMatcher.end(), commentMatcher.start(commentContentRegion), commentMatcher.end(commentContentRegion), commentMatcher.group(commentContentRegion)));
			// Collect the annotation in the content of the comment.
			ArrayList<TemplateAnnotation> foundCommentAnnotations = collectInlineAnnotations(templateContent, fileFormatConfig, commentMatcher.start(commentContentRegion), commentMatcher.end(commentContentRegion));
			if (foundCommentAnnotations.size() == 1) {
				TemplateAnnotation onlyAnnotationInComment = foundCommentAnnotations.get(0);
				logger.fine(String.format("Only annotation found: '%s'", templateContent.substring(onlyAnnotationInComment.getAnnotationBeginIndex(), onlyAnnotationInComment.getAnnotationEndIndex())));
				// If the annotation is the only thing on the line, we take the whole line as begin and end index to make sure its not in the result.
				if (
						// If the regions of the annotation are the same as the comment content bounds.
						(
								onlyAnnotationInComment.getAnnotationBeginIndex() == commentMatcher.start(commentContentRegion) 
								&& onlyAnnotationInComment.getAnnotationEndIndex() == commentMatcher.end(commentContentRegion)
						)
						// Or the remaining content is only white-space.
						|| (
								onlyAnnotationInComment.getAnnotationBeginIndex() == commentMatcher.start(commentContentRegion)
								&& onlyAnnotationInComment.getAnnotationEndIndex() < commentMatcher.end(commentContentRegion)
								&& templateContent.substring(onlyAnnotationInComment.getAnnotationEndIndex(), commentMatcher.end(commentContentRegion)).trim().length() == 0
						)
				) {
					// Update the section annotation bounds.
					logger.fine("The comment only contains an annotation, so the whole comment is now part of the annotation.");
					onlyAnnotationInComment.setAnnotationBeginIndex(commentMatcher.start());
					onlyAnnotationInComment.setAnnotationEndIndex(commentMatcher.end());
				}
			}
			annotations.addAll(foundCommentAnnotations);
		}
	}
	
	/**
	 * Collection all text annotations (in single and multi-line comment parts).
	 * @param templateContent The template content to scan.
	 * @param fileFormatConfig The file format config.
	 * @return The list of annotations found.
	 * @throws TemplatePreprocessorException
	 */
	public static ArrayList<TemplateAnnotation> collectTextAnnotations(String templateContent, FileFormatConfig fileFormatConfig, int beginIndex, int endIndex) throws TemplatePreprocessorException {
		// Initialize a collection for the template annotations.
		ArrayList<TemplateAnnotation> annotations = new ArrayList<TemplateAnnotation>();
		
		// First search for single-line comment sections and scan for annotations in there.
		if (fileFormatConfig.getSingleLineCommentPrefix() != null && fileFormatConfig.getSingleLineCommentPrefix().length() > 0) {
			// Single line comments can start anywhere on a line (beginning or after some code), but always end with line-end.
			/**
			 * $               -> The end of the line.
			 * \r?             -> Include the carriage return (optionally)
			 * \n?             -> Include the new line. (optionally)
			 */
			Pattern singleLinePattern = Pattern.compile(
					String.format(
							"%s$\\r?\\n?",
							COMMENT_REGEX(
									fileFormatConfig.getSingleLineCommentPrefix(), 
									fileFormatConfig.getAnnotationPrefix()
							)
					),
					// Match case insensitive.
					Pattern.CASE_INSENSITIVE
					// Find the pattern on any line (not only the first or last line).
					| Pattern.MULTILINE
			);
			
			// Collect the annotation for the single-line comment sections.
			collectCommentAnnotations(templateContent, fileFormatConfig, singleLinePattern, 1, annotations, beginIndex, endIndex);
		}
		
		// Second search for multi-line comment sections and scan for annotations in there.
		if (fileFormatConfig.getMultiLineCommentPrefix() != null
				&& fileFormatConfig.getMultiLineCommentPrefix().length() > 0 
				&& fileFormatConfig.getMultiLineCommentSuffix() != null 
				&& fileFormatConfig.getMultiLineCommentSuffix().length() > 0)
		{
			/**
			 * If there is nothing but empty characters between line start and the comment start we take it into the match.
			 * %s              -> 3st parameter for String.format, the multi-line comment suffix.
			 * [ \t]*          -> Again, any space or tab characters
			 * $?              -> The end of the line. (optionally)
			 * \r?             -> Include the carriage return (optionally)
			 * \n?             -> Include the new line. (optionally)
			 */
			Pattern multiLinePattern = Pattern.compile(
					String.format(
							"%s%s[ \\t]*$?\\r?\\n?",
							COMMENT_REGEX(
									fileFormatConfig.getMultiLineCommentPrefix(), 
									fileFormatConfig.getAnnotationPrefix()
							),
							Pattern.quote(fileFormatConfig.getMultiLineCommentSuffix())
					),
					// Match case insensitive.
					Pattern.CASE_INSENSITIVE
					// Find the pattern on any line (not only the first or last line).
					| Pattern.MULTILINE
					// Also set the . to also match newlines.
					| Pattern.DOTALL 
			);
			
			// Collect the annotation for the single-line comment sections.
			collectCommentAnnotations(templateContent, fileFormatConfig, multiLinePattern, 1, annotations, beginIndex, endIndex);
		}
		
		return annotations;
	}

}
