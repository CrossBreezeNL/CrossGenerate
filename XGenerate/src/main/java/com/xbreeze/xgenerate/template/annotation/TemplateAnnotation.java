package com.xbreeze.xgenerate.template.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionAction;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionStyle;

/**
 * The abstract TemplateAnnotation class which should be extends by all annotations supported by CrossGenerate.
 * 
 * @author Harmen
 */
// Don't create a attribute or element for fields or get methods by default.
// All are explicitly defined.
@XmlAccessorType(XmlAccessType.NONE)
public abstract class TemplateAnnotation implements Comparable<TemplateAnnotation> {
	// The logger for this class.
	protected static final Logger logger = Logger.getLogger(TemplateAnnotation.class.getName());
	
	/**
	 * The starting character index of the annotation.
	 */
	protected Integer _annotationBeginIndex = -1;
	
	/**
	 * The ending character index of the annotation.
	 */
	protected Integer _annotationEndIndex = -1;
	
	/**
	 * Whether the annotation is defined in the template.
	 */
	protected boolean _definedInTemplate;
	
	/**
	 * Constructor.
	 */
	public TemplateAnnotation() { }
	
	/**
	 * Get the name of the annotation.
	 * @return The name of the annotation.
	 */
	public String getAnnotationName() {
		String className = this.getClass().getSimpleName();
		return className.substring("Template".length(), className.length() - "Annotation".length());
	}

