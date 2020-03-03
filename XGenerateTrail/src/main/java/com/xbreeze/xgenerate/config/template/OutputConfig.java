package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * The Output part of the Template configuration.
 * One can specify the output type here.
 * 
 * @author Harmen
 */
public class OutputConfig {
	/**
	 * Configuration enum which specifies the output type.
	 *  - output_per_element - will results in 1 output for each element mapped to the root section of the template.
	 *  - single_output - will results in 1 output in total, combining all translations from elements into the template into 1 output.
	 */
	public enum OutputType {
		output_per_element,
		single_output
	}
	
	/**
	 * The OutputType configuration.
	 * @see OutputType
	 */
	private OutputType _type;

	/**
	 * @return the type
	 */
	@XmlAttribute(required=true)
	public OutputType getType() {
		return _type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(OutputType type) {
		this._type = type;
	}
}
