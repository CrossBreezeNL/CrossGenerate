@Unit
Feature: Unit_Config_TextTemplate_PlaceholderFormat
  In this feature we will describe the placeholder format feature for text templates specified in config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource">
          <description>Some description</description>
        </system>
      </modeldefinition>
      """

  Scenario: Current accessor default
    Given the following template named "ExampleTemplate.sql":
      """
      database_name
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Database">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.sql" with content:
      """
      ExampleSource
      """

  Scenario Outline: Accessor <Scenario>
    Given the following template named "ExampleTemplate.sql":
      """
      <Template>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Database">
          <FileFormat <AccessorName>Accessor="<AccessorChar>" />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.sql" with content:
      """
      <ExpectedResult>
      """

    Examples: 
      | Scenario          | AccessorName | AccessorChar | Template                 | ExpectedResult   |
      | Current simple    | current      | _            | database_name            | ExampleSource    |
      | Current multichar | current      | blaat        | databaseblaatname        | ExampleSource    |
      | Child simple      | child        | $            | database$description     | Some description |
      | Child multichar   | child        | blaat        | databaseblaatdescription | Some description |
