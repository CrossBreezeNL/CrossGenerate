@Unit
Feature: Unit_Config_XmlTemplate_Section_Optional
  In this feature we will describe the optional section feature for XML templates specified in config.
  
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
      
  Scenario Outline: section defined in config but not in template, optional is <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" />
          <Output type="output_per_element" />
          <XmlSections>
            <XmlSection name="Tables" templateXPath="//Table[@name='entity_name']" />
            <XmlSection name="OtherTables" templateXPath="//Table[@name='dummy']" <Optional> />
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
          <Table name="Order"/>
        </Tables>
      </Database>
      """
    And I expect the following console message:
      """
      <ErrorLevel> No template nodes found for section 'OtherTables' using XPath '//Table[@name='dummy']'
      """

    Examples: 
      | Scenario  | Optional         | ErrorLevel |
      | undefined |                  | [WARNING]  |
      | false     | optional="false" | [WARNING]  |
      | true      | optional="true"  | [INFO   ]  |
