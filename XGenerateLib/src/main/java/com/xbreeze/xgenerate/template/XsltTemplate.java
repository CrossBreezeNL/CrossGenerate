package com.xbreeze.xgenerate.template;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.config.binding.LiteralConfig;
import com.xbreeze.xgenerate.config.binding.PlaceholderConfig;
import com.xbreeze.xgenerate.config.binding.SectionModelBindingConfig;
import com.xbreeze.xgenerate.config.template.OutputConfig.OutputType;
import com.xbreeze.xgenerate.config.template.RootTemplateConfig;
import com.xbreeze.xgenerate.config.template.XMLTemplateConfig;

public class XsltTemplate {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(XsltTemplate.class.getName());
	
	private enum PlaceholderType {
		XSL_VALUE_OF,
		XSL_INLINE
	};
	
	/**
	 * The template buffer.
	 */
	private StringBuffer _templateBuffer;
	
	/**
	 * Track whether the template is finalized.
	 * This is used to give a warning when toString is used on an unfinalized template.
	 */
	private boolean _isFinalized = false;
	
	/**
	 * The output type of the template.
	 * This is used to decide on the result-document syntax.
	 */
	private OutputType _outputType;
	
	/**
	 * Constructor
	 * @param templateId
	 * @param templateFileLocation
	 * @param templateConfig
	 * @param outputFolder
	 * @param rootSectionModelBindingConfig
	 */
	public XsltTemplate(String templateId, String templateFileLocation, RootTemplateConfig templateConfig, String relativeOutputFileUri, SectionModelBindingConfig rootSectionModelBindingConfig) {
		this._templateBuffer = new StringBuffer();
		this._outputType = templateConfig.getOutputConfig().getType();
		
		// Initialize the template.
		initTemplate(templateId, templateFileLocation, templateConfig, relativeOutputFileUri, rootSectionModelBindingConfig);
	}
	
	/**
	 * Initialize the template, by creating the starting elements for the XSLT.
	 */
	private void initTemplate(String templateId, String templateFileName, RootTemplateConfig templateConfig, String relativeOutputFileUri, SectionModelBindingConfig rootSectionModelBindingConfig) {
		appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		// https://www.w3schools.com/xml/ref_xsl_el_stylesheet.asp
		appendLine("<xsl:stylesheet id=\"%s\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"3.0\">", templateId);
		
		// Set the output method based on the template type.
		// https://www.w3schools.com/xml/ref_xsl_el_output.asp
		// TODO: Maybe encoding should also be set in the FileFormatConfig.
//		if (templateConfig.getFileFormatConfig().getTemplateType().equals(TemplateType.xml)) {
//			//appendLine("<xsl:output method=\"xml\" version=\"1.0\" encoding=\"UTF-8\" indent=\"yes\" />");
//			//appendLine("<xsl:output method=\"xml\" version=\"1.0\" encoding=\"UTF-8\" indent=\"yes\" omit-xml-declaration=\"no\" />");
//			appendLine("<xsl:output method=\"text\" encoding=\"UTF-8\" indent=\"no\" />");
//		} else {
			appendLine("<xsl:output method=\"text\" encoding=\"UTF-8\" indent=\"no\" />");
		
//		}
		
		// Strip white space from all elements (from the model).
		appendLine("<xsl:strip-space elements=\"*\"/>\n");
		
		appendLine("<!-- Match the template on the root node and call the specific template. -->");
		appendLine("<xsl:template match=\"/\"><xsl:call-template name=\"%s\" /></xsl:template>\n", templateId);
		
		// Add the template part, here we don't include match, since this is handled by the first xsl:for-each.
		appendLine("<!-- The specific template based on the template files '%s' -->", templateFileName);
		append("<xsl:template name=\"%s\">", templateId);
		
		// Configure the result document.
		// See: https://www.saxonica.com/html/documentation/xsl-elements/result-document.html
		// If this is the root section, add the output document info.
		// If the output type is output_per_element, add the result-document directive.
		// TODO Use full template location (without config part)
		String fileNamePlaceholder = processPlaceholders(Paths.get(relativeOutputFileUri).resolve(templateId).toString(), rootSectionModelBindingConfig, templateConfig, PlaceholderType.XSL_INLINE, _outputType.equals(OutputType.output_per_element));
		// Add the for-each part on the model node to match the template on.
		String rootForEach = String.format("<xsl:for-each select=\"%s\">", rootSectionModelBindingConfig.getModelXPath());
		
		// For the single out we first write the result-document element and then the for-each.
		if (_outputType.equals(OutputType.single_output)) {
			append("<xsl:result-document method=\"text\" href=\"%s\">", fileNamePlaceholder);
			// Add the template match part on the model node to match the template on.
			append(rootForEach);
		}
		
		// For the output-per-element we first write the for-each and then the result-document.
		else if (_outputType.equals(OutputType.output_per_element)) {
			append(rootForEach);
			append("<xsl:result-document method=\"text\" href=\"%s\">", fileNamePlaceholder);
		}
	}
	
