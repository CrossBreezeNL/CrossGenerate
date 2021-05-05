package com.xbreeze.xgenerate.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * A namespace existing in the model that is needed to perform XSLT on the model.
 */
// Set the order of the properties.
@XmlType(propOrder={"prefix", "namespace"})
public class NamespaceConfig {
	private String prefix;

	private String namespace;
	
	/**
	 * @return the namespace prefix
	 */
	@XmlAttribute(required=true)
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix The namespace prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return the namespace IRI
	 */
	@XmlAttribute(required=true)
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @param namespace the namespace IRI to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	/**
	 * Construct a namespace declaration for XSL stylesheet.
	 * @return the namespace declaration
	 */
	public String getNamespaceDeclaration() {
		StringBuilder sb = new StringBuilder();
		sb.append("xmlns");
		if (this.prefix != null && this.prefix.trim().length() > 0) {
			sb.append(":").append(this.prefix.trim());
		}
		sb.append("=\"").append(this.namespace).append("\"");
		return sb.toString();				
	}
}
