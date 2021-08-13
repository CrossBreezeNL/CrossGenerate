@Unit
Feature: Unit_Config_Model_ModelAttributeInjection_WithNamespace
  In this feature we will describe the ModelAttributeInjection feature in the model config using namespaces.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities xmlns:xa="http://www.x-breeze.com/a" xmlns:xb="http://www.x-breeze.com/b">
        <entity name="A"/>
        <xa:entity name="B"/>
        <xb:entity name="C"/>
      </entities>
      """
    And the following template named "Unit_Config_Model_ModelAttributeInjection.txt":
      """
      table_name -> table_type

      """

  Scenario Outline: Single <Scenario> attribute injection
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelNamespaces>
            <ModelNamespace prefix="aa" namespace="http://www.x-breeze.com/a" />
            <ModelNamespace prefix="bb" namespace="http://www.x-breeze.com/b" />
          </ModelNamespaces>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="<modelXPath>" targetAttribute="type" target<targetType>="<targetValue>" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/*" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      A -> <expectedResultA>
      B -> <expectedResultB>
      C -> <expectedResultC>

      """

    Examples: 
      | Scenario     | modelXPath  | targetType | targetValue | expectedResultA | expectedResultB | expectedResultC |
      | simple no ns | //entity    | Value      | simple      | simple          |                 |                 |
      | simple ns aa | //aa:entity | Value      | simple      |                 | simple          |                 |
      | simple ns bb | //bb:entity | Value      | simple      |                 |                 | simple          |
