package com.xbreeze.xgenerate.template.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;

/***
 * Class for storing the nodes that match with a templatesection annotations' elementXPath in a certain XML template document.
 * @author Willem
 *
 */
public class XMLTemplateSectionWithNodes {
	
	/***
	 * The name of the section
	 */
	private TemplateSectionAnnotation _templateSectionAnnotation;
	
	/***
	 * The nodes that match the xpath
	 */
	private NodeList _nodes;
	
	/***
	 * Constructor, initializes the object and performs xpath on specified document.
	 * @param name the name of the section
	 * @param elementXPath the xpath to perform
	 * @throws TemplatePreprocessorException 
	 * @document the document to perform the xpath against.
	 */
	public XMLTemplateSectionWithNodes(TemplateSectionAnnotation tsa, NodeList nodes) {
		this._templateSectionAnnotation = tsa;
		this._nodes = nodes;
	}
	
	/***
	 * Method verifies if the node passed as an argument is in the nodelist of this section
	 * @param node the node that is evaluated against to the nodelist
	 * @return true if the node is found in the nodelist, false otherwise
	 */
	public boolean containsNode(Node node) {
		if (_nodes == null) {
			return false;
		}
		for (int i=0; i < _nodes.getLength();i++) {
			if (_nodes.item(i).equals(node))
				return true;
		}
		return false;
	}
	
	/***
	 * Returns the name of this section
	 * @return name of the section
	 */
	public String getName() {
		return this._templateSectionAnnotation.getName();
	}

	/**
	 * @return the templateSectionAnnotation
	 */
	public TemplateSectionAnnotation getTemplateSectionAnnotation() {
		return _templateSectionAnnotation;
	}

	/**
	 * @return the nodes
	 */
	public NodeList getNodes() {
		return _nodes;
	}
}
