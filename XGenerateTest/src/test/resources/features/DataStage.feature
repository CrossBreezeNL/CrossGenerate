#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate DataStage

Feature: PowerCenter
  This feature file contains the features when using DataStage in CrossGenerate templates
  
  Scenario: Simple staging job
  
    Given I have the following model file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/general/modelOracle.xml
    """
  
    And the following template file: 
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/DataStage/template-files/load_system_name.xml
    """
    
    And the following config file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/DataStage/config-files/ExampleDataStageConfig.xml
    """ 
    
    When I run the generator
      
    Then I expect 1 generation result(s)
        
    And an output named file:///C:/CrossGenerate/Output/load_ExampleSource.xml with contents equal to file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/DataStage/expected-output-files/load_ExampleSource.xml
    """
    
   
    