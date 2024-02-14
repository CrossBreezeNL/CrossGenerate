@Unit
Feature: Unit_Config_Template_OutputType
  In this feature we will describe the OutputType feature in the template config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A"/>
        <entity name="B"/>
        <entity name="C"/>
      </entities>
      """
    And the following template named "Unit_Config_Template_OutputType_table_name.txt":
      """
      table_name
      
      """

  Scenario: Single output
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Template_OutputType_A B C.txt" with content:
      """
      A
      B
      C
      
      """
      
  Scenario: Outper per element
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Template">
          <Output type="output_per_element" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 3 generation result
    And an output named "Unit_Config_Template_OutputType_A.txt" with content:
      """
      A
      
      """
    And an output named "Unit_Config_Template_OutputType_B.txt" with content:
      """
      B
      
      """
    And an output named "Unit_Config_Template_OutputType_C.txt" with content:
      """
      C
      
      """
