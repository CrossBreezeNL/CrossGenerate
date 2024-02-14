@Unit
Feature: Unit_Config_XmlTemplate_Section
  In this feature we will describe the section feature for XML templates specified in config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource">
          <entity name="Order"/>
        </system>
      </modeldefinition>
      """
    And the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database name="system_name">
        <Tables>
          <Table name="entity_name"/>
        </Tables>
      </Database>
      """

  Scenario Outline: Implicit root and explicit section <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" />
          <Output type="output_per_element" />
          <XmlSections>
            <XmlSection name="Tables" templateXPath="<templateXPath>" />
          </XmlSections>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="./entity" placeholderName="entity"/>
          </SectionModelBinding>           
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database name="ExampleSource">
        <Tables>
          <expectedResult>
        </Tables>
      </Database>
      """

    Examples: 
      | Scenario | templateXPath                               | expectedResult              |
      | Simple   | /Database/Tables/Table                      | <Table name="Order"/>       |
      | Filtered | /Database/Tables/Table[@name='entity_name'] | <Table name="Order"/>       |
      | Invalid  | /Database/Tables/Table[@name='incorrect']   | <Table name="entity_name"/> |
