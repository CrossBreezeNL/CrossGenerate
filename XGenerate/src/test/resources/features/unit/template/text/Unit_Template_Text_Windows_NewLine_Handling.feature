#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate Windows NewLine Handling

@Unit
Feature: Unit_Template_Text_Windows_NewLine_Handling
  This feature file contains the features when using Windows NewLines (CRLF) in your template.
  In the results pane click on 'Show Whitespace Characters' to view the CRLF vs LF.

  Scenario: Windows new line in template
    Given I have the following model file: "model-files/WithoutNewlines.xml"
    And the following config file: "config-files/Windows_NewLine_Handling.xml"
    And the following template file: "template-files/CRLF_Column_Attribute.sql"
      
    When I run the generator
    
    Then I expect 1 generation results
    And an output named "CRLF_Column_Attribute.sql" with contents equal to file: "expected-output-files/Windows_NewLine_Template.sql"

	Scenario: New line in placeholder encoded
    Given I have the following model file: "model-files/LF_InAttribute_Encoded.xml"
    And the following config file: "config-files/Windows_NewLine_Handling.xml" 
    And the following template file: "template-files/LF_Column_Attribute.sql"
      
    When I run the generator
    
    Then I expect 1 generation results
    And an output named "LF_Column_Attribute.sql" with contents equal to file: "expected-output-files/LF_NewLines.sql"

	@KnownIssue
	# In Windows this scenario succeeds, but in linux it fails. This is probably due to Git clone changing the new-lines in the files on checkout.
  Scenario: Windows new line in placeholder encoded
    Given I have the following model file: "model-files/CRLF_InAttribute_Encoded.xml"
    And the following config file: "config-files/Windows_NewLine_Handling.xml" 
    And the following template file: "template-files/CRLF_Column_Attribute.sql"
    
    When I run the generator
    
    Then I expect 1 generation results
    And an output named "CRLF_Column_Attribute.sql" with contents equal to file: "expected-output-files/CRLF_NewLines.sql"
    
  @KnownIssue
  Scenario: Windows new line in placeholder not-encoded @KnownIssue
    Given I have the following model file: "model-files/CRLF_InElement_NotEncoded.xml"
    And the following config file: "config-files/Windows_NewLine_Handling.xml" 
    And the following template file: "template-files/CRLF_Column_Element.sql"
    
    When I run the generator
    
    Then I expect 1 generation results
    And an output named "CRLF_Column_Element.sql" with contents equal to file: "expected-output-files/CRLF_NewLines.sql"
