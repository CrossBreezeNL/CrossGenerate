@Unit
Feature: Unit_Config_Binding_SectionModelBinding_Literal
  In this feature we will describe the Literal feature in the SectionModelBinding config.

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

  Scenario Outline: Literal with <Scenario> XPath
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" variableName="tableNode">
            <Literals>
              <Literal literal="<literal>" modelXPath="<modelXPath>" />
            </Literals>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    And the following template named "Unit_Config_Binding_SectionModelBinding_Placeholder.txt":
      """
      table_name -> <literal> ;

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
      | Scenario    | literal      | modelXPath                         | expectedResultA | expectedResultB | expectedResultC |
      | relative    | nextTable    | following-sibling::entity[1]/@name | A -> B ;        | B -> C ;        | C ->  ;         |
      | absolute    | firstTable   | //entity[1]/@name                  | A -> A ;        | B -> A ;        | C -> A ;        |
      | variable    | primary      | $tableNode/@name                   | A -> A ;        | B -> B ;        | C -> C ;        |
      | regex chars | $something\\ | @name                              | A -> A ;        | B -> B ;        | C -> C ;        |
