#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate PowerCenter

Feature: PowerCenter
  This feature file contains the features when using PowerCenter in CrossGenerate templates
  
  Scenario: Simple staging mapping
  
    Given I have the following model file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/general/model.xml
    """
  
    And the following template file: 
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/PowerCenter/template-files/stg_load_system_name.XML
    """
    
    And the following config file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/PowerCenter/config-files/ExamplePowerCenterConfig.xml
    """ 
    
    When I run the generator
      
    Then I expect 1 generation result(s)
        
    And an output named file:///C:/CrossGenerate/Output/stg_load_ExampleSource.XML with contents equal to file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/PowerCenter/expected-output-files/stg_load_ExampleSource.XML
    """
    
   
    