	/**
	 * Create a TemplateAnnotation object using the annotation name and parameters.
	 * @param name The annotation name.
	 * @param params The annotation parameters.
	 * @return The TemplateAnnotation objects (as a subclass of TemplateAnnotation).
	 * @throws AnnotationException When there is something wrong with the annotation
	 * @throws UnhandledException When something unexpected happened.
	 */
	public static TemplateAnnotation fromName(String annotationName, String annotationParams, int charIndexStart, int charIndexEnd) throws AnnotationException, UnhandledException  {
		
		// Construct the class name for the annotation (Template<Name>Annotation).
		// It expects the class to be in the same package as TemplateAnnotation.
		String annotationTypeClassName = String.format("%s.Template%sAnnotation", TemplateAnnotation.class.getPackage().getName(), annotationName);
		logger.fine(String.format("Trying to initialize object from class '%s'", annotationTypeClassName));
		
		// Now try to find the class and initiate one.
		try {
			// Get the class for the annotation name (as subclass of TemplateAnnotation).
			Class<? extends TemplateAnnotation> annotationTypeClass = Class.forName(annotationTypeClassName).asSubclass(TemplateAnnotation.class);
			// Instantiate the annotation class.
			TemplateAnnotation templateAnnotation = annotationTypeClass.getConstructor().newInstance();
			// When this method (fromName) is called the annotation was defined in the template.
			templateAnnotation.setDefinedInTemplate(true);
			// Set the start and end char index on the template annotation.
			templateAnnotation.setAnnotationBeginIndex(charIndexStart);
			templateAnnotation.setAnnotationEndIndex(charIndexEnd);
			
			// Get an array with the set methods, to use for finding the right set method.
			// Use getDeclaredMethods will only return the declared methods in that class (no the superclasses).
			Method[] annotationSetMethods = Arrays.stream(annotationTypeClass.getDeclaredMethods()).filter(m -> m.getName().startsWith("set")).toArray(Method[]::new);
			
			// If the annotation class only has 1 set method and its name is set<annotation-name>, we call this method with the full params string.
			// This is for example for the Comment annotation, where the whole params content is the comment.
			if (annotationSetMethods.length  == 1 && annotationSetMethods[0].getName().equalsIgnoreCase(getSetMethodName(annotationName))) {
				logger.fine(String.format("Only 1 argument allowed for the annotation, so whole params value is assigned to %s.", annotationName));
				// Invoke the set method on the parameter named the same as the annotation name.
				invokeSetMethod(templateAnnotation, annotationSetMethods, annotationName, annotationParams);
			}
			
			// Otherwise we loop through the params and call the right set method.
			else {
				
				// Find the argument name-value pairs.
				/**
				 * [ \t]*                         -> Any space or tab characters
				 * ,?                             -> A comma between parameters (optional)
				 * [ \t]*                         -> Any space or tab characters
				 * (?<paramName>[a-z]+)           -> The name of the parameter (region 1: paramName)
				 * [ \t]*                         -> Again, any space or tab characters
				 * =                              -> The equals sign
				 * [ \t]*                         -> Again, any space or tab characters
				 * (                              -> Start of parameter value (region 2)
				 * (?<simpleParamValue>[0-9a-z]+) -> Value without quotes. (region 3: simpleParamValue)
				 * |                              -> Choice between parameter value with or without quotes.
				 * ('|"|\Q&quot;\E)               -> Single quote, Double quote, XML single quote, start of the param value (optional) (region 4)
				 * (?<complexParamValue>.+?)      -> Any character except the one in region 4 (? so it does lazy matching). (region 5: complexParamValue)
				 * \4                             -> Must be same as region 4.
				 * )                              -> End of parameter value (end of region 2)
				 */
				Pattern compiledPattern = Pattern.compile(
						"[ \\t]*,?[ \\t]*(?<paramName>[a-z]+)[ \\t]*=[ \\t]*((?<simpleParamValue>[0-9a-z]+)|('|\"|\\Q&quot;\\E)(?<complexParamValue>.+?)\\4)",
						// Match case insensitive.
						Pattern.CASE_INSENSITIVE
				);
				Matcher matcher = compiledPattern.matcher(annotationParams);

				// Store the previous end match index to check whether we aren't skipping bits.
				int previousEndMatchIndex = 0;
				// For every name-value pair we invoke the set method on the template annotation object.
				while (matcher.find()) {
					
					if (matcher.start() != previousEndMatchIndex)
						throw new AnnotationException(String.format("Part of annotation params found which is not according to the expected format: %s (%d:%d)", annotationParams.substring(previousEndMatchIndex, matcher.start()), previousEndMatchIndex, matcher.start()));
					
					// Group 1: The name of the annotation.
					String paramName = matcher.group("paramName");
					// Group 3/5: The arguments for the annotation.
					String paramValue = (matcher.group("simpleParamValue") != null) ? matcher.group("simpleParamValue") : matcher.group("complexParamValue");
					logger.fine(String.format("Found annotation param key-value pair (%s='%s')", paramName, paramValue));
					
					// Invoke the set method.
					invokeSetMethod(templateAnnotation, annotationSetMethods, paramName, paramValue);
					
					// Store the current end index in the variable.
					previousEndMatchIndex = matcher.end();
				}
				
				if (annotationParams.length() > 0 && previousEndMatchIndex != annotationParams.length())
					throw new AnnotationException(String.format("Params part of annotation couldn't be parsed: '%s'", annotationParams.substring(previousEndMatchIndex)));
				
			}
			
			// Return the template annotation.
			return templateAnnotation;
		} catch (ClassNotFoundException e) {
			// When we get into this catch, the user specified an unspecified annotation.
			throw new UnknownAnnotationException(annotationName, e);
		} catch (InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new UnhandledException(String.format("Error while initializing class for '%s', maybe default constructor missing?", annotationName), e);
		} catch (IllegalAccessException | ClassCastException e) {
			// ClassCastException can only occur when the annotation class is not a subclass of TemplateAnnotation, which shouldn't occur.
			throw new UnhandledException(e);
			// This should never happen as long as there is a constructor without parameters.
		}
	}
	
	/**
	 * Get the name of the set method name for a parameter.
	 * @param paramName The name of the parameter.
	 * @return The set method name.
	 */
	private static String getSetMethodName(String paramName) {
		return String.format("set%s", paramName);
	}
	
