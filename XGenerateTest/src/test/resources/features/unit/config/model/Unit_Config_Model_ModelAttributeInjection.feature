@Unit
Feature: Unit_Config_Model_ModelAttributeInjection
  In this feature we will describe the ModelAttributeInjection feature in the model config.

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
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="<modelXPath>" targetAttribute="type" target<targetType>="<targetValue>" />
          </ModelAttributeInjections>
        </Model>
        <Template rootSectionName="Template">
          <FileFormat templateType="text" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
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
      | Scenario     | modelXPath          | targetType | targetValue | expectedResultA | expectedResultB | expectedResultC |
      | simple value | //entity            | Value      | simple      | simple          | simple          | simple          |
      | filter value | //entity[@name='B'] | Value      | simple      |                 | simple          |                 |
      | simple XPath | //entity            | XPath      | ./@name     | A               | B               | C               |
      | filter XPath | //entity[@name='B'] | XPath      | ./@name     |                 | B               |                 |

  Scenario: Multiple attribute injection
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="//entity[@name='A']" targetAttribute="type" targetValue="The entity is A" />
            <ModelAttributeInjection modelXPath="//entity[@name='B']" targetAttribute="type" targetValue="The entity is B" />
            <ModelAttributeInjection modelXPath="//entity[@name='C']" targetAttribute="type" targetValue="The entity is C" />
          </ModelAttributeInjections>
        </Model>
        <Template rootSectionName="Template">
          <FileFormat templateType="text" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      A -> The entity is A
      B -> The entity is B
      C -> The entity is C

      """