	public static String processPlaceholders(String templatePart, SectionModelBindingConfig parentBindingConfig, RootTemplateConfig templateConfig) {
		return processPlaceholders(templatePart, parentBindingConfig, templateConfig, PlaceholderType.XSL_VALUE_OF, true);
	}
	
	public static String processPlaceholders(String templatePart, SectionModelBindingConfig parentBindingConfig, RootTemplateConfig templateConfig, PlaceholderType placeholderType, boolean isBounded) {
		// Store the result in a local String.
		String processedTemplatePart = templatePart;
		
		// If the current section has a variable name defined, we use the variable i.s.o. a XPath expression.
		// The path of the placeholder of the current section is always the local element (so '.') if were are within the binded section.
		// If not bounded, we use the model XPath.
		String parentPlaceholderXPath = (parentBindingConfig.hasVariableName()) ? String.format("$%s", parentBindingConfig.getVariableName()) : (isBounded) ? "." : parentBindingConfig.getModelXPath();
		
		// Process the placeholder of this section.
		processedTemplatePart = XsltTemplate.processPlaceholder(
				parentBindingConfig.getPlaceholderName(),
				parentPlaceholderXPath,
				processedTemplatePart,
				templateConfig,
				placeholderType
		);
		
		// Loop through the placeholders defined in this section binding and process them.
		if (parentBindingConfig.getPlaceholderConfigs() != null) {
			for (PlaceholderConfig placeholder : parentBindingConfig.getPlaceholderConfigs()) {
				processedTemplatePart = XsltTemplate.processPlaceholder(
						placeholder.getName(),
						// If a variable name is defined for the placeholder, use the variable i.s.o. the model XPath.
						(placeholder.hasVariableName()) ? String.format("$%s", placeholder.getVariableName()) : placeholder.getModelXPath(),
						processedTemplatePart,
						templateConfig,
						placeholderType
				);
			}
		}
		
		return processedTemplatePart;
	}
	
	/**
	 * Function to process a placeholder.
	 * @param placeholderName The placeholder name.
	 * @param modelXPath The model XPath
	 * @param templatePartToProcess The template part to process
	 * @param fileFormatConfig The file format config.
	 * @param placeholderType The placeholder type.
	 * @return The processed template, where placeholders are replaced with XSLT expressions.
	 */
	private static String processPlaceholder(String placeholderName, String modelXPath, String templatePartToProcess, RootTemplateConfig templateConfig, PlaceholderType placeholderType) {
		String processedTemplate = templatePartToProcess;
		
		/**
		 * The regex to find a placeholder:
		 * %s            - The placeholder name
		 * %s            - The node accessor
		 * ([a-zA-Z]+)   - The attribute name to select
		 */
		String placeholderRegex =  String.format(
				"%s%s([a-zA-Z]+)",
				placeholderName,
				"%s"
		);
		
		// Perform placeholder replacement for the current accessor.
		/** 
		 * replacement-regex:
		 * %s   - The model XPath expression for the placeholder.
		 * /    - A slash to select something from the element at the level of %s.
		 * @    - The XML attribute accessor.
		 * $1   - The value of group 1 (the attribute name to select from the find regex)
		 */
		String currentAccessorPlaceholderRegex = String.format(placeholderRegex, Pattern.quote(templateConfig.getFileFormatConfig().getCurrentAccessor()));
		processedTemplate = replacePlaceholders(processedTemplate, currentAccessorPlaceholderRegex, "%s/@$1", modelXPath, placeholderType, templateConfig);
		
		// Perform placeholder replacement for the child accessor.
		if (templateConfig.getFileFormatConfig().getChildAccessor() != null && templateConfig.getFileFormatConfig().getChildAccessor().length() > 0) {
			/**
			 * replacement-regex:
			 * %s   - The model XPath expression for the placeholder.
			 * /    - A slash to select something from the element at the level of %s.
			 * $1   - The value of group 1 (the attribute name to select from the find regex)
			 */
			String childAccessorPlaceholderRegex = String.format(placeholderRegex, Pattern.quote(templateConfig.getFileFormatConfig().getChildAccessor()));
			processedTemplate = replacePlaceholders(processedTemplate, childAccessorPlaceholderRegex, "%s/$1", modelXPath, placeholderType, templateConfig);
		}
		
		return processedTemplate;
	}
	
