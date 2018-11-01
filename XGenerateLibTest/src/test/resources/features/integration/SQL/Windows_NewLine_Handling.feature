#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate Windows NewLine Handling

@Integration

Feature: Integration_SQL_Windows_NewLine_Handling
  This feature file contains the features when using Windows NewLines (CRLF) in your template.
  In the results pane click on 'Show Whitespace Characters' to view the CRLF vs LF.

  Scenario: Windows new line handling
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
    And an output named "Windows_NewLine_Handling.sql" with contents equal to file: "expected-output-files/Windows_NewLine_Handling.sql"
