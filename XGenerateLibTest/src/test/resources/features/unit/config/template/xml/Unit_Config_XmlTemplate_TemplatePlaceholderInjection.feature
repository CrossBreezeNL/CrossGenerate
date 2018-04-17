@Unit
Feature: Unit_Config_XmlTemplate_TemplatePlaceholderInjection
  In this feature we will describe the TemplatePlaceholderInjection feature in the template config.

  Scenario Outline: Single <Scenario> template placeholder injection
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A" property="something">
          <childProperty>Something child</childProperty>
        </entity>
      </entities>
      """
    Given the following template named "Unit_Config_Template_TemplatePlaceholderInjection.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <table name="table_name" filter="yes" someProperty="Bla1"/>
        <table name="table_name" filter="no" someProperty="Bla2"/>
      </tables>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Template">
          <FileFormat templateType="xml" currentAccessor="_" childAccessor="$" />
          <Output type="output_per_element" />
          <TemplatePlaceholderInjections>
            <TemplatePlaceholderInjection templateXPath="<templateXPath>" modelNode="<modelNode>" scope="<scope>" />
          </TemplatePlaceholderInjections>
        </Template>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table"/>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Template_TemplatePlaceholderInjection.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <expectedResult1>
        <expectedResult2>
      </tables>
      """

    Examples: 
      | Scenario       | templateXPath                        | modelNode     | scope   | expectedResult1                                               | expectedResult2                                              |
      | simple current | //table/@someProperty                | property      | current | <table name="A" filter="yes" someProperty="something"/>       | <table name="A" filter="no" someProperty="something"/>       |
      | filter current | //table[@filter='yes']/@someProperty | property      | current | <table name="A" filter="yes" someProperty="something"/>       | <table name="A" filter="no" someProperty="Bla2"/>            |
      | simple child   | //table/@someProperty                | childProperty | child   | <table name="A" filter="yes" someProperty="Something child"/> | <table name="A" filter="no" someProperty="Something child"/> |
      | filter child   | //table[@filter='yes']/@someProperty | childProperty | child   | <table name="A" filter="yes" someProperty="Something child"/> | <table name="A" filter="no" someProperty="Bla2"/>            |