	/**
	 * Replace the placeholders in the templatePartToProcess.
	 * @param templatePartToProcess The template part to process.
	 * @param placeholderRegex The regex for finding placeholders.
	 * @param placeholderReplaceXPath The replacement value for the placeholders.
	 * @param modelXPath The XPath to the model.
	 * @param placeholderType The placeholder type.
	 * @return The processed template part, where placeholders are replaced wit the XSLT instruction.
	 */
	private static String replacePlaceholders(String templatePartToProcess, String placeholderRegex, String placeholderReplaceXPath, String modelXPath, PlaceholderType placeholderType, RootTemplateConfig templateConfig) {
		// Preprocess the placeholder replace XPath.
		String preprocessedPlaceholderReplaceXPath = placeholderReplaceXPath;
		// When processing an XML template, double encode xml entities so the entities are still escaped after XSLT
		// TODO make this more generic xsl replace is supposed to support regex but didn't seem to work well with & also &apos; is not escaped (gave an error)
		if (templateConfig instanceof XMLTemplateConfig) {
			preprocessedPlaceholderReplaceXPath = String.format("replace(replace(replace(replace(%s,'&amp;','&amp;amp;'), '&lt;','&amp;lt;'),'&gt;','&amp;gt;'),'&quot;','&amp;quot;')", placeholderReplaceXPath);
		}
		
		String placeholderFormat;
		switch (placeholderType) {
			case XSL_INLINE:
				/**
				 * The xsl inline replacement value for the placeholder:
				 * {<replacement-regex>}
				 */
				placeholderFormat = String.format("{%s}", preprocessedPlaceholderReplaceXPath);
				break;
			// In all other cases return the value-of version.
			case XSL_VALUE_OF:
			default:
				/**
				 * The xsl:value-of replacement value for the placeholder:
				 * <xsl:value-of select=\"<placeholder-name>/<model-node>\" />
				 */
				placeholderFormat = String.format("</xsl:text><xsl:value-of select=\"%s\" /><xsl:text>", preprocessedPlaceholderReplaceXPath);
				break;
		}
		
		// Group 1: The attribute name.
		// Replace the $ in a modelXPath with \\$ so it's escape in the regex while executing the replaceAll.
		String placeholderReplacement = String.format(placeholderFormat, modelXPath.replace("$", "\\$"));

		// Perform the replacement for the placeholder.
		//logger.info(String.format("Processing placeholder '%s' -> %s", placeholderRegex, placeholderReplacement));
		return templatePartToProcess.replaceAll(placeholderRegex, placeholderReplacement);		
	}
	
	/**
	 * Function to process the literals defined for a SectionModelBinding.
	 * @param templatePart The template part to process.
	 * @param parentBindingConfig The SectionModelBinding config for the section of the template part.
	 * @param templateConfig The root template config.
	 * @return The processed template part.
	 */
	public static String processLiterals(String templatePart, SectionModelBindingConfig parentBindingConfig, RootTemplateConfig templateConfig) {
		// If there are not literal configs for this section, return the templatePart directly.
		if (parentBindingConfig.getLiteralConfigs() == null)
			return templatePart;
			
		// Store the result in a local String.
		String processedTemplatePart = templatePart;
		
		// Loop through the literal configs and apply them to the template part.
		for (LiteralConfig literal : parentBindingConfig.getLiteralConfigs()) {
			/** 
			 * replacement-regex:
			 * %s   - The model XPath expression for the placeholder.
			 */
			String literalRegex = Pattern.quote(literal.getLiteral());
			processedTemplatePart = replacePlaceholders(processedTemplatePart, literalRegex, "%s", literal.getModelXPath(), PlaceholderType.XSL_VALUE_OF, templateConfig);
		}
		
		return processedTemplatePart;
	}
	
	/**
	 * Finalize the template by closing the xsl element.
	 */
	public void finalizeTemplate() {
		
		// If the output is per element, close the result-document before the for-each.
		if (_outputType.equals(OutputType.output_per_element)) {
			append("</xsl:result-document>");
			append("</xsl:for-each>");
		}
		// If the output is a single-output close the result-document after the for-each.
		else if (_outputType.equals(OutputType.single_output)) {
			append("</xsl:for-each>");
			append("</xsl:result-document>");
		}
		
		appendLine("</xsl:template>");
		appendLine("<!-- End of the specific template. -->");
		appendLine("");
		append("</xsl:stylesheet>");
		
		// Set the status of isFinalized to true.
		_isFinalized = true;
	}
	
	/**
	 * Append the content of str to the template buffer.
	 * @param str The text to append.
	 */
	public void append(String str) {
		_templateBuffer.append(str);
	}
	
	/**
	 * Append a string using the template and the args.
	 * @param template The string template.
	 * @param args The args as an input for the String.format.
	 */
	public void append(String template, Object... args) {
		append(String.format(template, args));
	}
	
	/**
	 * Append the content of str with a line separator to the template buffer.
	 * @param str The content of the line to append.
	 */
	public void appendLine(String str) {
		_templateBuffer.append(str).append(System.lineSeparator());
	}
	
	/**
	 * Append a string with a newline separator using the template and the args.
	 * @param template The string template.
	 * @param args The args as an input for the String.format.
	 */
	public void appendLine(String template, Object... args) {
		appendLine(String.format(template, args));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// If the template is not finalized yet, give a warning.
		if (!_isFinalized)
			logger.log(Level.WARNING, "The toString() method is called on a PreprocessedTemplate, but it's not finalized yet!");
		
		return _templateBuffer.toString();
	}
}
