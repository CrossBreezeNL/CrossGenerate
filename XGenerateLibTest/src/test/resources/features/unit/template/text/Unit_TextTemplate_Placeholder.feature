@Unit
Feature: Unit_TextTemplate_Placeholder
  In this feature we will describe the placeholder feature in text templates.

  Scenario Outline: Placeholder handling
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <attribute name="FirstColumn" property="SomeProperty" />
      </modeldefinition>
      """
    And the following template named "Unit_TextTemplate_Placeholder.sql":
      """
      <Template>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Columns">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Columns" modelXPath="/modeldefinition/attribute" placeholderName="column" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_TextTemplate_Placeholder.sql" with content:
      """
      <ExpectedOutput>
      """

    Examples: 
      | Scenario | Template                    | ExpectedOutput           |
      | Single   | column_name                 | FirstColumn              |
      | Double   | column_name column_property | FirstColumn SomeProperty |
