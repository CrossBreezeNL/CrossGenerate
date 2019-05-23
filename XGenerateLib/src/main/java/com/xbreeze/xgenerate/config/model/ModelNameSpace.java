package com.xbreeze.xgenerate.config.model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * A namespace existing in the model that is needed to perform XSLT on the model.
 */

public class ModelNameSpace {
	private String alias;

	private String url;
	
	/**
	 * @return the nameSpace Alias
	 */
	@XmlAttribute(required=true)
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the Namespace alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	/**
	 * @return the nameSpace URL
	 */
	@XmlAttribute(required=true)
	public String getUrl() {
		return url;
	}

	/**
	 * @param nameSpaceURL the Namespace URL to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Construct a namespace declaration for XSL stylesheet.
	 * @return the namespace declaration
	 */
	public String getNameSpaceDeclaration() {
		StringBuilder sb = new StringBuilder();
		sb.append("xmlns");
		if (this.alias != null && this.alias.trim().length() > 0) {
			sb.append(":").append(this.alias.trim());
		}
		sb.append("=\"").append(this.url).append("\"");
		return sb.toString();				
	}
}
