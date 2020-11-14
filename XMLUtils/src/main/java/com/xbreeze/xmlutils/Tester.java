package com.xbreeze.xmlutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Stack;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

public class Tester {

	public static void main(String[] args) {
		XmlObject doc = null;
		
		try {
			System.out.println( new java.io.File( "." ).getCanonicalPath());
			doc = getXmlObject("./test.xml");
			resolveIncludes(doc);
			//Output result to output
			System.out.println(doc.toString());
			
		}
		catch (XmlUtilException | IOException exc) {
			System.out.println("Error reading xml file: " + exc.getMessage());
			return;
		}		
	}
	
	public static XmlObject resolveIncludes(XmlObject xmlDoc) throws XmlUtilException {
		//XPath over all include nodes
		XmlCursor rootCur= xmlDoc.newCursor();
		//Need to declare namespace in xpath since the namespace declaration is not picked up from the xml document
		//https://stackoverflow.com/questions/35501300/xpath-command-with-declaring-multiple-namespaces
		
		//Get all locations and store all xml objects
		Stack<XmlObject> includes = new Stack<>();
		rootCur.selectPath("declare namespace xi='http://www.w3.org/2001/XInclude' .//xi:include");
		while (rootCur.toNextSelection()) {
			XmlObject childDoc = getXmlObject(rootCur.getAttributeText(new QName("src")));
			rootCur.push();
			includes.push(childDoc);
		}
		
		//Now reversed order process all locations
		while (rootCur.pop()) {
			//Remove the include node
			rootCur.removeXml();
			//insert the included content
			XmlCursor includeCur = includes.pop().newCursor();
			//Move cursor to first XML element in the doc
			if (includeCur.toFirstChild()) {
				includeCur.copyXml(rootCur);			
			}
		}
		// Stmt below gives a Saxon namespace error
		/*
		 * XmlCursor includeCur = rootCur.execQuery("//xi:include");
		 *
		while (includeCur.hasNextToken()) {
			TokenType t = includeCur.toNextToken();
			System.out.println(t.toString());
			
		}*/
		return xmlDoc;
	}
	
	
	//Read the contents of a file into an Xml Object
	public static XmlObject getXmlObject(String fileName) throws XmlUtilException{
		File f = new File(fileName);
		try {
			return XmlObject.Factory.parse(f);
		} 
		catch (IOException | XmlException exc) {
			throw new XmlUtilException(String.format("Error reading file %s: %s", fileName, exc.getMessage()));
		}
		
	}
		

}
