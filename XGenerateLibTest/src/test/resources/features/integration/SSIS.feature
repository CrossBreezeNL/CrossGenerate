#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SSIS

@Integration

Feature: Integration_SSIS
  This feature file contains the features when using SSIS in CrossGenerate templates
  
  Scenario: Simple staging package
  
	  Given I have the following model file: "general/model.xml"
	  And the following template file: "SSIS/template-files/stg_load_system_name_entity_name.dtsx"
	  And the following config file: "SSIS/config-files/ExampleSSISConfig.xml" 
	  
	  When I run the generator
	    
	  Then I expect 2 generation results
    And an output named "stg_load_ExampleSource_Order.dtsx" with contents equal to file: "SSIS/expected-output-files/stg_load_ExampleSource_Order.dtsx"
    And an output named "stg_load_ExampleSource_Customer.dtsx" with contents equal to file: "SSIS/expected-output-files/stg_load_ExampleSource_Customer.dtsx"
