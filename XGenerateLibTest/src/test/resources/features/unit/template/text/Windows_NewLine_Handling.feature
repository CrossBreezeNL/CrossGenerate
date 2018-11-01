#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate Windows NewLine Handling

@Unit
Feature: Integration_SQL_Windows_NewLine_Handling
  This feature file contains the features when using Windows NewLines (CRLF) in your template.
  In the results pane click on 'Show Whitespace Characters' to view the CRLF vs LF.

  Scenario: Windows new line in template
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <attribute name="FirstColumn" property="SomeProperty" />
      </modeldefinition>
      """
    And the following config file: "config-files/Windows_NewLine_Handling.xml" 
    And the following template file: "template-files/Windows_NewLine_Handling.sql"
      
    When I run the generator
    
    Then I expect 1 generation results
    And an output named "Windows_NewLine_Handling.sql" with contents equal to file: "expected-output-files/Windows_NewLine_Template.sql"

  Scenario: Windows new line in placeholder encoded
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <attribute name="First&#13;&#10;Column" property="SomeProperty" />
      </modeldefinition>
      """
    And the following config file: "config-files/Windows_NewLine_Handling.xml" 
    And the following template file: "template-files/Windows_NewLine_Handling.sql"
      
    When I run the generator
    
    Then I expect 1 generation results
    And an output named "Windows_NewLine_Handling.sql" with contents equal to file: "expected-output-files/Windows_NewLine_Placeholder.sql"
    
  @KnownIssue
  Scenario: Windows new line in placeholder not-encoded
    Given I have the following model file: "model-files/Windows_NewLine_Placeholder.xml"
    And the following config file: "config-files/Windows_NewLine_Handling.xml" 
    And the following template file: "template-files/Windows_NewLine_Placeholder.sql"
      
    When I run the generator
    
    Then I expect 1 generation results
    And an output named "Windows_NewLine_Placeholder.sql" with contents equal to file: "expected-output-files/Windows_NewLine_Placeholder.sql"
