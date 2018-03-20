#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate DataStage

@Integration

Feature: Integration_DataStage
  This feature file contains the features when using DataStage in CrossGenerate templates
  
  Scenario: Simple staging job
  
    Given I have the following model file: "general/modelOracle.xml"
    And the following template file: "DataStage/template-files/load_system_name.xml"
    And the following config file: "DataStage/config-files/ExampleDataStageConfig.xml" 
    
    When I run the generator

    Then I expect 1 generation result
    And an output named "load_ExampleSource.xml" with contents equal to file: "DataStage/expected-output-files/load_ExampleSource.xml"
    