package com.xbreeze.xmlutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

public class Tester {

	public static void main(String[] args) {
		Path xmlFile = Path.of("./test.xml");
		String xmlContent ="";
		try {
			System.out.println( new java.io.File( "." ).getCanonicalPath());
			xmlContent = Files.readString(xmlFile);
		}
		catch (IOException exc) {
			System.out.println("Error reading xml file: " + exc.getMessage());
			return;
		}
		
		//Read xmlContent into XMLObject
		try {
		XmlObject doc = XmlObject.Factory.parse(xmlContent);
		XmlCursor cursor = doc.newCursor();
		cursor.toFirstContentToken();
		cursor.toFirstChild();		
		System.out.println(cursor.getName());
		}
		catch (XmlException exc) {
			System.out.println("Error parsing xml: " + exc.getMessage());
		}
	}

}
