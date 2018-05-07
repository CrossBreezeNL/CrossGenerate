@Unit
Feature: Unit_Config_Binding_SectionModelBinding
  In this feature we will describe the SectionModelBinding feature in the binding config.

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

  Scenario: Implicit root section name
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate>
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="_template_" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    And the following template named "Unit_Config_Binding_SectionModelBinding.txt":
      """
      table_name

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Binding_SectionModelBinding.txt" with content:
      """
      A
      B
      C

      """

  Scenario Outline: Root section model binding <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="<rootSectionName>">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="<rootSectionName>" modelXPath="<modelXPath>" placeholderName="<placeholderName>" />
        </Binding>
      </XGenConfig>
      """
    And the following template named "Unit_Config_Binding_SectionModelBinding.txt":
      """
      <placeholderName>_name
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Binding_SectionModelBinding.txt" with content:
      """
      <expectedResultA><expectedResultB><expectedResultC>
      """

    Examples: 
      | Scenario | rootSectionName | modelXPath                  | placeholderName | expectedResultA | expectedResultB | expectedResultC |
      | simple   | Template        | /entities/entity            | table           | A               | B               | C               |
      | empty    |                 | /entities/entity            | table           | A               | B               | C               |
      | filtered | Template        | /entities/entity[@name='A'] | table           | A               |                 |                 |

  Scenario Outline: Nested section model binding <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Template">
          <FileFormat singleLineCommentPrefix="--" />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="<rootModelXPath>" placeholderName="root">
            <SectionModelBinding section="Section" modelXPath="<childModelXPath>" placeholderName="<childPlaceholderName>" />
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    And the following template named "Unit_Config_Binding_SectionModelBinding.txt":
      """
      -- @XGenTextSection(name="Section" end="_name")
      <childPlaceholderName>_name
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Binding_SectionModelBinding.txt" with content:
      """
      <expectedResultA><expectedResultB><expectedResultC>
      """

    Examples: 
      | Scenario | rootModelXPath | childModelXPath   | childPlaceholderName | expectedResultA | expectedResultB | expectedResultC |
      | simple   | /entities      | entity            | table                | A               | B               | C               |
      | filtered | /entities      | entity[@name='A'] | table                | A               |                 |                 |
