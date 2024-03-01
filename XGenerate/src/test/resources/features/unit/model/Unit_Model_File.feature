@Unit
Feature: Unit_Model_File
  In this feature we will describe the feature to use Xml Include instructions in the model file.

  Scenario: Xml include in model file
    Given I have the following model file: "example-model.xml"
    And the following config file: "example-config.xml"
    And the following template file: "example-template.txt"
    When I run the generator
    Then I expect 1 generation result
    And an output named "example-template.txt" with contents equal to file: "expected-output.txt"