/*******************************************************************************
 *   Copyright (c) 2024 CrossBreeze
 *
 *   This file is part of CrossGenerate.
 *
 *      CrossGenerate is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      CrossGenerate is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with CrossGenerate.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *      Willem Otten - CrossBreeze
 *      Harmen Wessels - CrossBreeze
 *      Jacob Siemaszko - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.utils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;

import com.xbreeze.xgenerate.config.NamespaceConfig;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.namespace.NamespaceContext;

import net.sf.saxon.xpath.XPathEvaluator;
import net.sf.saxon.xpath.XPathExpressionImpl;
import net.sf.saxon.xpath.XPathFactoryImpl;

public class SaxonXMLUtils {
	
	private XPathEvaluator _xpath = null;

	/**
	 * Transform an XML Document object into a string representation.
	 * @param doc The Document object.
	 * @return The string representation of the XML Document.
	 * @throws XmlException
	 */
	public static String XmlDocumentToString(Document doc) throws XmlException {
		//Transform the preprocessed document to string and store it in the model object.
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.transform(domSource, result);			
		} catch (TransformerException e) {
			throw new XmlException(String.format("Error transforming document to string: %s", e.getMessage()));
		}
		return writer.toString();
	}
	
	/**
	 * @return Return the XPathEvaluator, if it is not yet initialized, create a new one
	 */
	private XPathEvaluator getXPathEvaluator() {
		if (this._xpath == null) {
			XPathFactoryImpl xPathfactory = new XPathFactoryImpl();
			this._xpath = (XPathEvaluator) xPathfactory.newXPath();
		}
		return this._xpath;
	}
	
	/**
	 * Evaluate the xpath expression and return the result as an XPathExpressionImpl object 
	 * @param xPathExpression
	 * @return
	 * @throws XPathExpressionException
	 */
	public XPathExpressionImpl getXPathExpression(String xPathExpression) throws XPathExpressionException {
		return(XPathExpressionImpl)this.getXPathEvaluator().compile(xPathExpression); 
	}
	
	public void setNamespaces(ArrayList<NamespaceConfig> namespaces) {
		// If namespaces are defined, create a namespace context object and set it on the xpath object.
		if (namespaces != null && namespaces.size() > 0) {
			ModelNamespaceContext nsContext = new ModelNamespaceContext(namespaces);
			this.getXPathEvaluator().setNamespaceContext(nsContext);
		}
	}
	
	/**
	 * Helper class to deal with namespace aware preprocessing.
	 */
	private static class ModelNamespaceContext implements NamespaceContext {
		
		private ArrayList<NamespaceConfig> namespaces;
		
		public ModelNamespaceContext (ArrayList<NamespaceConfig> namespaces) {
			this.namespaces = namespaces;
		}
		
		@Override
		public String getNamespaceURI(String prefix) {
			for (NamespaceConfig ns: this.namespaces) {
				if (ns.getPrefix().equalsIgnoreCase(prefix)) {
					return ns.getNamespace();
				}
			}
			return null;
		}

		@Override
		public String getPrefix(String namespaceURI) {
			for (NamespaceConfig ns: this.namespaces) {
				if (ns.getNamespace().equalsIgnoreCase(namespaceURI)) {
					return ns.getPrefix();
				}
			} 
			return null;
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceURI) {
			// TODO Not implemented/used at the moment.
			return null;
		}
		
	}
}
