@Unit
Feature: Unit_Config_Model_ModelNamespace
  In this feature we will describe the ModelNamespace feature in the model config

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

  Scenario Outline: Namespace single result <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model namespaceAware="<namespaceAware>">
          <ModelNamespaces>
            <ModelNamespace prefix="xa" namespace="http://www.x-breeze.com/a" />
            <ModelNamespace prefix="xb" namespace="http://www.x-breeze.com/b" />
          </ModelNamespaces>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="<modelXPath>" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelNodeRemoval.txt" with content:
      """
      <expectedResult>
      
      """

    Examples: 
      | Scenario                                    | namespaceAware | modelXPath       | expectedResult       |
      | namespace aware, select without namespace | true           | /entities/entity    | A -> This is table A |
      | namespace aware, select with namespace    | true           | /entities/xa:entity | B -> This is table B  |
      | namespace unaware, select with namespace  | false          | /entities/xa:entity | B -> This is table B                     |
      | namespace unaware, select without namespace | false          | /entities/entity | A -> This is table A |
