@Unit
Feature: Unit_Config_Model_ModelAttributeInjection_ValueMappings
  In this feature we will describe the ModelAttributeInjection ValueMappings feature in the model config.

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
    And the following template named "Unit_Config_Model_ModelAttributeInjection_ValueMappings.txt":
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
            <ModelAttributeInjection modelXPath="//entity" targetAttribute="type">
              <ValueMappings inputNode="@name">
                <ValueMapping inputValue="<inputValue1>" outputValue="1" />
                <ValueMapping inputValue="<inputValue2>" outputValue="2" />
                <ValueMapping inputValue="<inputValue3>" outputValue="3" />
              </ValueMappings>
            </ModelAttributeInjection>
          </ModelAttributeInjections>
        </Model>
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
    And an output named "Unit_Config_Model_ModelAttributeInjection_ValueMappings.txt" with content:
      """
      A -> <expectedResultA>
      B -> <expectedResultB>
      C -> <expectedResultC>

      """

    Examples: 
      | Scenario     | inputValue1 | inputValue2 | inputValue3 | expectedResultA | expectedResultB | expectedResultC |
      | 1 on 1 match | A           | B           | C           |               1 |               2 |               3 |
      | no match     | A           | B           | D           |               1 |               2 |                 |
