package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessor;

/**
 * The RootTemplateConfig part of CrossBreeze configuration.
 * It contains configuration for:
 *  - Output
 *  
 * @author Harmen
 */
@XmlTransient
public abstract class RootTemplateConfig extends AbstractTemplateConfig {
	/**
	 * The Output configuration for the Template.
	 * @see OutputConfig
	 */
	private OutputConfig _outputConfig;
	
	/**
	 * @return the output
	 */
	@XmlElement(name="Output", required=true)
	public OutputConfig getOutputConfig() {
		return _outputConfig;
	}

	/**
	 * @param outputConfig the output to set
	 */
	public void setOutputConfig(OutputConfig outputConfig) {
		this._outputConfig = outputConfig;
	}
	
	/**
	 * Function to get the preprocessor for the template type.
	 * @param templateConfig The configuration of the template.
	 * @return The template pre-processor for the TemplateType.
	 */
	public abstract TemplatePreprocessor getTemplatePreprocessor(XGenConfig config);

}