	/**
	 * Invoke the set method for a parameter in a  template annotation.
	 * @param templateAnnotation The template annotation
	 * @param annotationSetMethods The array with set methods (for re-use it is stored in the calling function)
	 * @param paramName The parameter name
	 * @param paramValue The parameter value
	 * @throws AnnotationException When there is something wrong with the annotation
	 * @throws UnhandledException When something unexpected happened
	 */
	private static void invokeSetMethod(TemplateAnnotation templateAnnotation, Method[] annotationSetMethods, String paramName, String paramValue) throws AnnotationException, UnhandledException {
		// Get the method to execute.
		Optional<Method> optionalParamSetMethod = Arrays.stream(annotationSetMethods).filter(f -> f.getName().equalsIgnoreCase(getSetMethodName(paramName))).findFirst();
		
		// If the method was not found, throw an exception.
		if (!optionalParamSetMethod.isPresent()) {
			// When a parameter name was specified for which there is no set<Param-name> method.
			throw new UnknownAnnotationParamException(templateAnnotation.getAnnotationName(), paramName);
		}
		
		// Get the Method object.
		Method setMethod = optionalParamSetMethod.get();
		
		// The set method must have 1 argument.
		if (setMethod.getParameterCount() != 1) {
			// For some reason the set method does not have 1 argument. This is wrong!
			throw new AnnotationException(String.format("The annotation set method doesn't have exactly 1 argument (%s -> %s)", templateAnnotation.getAnnotationName(), setMethod.getName()));
		}
		
		// Create an object to store the parameter value in.
		Object paramValueObject;
			
		// Get the first parameter.
		Parameter setValueParam = setMethod.getParameters()[0];
		// Get the type of the parameter.
		Type paramValueType = setValueParam.getParameterizedType();

		// Try to assign the right object type to the value object.
		// String
		if (String.class.equals(paramValueType)) {
			// The paramValue is already a String, so we can re-use the object.
			paramValueObject = paramValue;
		}
		// Integer
		else if (int.class.equals(paramValueType) || Integer.class.equals(paramValueType)) {
			try {
				// When the target type is an Integer, we need to parse the String into an int.
				paramValueObject = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				// When the parse failed, we throw an exception.
				throw new IncorrectParamValueException(templateAnnotation.getAnnotationName(), paramName, paramValue, paramValueType.toString());
			}
		}
		// Boolean
		else if (boolean.class.equals(paramValueType) || Boolean.class.equals(paramValueType)) {
			paramValueObject = Boolean.parseBoolean(paramValue);
		}
		// RepetitionStyle
		else if (RepetitionStyle.class.equals(paramValueType)) {
			paramValueObject = RepetitionStyle.valueOf(paramValue);
		}
		// RepetitionAction
		else if (RepetitionAction.class.equals(paramValueType)) {
			paramValueObject = RepetitionAction.valueOf(paramValue);
		}		
		// When we have a set method with a parameter type we don't support yet, we throw an exception.
		else {
			throw new AnnotationException(String.format("The annotation set method parameter type is not supported (%s -> %s -> %s)", templateAnnotation.getAnnotationName(), setMethod.getName(), paramValueType.toString()));
		}
		
		// Invoke the set method with the param value.
		try {
			setMethod.invoke(templateAnnotation, paramValueObject);
		} catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
			// This shouldn't occur.
			throw new UnhandledException(e);
		}
	}
	
	@Override
	public int compareTo(TemplateAnnotation otherTemplateAnnotation) {
		return this.getAnnotationBeginIndex() - otherTemplateAnnotation.getAnnotationBeginIndex();
	}
	
	/**
	 * @return the annotationBeginIndex
	 */
	public int getAnnotationBeginIndex() {
		return _annotationBeginIndex;
	}

	/**
	 * @param annotationBeginIndex the annotationBeginIndex to set
	 */
	public void setAnnotationBeginIndex(int annotationBeginIndex) {
		this._annotationBeginIndex = annotationBeginIndex;
	}

	/**
	 * @return the annotationEndIndex
	 */
	public int getAnnotationEndIndex() {
		return _annotationEndIndex;
	}

	/**
	 * @param annotationEndIndex the annotationEndIndex to set
	 */
	public void setAnnotationEndIndex(int annotationEndIndex) {
		this._annotationEndIndex = annotationEndIndex;
	}
	

	/**
	 * @return the definedInTemplate
	 */
	public boolean isDefinedInTemplate() {
		return _definedInTemplate;
	}

	/**
	 * @param definedInTemplate the definedInTemplate to set
	 */
	public void setDefinedInTemplate(boolean definedInTemplate) {
		this._definedInTemplate = definedInTemplate;
	}
}
