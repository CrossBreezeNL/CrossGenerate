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
			throw new XmlException(
				String.format("Error transforming document to string: %s", e.getMessage())
				);
		}
		return writer.toString();
	}
	
	//Evaluate the xpath expression and return the result as an XPathExpressionImpl object
	public XPathExpressionImpl getXPathExpression(String xPathExpression) throws XPathExpressionException {
		return(XPathExpressionImpl)this.getXPathEvaluator().compile(xPathExpression); 
	}
		
	//Return the XPathEvaluator, if it is not yet initialized, create a new one
	public XPathEvaluator getXPathEvaluator() {
		if (this._xpath == null) {
			XPathFactoryImpl xPathfactory = new XPathFactoryImpl();
			this._xpath = (XPathEvaluator) xPathfactory.newXPath();
		}
		return this._xpath;
	}
	
	public void setNamespaces(ArrayList<NamespaceConfig> namespaces) {
		// If namespaces are defined, create a namespace context object and set it on the xpath object.
		if (namespaces != null && namespaces.size() > 0) {
			ModelNamespaceContext nsContext = new ModelNamespaceContext(namespaces);
			this.getXPathEvaluator().setNamespaceContext(nsContext);
		}
	}
	
	// helper class to deal with namespace aware preprocessing
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
				// TODO Auto-generated method stub
				return null;
			}
			
		}
}
