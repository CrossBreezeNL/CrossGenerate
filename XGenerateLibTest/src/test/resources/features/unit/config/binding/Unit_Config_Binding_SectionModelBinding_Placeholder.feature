@Unit
Feature: Unit_Config_Binding_SectionModelBinding_Placeholder
  In this feature we will describe the Placeholder feature in the SectionModelBinding config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities primary="A">
        <entity name="A"/>
        <entity name="B"/>
        <entity name="C"/>
      </entities>
      """

  Scenario Outline: Placeholder with <Scenario> XPath
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" variableName="tableNode">
            <Placeholders>
              <Placeholder name="<placeholderName>" modelXPath="<modelXPath>" />
            </Placeholders>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    And the following template named "Unit_Config_Binding_SectionModelBinding_Placeholder.txt":
      """
      table_name -> <placeholderName>_name ;

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Binding_SectionModelBinding_Placeholder.txt" with content:
      """
      <expectedResultA>
      <expectedResultB>
      <expectedResultC>

      """

    Examples: 
      | Scenario | placeholderName | modelXPath                   | expectedResultA | expectedResultB | expectedResultC |
      | relative | nextTable       | following-sibling::entity[1] | A -> B ;        | B -> C ;        | C ->  ;         |
      | absolute | firstTable      | //entity[1]                  | A -> A ;        | B -> A ;        | C -> A ;        |
      | variable | primary         | $tableNode                   | A -> A ;        | B -> B ;        | C -> C ;        |
      
  Scenario: Placeholders using variable
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table">
            <Placeholders>
              <Placeholder name="firstPlaceholder" modelXPath="//entity[1]" variableName="placeholderNode" />
              <Placeholder name="secondPlaceholder" modelXPath="$placeholderNode" />
            </Placeholders>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    And the following template named "Unit_Config_Binding_SectionModelBinding_Placeholder.txt":
      """
      table_name -> secondPlaceholder_name ;

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Binding_SectionModelBinding_Placeholder.txt" with content:
      """
      A -> A ;
      B -> A ;
      C -> A ;

      """
