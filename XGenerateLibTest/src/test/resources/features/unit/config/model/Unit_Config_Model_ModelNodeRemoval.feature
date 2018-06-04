@Unit
Feature: Unit_Config_Model_ModelNodeRemoval
  In this feature we will describe the ModelNodeRemoval feature in the model config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A" description="This is table A" />
        <entity name="B" description="This is table B" />
        <entity name="C" description="This is table C" />
      </entities>
      """
    And the following template named "Unit_Config_Model_ModelNodeRemoval.txt":
      """
      table_name -> table_description

      """

  Scenario: Single element removal
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelNodeRemovals>
            <ModelNodeRemoval modelXPath="//entity[@name='B']" />
          </ModelNodeRemovals>
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
    And an output named "Unit_Config_Model_ModelNodeRemoval.txt" with content:
      """
      A -> This is table A
      C -> This is table C

      """
     
  Scenario: Single attribute removal
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelNodeRemovals>
            <ModelNodeRemoval modelXPath="//entity[@name='B']/@description" />
          </ModelNodeRemovals>
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
    And an output named "Unit_Config_Model_ModelNodeRemoval.txt" with content:
      """
      A -> This is table A
      B -> 
      C -> This is table C

      """
      
  Scenario: Multi node removal
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelNodeRemovals>
            <ModelNodeRemoval modelXPath="//entity" />
          </ModelNodeRemovals>
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
    And an output named "Unit_Config_Model_ModelNodeRemoval.txt" with content:
      """

      """