#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SSIS

Feature: SSIS
  This feature file contains the features when using SSIS in CrossGenerate templates
  
  Scenario: Simple staging package
  
	  Given I have the following model file:
	  """
	  file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/general/model.xml
	  """
	
	  And the following template file: 
	  """
	  file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/SSIS/template-files/stg_load_system_name_entity_name.dtsx
	  """
	  
	  And the following config file:
	  """
	  file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/SSIS/config-files/ExampleSSISConfig.xml
	  """ 
	  
	  When I run the generator
	    
	  Then I expect 2 generation result(s)
        
    And an output named file:///C:/CrossGenerate/Output/stg_load_ExampleSource_Order.dtsx with contents equal to file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/SSIS/expected-output-files/stg_load_ExampleSource_Order.dtsx
    """

    And an output named file:///C:/CrossGenerate/Output/stg_load_ExampleSource_Customer.dtsx with contents equal to file:
    """
    file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateTest/src/test/resources/feature-support-files/SSIS/expected-output-files/stg_load_ExampleSource_Customer.dtsx
    """
   
    