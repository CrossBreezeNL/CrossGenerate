@Unit
Feature: Unit_Config_Model_ModelNodeRemoval_WithNamespaces
  In this feature we will describe the ModelNodeRemoval feature in the model config with namespaces.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities xmlns:xa="http://www.x-breeze.com/a" xmlns:xb="http://www.x-breeze.com/b">
        <entity name="A" description="This is table A" />
        <xa:entity name="B" description="This is table B" />
        <xb:entity name="C" description="This is table C" />
      </entities>
      """
    And the following template named "Unit_Config_Model_ModelNodeRemoval.txt":
      """
      table_name -> table_description

      """

  Scenario Outline: Single element removal
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelNamespaces>
            <ModelNamespace prefix="aa" namespace="http://www.x-breeze.com/a" />
            <ModelNamespace prefix="bb" namespace="http://www.x-breeze.com/b" />
          </ModelNamespaces>
          <ModelNodeRemovals>
            <ModelNodeRemoval modelXPath="<modelXPath>" />
          </ModelNodeRemovals>
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
    And an output named "Unit_Config_Model_ModelNodeRemoval.txt" with content:
      """
      A -> <expectedResultA>
      B -> <expectedResultB>
      C -> <expectedResultC>

      """

    Examples: 
      | Scenario       | modelXPath               | expectedResultA | expectedResultB | expectedResultC |
      | no ns          | //entity/@description    |                 | This is table B | This is table C |
      | ns aa          | //aa:entity/@description | This is table A |                 | This is table C |
      | ns bb          | //bb:entity/@description | This is table A | This is table B |                 |
      | no ns wildcard | /entities/*/@description |                 |                 |                 |
