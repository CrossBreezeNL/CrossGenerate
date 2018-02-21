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
	 * Static method for scanning for annotations
	 * @param templateContent the template part that is searched for annotations
	 * @return returns the list of annotations found in the text.
	 * @throws TemplatePreprocessorException 
	 */
	public static ArrayList<TemplateAnnotation> collectAnnotations(String templateContent, String annotationTextPrefix, String annotationPrefix, String annotationArgsPrefix, String annotationArgsSuffix, String annotationTextSuffix) throws TemplatePreprocessorException {
		ArrayList<TemplateAnnotation> annotations = new ArrayList<>();
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
						Pattern.quote(annotationTextPrefix),
						Pattern.quote(annotationPrefix),
						Pattern.quote(annotationArgsPrefix),
						Pattern.quote(annotationArgsSuffix),
						Pattern.quote(annotationTextSuffix)
				)
		);
		// Create the matcher.
		Matcher matcher = compiledPattern.matcher(templateContent);
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
					// Add the template annotation to the list.
					annotations.add(templateAnnotation);
				} catch (UnhandledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (AnnotationException e) {
				throw new TemplatePreprocessorException(e);
			}
		}
		
		return annotations;
	}

}
