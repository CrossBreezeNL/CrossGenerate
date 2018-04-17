@Unit
Feature: Unit_Config_XmlTemplate_TemplateAttributeInjection
  In this feature we will describe the TemplateAttributeInjection feature in the template config.

  Scenario Outline: Single <Scenario> template attribute injection
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A" />
      </entities>
      """
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
        <Template rootSectionName="Template">
          <FileFormat templateType="xml" />
          <Output type="output_per_element" />
          <TemplateAttributeInjections>
            <TemplateAttributeInjection templateXPath="<templateXPath>" attributeName="<attributeName>" attributeValue="<attributeValue>" />
          </TemplateAttributeInjections>
        </Template>
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
