package com.xbreeze.xgenerate.template;

import java.net.URI;
import java.util.logging.Logger;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.binding.SectionModelBindingConfig;
import com.xbreeze.xgenerate.config.template.TemplateConfig;
import com.xbreeze.xgenerate.template.annotation.UnknownAnnotationException;

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
		SectionModelBindingConfig[] rootSectionModelBindings = _config.getBindingConfig().getSectionModelBindingConfigs(rootSectionName);
		
		// Check whether there is only 1 root section model binding. If not, throw an exception.
		if (rootSectionModelBindings.length != 1) {
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
	
	public static String doubleEntityEncode(String input) {
		return input.replaceAll("&([a-zA-Z0-9]+;)", "&amp;$1");
	}
}
