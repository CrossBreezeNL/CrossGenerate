package com.xbreeze.xgenerate.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import com.xbreeze.xgenerate.UnhandledException;
import com.xbreeze.xgenerate.generator.Generator;
import com.xbreeze.xgenerate.generator.GeneratorException;

public class CrossGenerateTest {
	public static void main(String[] args) {
		
		// Execute the Microsoft SQL test.
		doMicrosoftSQLTest();
		
		// Execute the Microsoft SSIS test.
		//doMicrosoftSSISTest();
		
		// Execute the PowerCenter test.
		//doPowerCenterTest();
		
		// Execute the IBM DataStage test.
		//doDataStageTest();
		
	}
	
	@SuppressWarnings("unused")
	private static void doMicrosoftSQLTest() {
		Generator generator = new Generator();
		
		String modelFileName = "common/model/ExampleModel.xml";
		String templateFileName = "microsoft/template/staging/sql/Table_Staging.sql";
		String configFileName = "microsoft/config/ExampleSQLConfig.xml";
		
		try {
			URI modelFileUri = CrossGenerateTest.class.getResource(modelFileName).toURI();
			generator.setModelFromFile(modelFileUri);
			
			URI templateFileUri = CrossGenerateTest.class.getResource(templateFileName).toURI();
			URI configFileUri = CrossGenerateTest.class.getResource(configFileName).toURI();
			URI outputFileUri = Paths.get("C:\\CrossGenerate\\Output").toUri();
			
			generator.generateFromFilesAndWriteOutput(templateFileUri, configFileUri, outputFileUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (GeneratorException e) {
			e.printStackTrace();
		} catch (UnhandledException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void doMicrosoftSSISTest() {
		Generator generator = new Generator();
		
		String modelFileName = "common/model/ExampleModel.xml";
		String templateFileName = "microsoft/template/staging/ssis/stg_load_system_name_entity_name.dtsx";
		String configFileName = "microsoft/config/ExampleSSISConfig.xml";
		
		try {
			URI modelFileUri = CrossGenerateTest.class.getResource(modelFileName).toURI();
			generator.setModelFromFile(modelFileUri);
			
			URI templateFileUri = CrossGenerateTest.class.getResource(templateFileName).toURI();
			URI configFileUri = CrossGenerateTest.class.getResource(configFileName).toURI();
			URI outputFileUri = Paths.get("C:\\CrossGenerate\\Output").toUri();
			
			generator.generateFromFilesAndWriteOutput(templateFileUri, configFileUri, outputFileUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (GeneratorException e) {
			e.printStackTrace();
		} catch (UnhandledException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void doPowerCenterTest() {
		Generator generator = new Generator();
		
		String modelFileName = "common/model/ExampleModel.xml";
		String templateFileName = "powercenter/template/staging/stg_load_system_name_entity_name.XML";
		String configFileName = "powercenter/config/ExamplePowerCenterConfig.xml";
		
		try {
			URI modelFileUri = CrossGenerateTest.class.getResource(modelFileName).toURI();
			generator.setModelFromFile(modelFileUri);
			
			URI templateFileUri = CrossGenerateTest.class.getResource(templateFileName).toURI();
			URI configFileUri = CrossGenerateTest.class.getResource(configFileName).toURI();
			URI outputFileUri = Paths.get("C:\\CrossGenerate\\Output").toUri();
			
			generator.generateFromFilesAndWriteOutput(templateFileUri, configFileUri, outputFileUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (GeneratorException e) {
			e.printStackTrace();
		} catch (UnhandledException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void doDataStageTest() {
		Generator generator = new Generator();
		
		String modelFileName = "common/model/ExampleModel.xml";
		String templateFileName = "datastage/template/load_system_name_entity_name.xml";
		String configFileName = "datastage/config/ExampleDataStageConfig.xml";
		
		try {
			URI modelFileUri = CrossGenerateTest.class.getResource(modelFileName).toURI();
			generator.setModelFromFile(modelFileUri);
			
			URI templateFileUri = CrossGenerateTest.class.getResource(templateFileName).toURI();
			URI configFileUri = CrossGenerateTest.class.getResource(configFileName).toURI();
			URI outputFileUri = Paths.get("C:\\CrossGenerate\\Output").toUri();
			
			generator.generateFromFilesAndWriteOutput(templateFileUri, configFileUri, outputFileUri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (GeneratorException e) {
			e.printStackTrace();
		} catch (UnhandledException e) {
			e.printStackTrace();
		}
	}
}
