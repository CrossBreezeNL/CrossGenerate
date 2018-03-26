package com.xbreeze.xgenerate.template.scanner;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.UnhandledException;
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
	 * Collection annotations which are in-line in a String. Some parts may be a annotation and other not. 
	 * @param templateContent The template part to scan.
	 * @param annotationPrefix The annotation prefix.
	 * @param annotationArgsPrefix The annotation args prefix.
	 * @param annotationArgsSuffix The annotation args suffix.
	 * @return A list of annotations found.
	 * @throws TemplatePreprocessorException
	 */
	public static ArrayList<TemplateAnnotation> collectInlineAnnotations(String templateContent, String annotationPrefix, String annotationArgsPrefix, String annotationArgsSuffix) throws TemplatePreprocessorException {
		/**
		 * [ \t]*       -> Any space or tab characters before the annotation.
		 * %s           -> 1nd parameter for String.format, the annotation prefix
		 * ([a-zA-Z]+)  -> The name of the annotation (region 1)
		 * [ \t]*       -> Again, any space or tab characters
		 * %s           -> 3rd parameter for String.format, the annotation args prefix
		 * (.*)         -> The arguments for the annotation (region 2).
		 * %s           -> 4th parameter for String.format, the annotation args suffix
		 */
		Pattern compiledPattern = Pattern.compile(
				String.format(
						"[ \t]*%s([a-zA-Z]+)[ \t]*%s(.*)%s",
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationPrefix)),
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationArgsPrefix)),
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationArgsSuffix))
				)
		);
		
		// Collect the annotations and return them based on the pattern.
		return collectAnnotations(templateContent, compiledPattern);
	}
	
	/**
	 * Static method for scanning for annotations
	 * @param templateContent the template part that is searched for annotations
	 * @return returns the list of annotations found in the text.
	 * @throws TemplatePreprocessorException 
	 */
	public static ArrayList<TemplateAnnotation> collectAnnotations(String templateContent, String annotationTextPrefix, String annotationPrefix, String annotationArgsPrefix, String annotationArgsSuffix, String annotationTextSuffix) throws TemplatePreprocessorException {
		/**
		 * (?m)         -> Multi-line match (indicates it can match on sub-parts of the input).
		 * ^            -> The beginning of a line
		 * [ \t]*       -> Any space or tab characters
		 * %s           -> 1st parameter for String.format, the annotation text prefix
		 * [ \t]*       -> Again, any space or tab characters
		 * %s           -> 2nd parameter for String.format, the annotation prefix
		 * ([a-zA-Z]+)  -> The name of the annotation (region 1)
		 * [ \t]*       -> Again, any space or tab characters
		 * %s           -> 3rd parameter for String.format, the annotation args prefix
		 * (.*)         -> The arguments for the annotation (region 2).
		 * %s           -> 4th parameter for String.format, the annotation args suffix
		 * [ \t]*       -> Again, any space or tab characters
		 * %s           -> 5th parameter for String.format, the annotation text suffix
		 * [ \t]*       -> Again, any space or tab characters
		 * $            -> The end of the line.
		 * \r?          -> Include the carriage return (optionally)
		 * \n?          -> Include the new line. (optionally)
		 * 
		 * Use Pattern.quote() to make sure the parameters passed are escaped if needed. Cause we want to treat them as literals.
		 * Revisions
		 *  - 1: (?m)^[ \t]*%s[ \t]*%s([a-zA-Z]+)[ \t]*%s(.*)%s[ \t]*$
		 *         - Initial version
		 *  - 2: (?m)%s[ \t]*%s([a-zA-Z]+)[ \t]*%s(.*)%s[ \t]*$
		 *         - Removed whitespace at beginning so that part will be a RawTemplateSection.
		 *  - 3: (?m)^[ \t]*%s[ \t]*%s([a-zA-Z]+)[ \t]*%s(.*)%s[ \t]*$\r?
		 *         - Added whitespace at beginning and added carriage return at the end (this solved empty lines issue in XSLT).
		 *  - 4: (?m)^[ \t]*%s[ \t]*%s([a-zA-Z]+)[ \t]*%s(.*)%s[ \t]*$\r?\n
		 *         - Added new line to the end.
		 *  - 5: (?m)^[ \t]*%s[ \t]*%s([a-zA-Z]+)[ \t]*%s(.*)%s[ \t]*%s[ \t]*$\r?\n?
		 *         - Added annotation text suffix as parameter and made newline optional
		 */
		Pattern compiledPattern = Pattern.compile(
				String.format(
						"(?m)^[ \\t]*%s[ \\t]*%s([a-zA-Z]+)[ \\t]*%s(.*)%s[ \\t]*%s[ \\t]*$\\r?\\n?",
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationTextPrefix)),
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationPrefix)),
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationArgsPrefix)),
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationArgsSuffix)),
						Pattern.quote(TemplatePreprocessor.doubleEntityEncode(annotationTextSuffix))
				)
		);
		
		// Collect the annotations and return them based on the pattern.
		return collectAnnotations(templateContent, compiledPattern);
	}
	
	/**
	 * Private method for collecting annotation using the template part to scan and a pattern.
	 * The pattern must contain 2 groups, the first being the annotation name and the second the annotation params.
	 * @param templateContent The template part to scan.
	 * @param pattern The pattern to use while scanning.
	 * @return The annotations found.
	 * @throws TemplatePreprocessorException
	 */
	private static ArrayList<TemplateAnnotation> collectAnnotations(String templateContent, Pattern pattern) throws TemplatePreprocessorException {
		ArrayList<TemplateAnnotation> annotations = new ArrayList<>();
		
		// Create the matcher.
		Matcher matcher = pattern.matcher(templateContent);
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

}
