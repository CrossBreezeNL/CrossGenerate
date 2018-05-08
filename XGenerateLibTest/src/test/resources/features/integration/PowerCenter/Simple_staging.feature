#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate PowerCenter

@Integration

Feature: Integration_PowerCenter_Simple_staging
  This feature file contains the features when using PowerCenter in CrossGenerate templates
  
  Scenario: Simple staging mapping
  
    Given I have the following model file: "../../../common/model.xml"
    And the following template file: "template-files/stg_load_system_name.XML"
    And the following config file: "config-files/ExamplePowerCenterConfig.xml" 
    
    When I run the generator
      
    Then I expect 1 generation result
    And an output named "stg_load_ExampleSource.XML" with contents equal to file: "expected-output-files/stg_load_ExampleSource.XML"
