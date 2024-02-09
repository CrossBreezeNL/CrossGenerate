@Unit
Feature: Unit_Config_XmlTemplate_TemplateAttributeInjection
  In this feature we will describe the TemplateAttributeInjection feature in the template config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A" />
      </entities>
      """

  Scenario Outline: Single <Scenario> template attribute injection
    Given the following template named "Unit_Config_Template_TemplateAttributeInjection.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <table name="table_name" filter="yes"/>
        <table name="table_name" filter="no"/>
      </tables>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Template">
          <Output type="output_per_element" />
          <TemplateAttributeInjections>
            <TemplateAttributeInjection templateXPath="<templateXPath>" attributeName="<attributeName>" attributeValue="<attributeValue>" />
          </TemplateAttributeInjections>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table"/>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Template_TemplateAttributeInjection.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <expectedResult1>
        <expectedResult2>
      </tables>
      """

    Examples: 
      | Scenario | templateXPath          | attributeName | attributeValue | expectedResult1                                 | expectedResult2                               |
      | simple   | //table                | type          | SomeType       | <table name="A" filter="yes" type="SomeType"/>  | <table name="A" filter="no" type="SomeType"/> |
      | filter   | //table[@filter='yes'] | type          | OtherType      | <table name="A" filter="yes" type="OtherType"/> | <table name="A" filter="no"/>                 |

  Scenario Outline: Multi match <Scenario> template attribute injection
    Given the following template named "Unit_Config_Template_TemplateAttributeInjection.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <table name="table_name" filter="yes"/>
        <table name="table_name" filter="no"/>
        <table name="table_name" filter="yesorno"/>
      </tables>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Template">
          <Output type="output_per_element" />
          <TemplateAttributeInjections>
            <TemplateAttributeInjection templateXPath="<templateXPath>" attributeName="<attributeName>" attributeValue="<attributeValue>" />
          </TemplateAttributeInjections>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table"/>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Template_TemplateAttributeInjection.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <expectedResult1>
        <expectedResult2>
        <expectedResult3>
      </tables>
      """

    Examples: 
      | Scenario   | templateXPath                       | attributeName | attributeValue | expectedResult1                                   | expectedResult2                                | expectedResult3                                       |
      | startswith | //table[starts-with(@filter,'yes')] | type          | SomeType       | <table name="A" filter="yes" type="SomeType"/>    | <table name="A" filter="no"/>                  | <table name="A" filter="yesorno" type="SomeType"/>    |
      | endswith   | //table[ends-with(@filter,'no')]    | type          | OtherType      | <table name="A" filter="yes"/>                    | <table name="A" filter="no" type="OtherType"/> | <table name="A" filter="yesorno" type="OtherType"/>   |
      | contains   | //table[contains(@filter,'yes')]    | type          | AnotherType    | <table name="A" filter="yes" type="AnotherType"/> | <table name="A" filter="no"/>                  | <table name="A" filter="yesorno" type="AnotherType"/> |