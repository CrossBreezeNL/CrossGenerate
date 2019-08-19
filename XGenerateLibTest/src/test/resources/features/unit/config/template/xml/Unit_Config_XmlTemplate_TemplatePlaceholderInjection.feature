@Unit
Feature: Unit_Config_XmlTemplate_TemplatePlaceholderInjection
  In this feature we will describe the TemplatePlaceholderInjection feature in the template config.

  @KnownIssue See scenario examples.
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
        <table name="table_name" filter="yes" <propertyName>="Bla1"/>
        <table name="table_name" filter="no" <propertyName>="Bla2"/>
      </tables>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Template">
          <FileFormat currentAccessor="_" childAccessor="$" />
          <Output type="output_per_element" />
          <TemplatePlaceholderInjections>
            <TemplatePlaceholderInjection templateXPath="<templateXPath>" modelNode="<modelNode>" scope="<scope>" />
          </TemplatePlaceholderInjections>
        </XmlTemplate>
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
      | Scenario                    | propertyName    | templateXPath                        | modelNode     | scope   | expectedResult1                                               | expectedResult2                                              |
      | simple current              | someProperty    | //table/@someProperty                | property      | current | <table name="A" filter="yes" someProperty="something"/>       | <table name="A" filter="no" someProperty="something"/>       |
      | filter current              | someProperty    | //table[@filter='yes']/@someProperty | property      | current | <table name="A" filter="yes" someProperty="something"/>       | <table name="A" filter="no" someProperty="Bla2"/>            |
      | simple child                | someProperty    | //table/@someProperty                | childProperty | child   | <table name="A" filter="yes" someProperty="Something child"/> | <table name="A" filter="no" someProperty="Something child"/> |
      | filter child                | someProperty    | //table[@filter='yes']/@someProperty | childProperty | child   | <table name="A" filter="yes" someProperty="Something child"/> | <table name="A" filter="no" someProperty="Bla2"/>            |
      | namespace template          | xb:someProperty | //table/@someProperty                | property      | current | <table name="A" filter="yes" xb:someProperty="something"/>    | <table name="A" filter="no" xb:someProperty="something"/>    |
      | namespace template no value | xb:someProperty | //table/@someProperty                | unknown       | current | <table name="A" filter="yes"/>                                | <table name="A" filter="no"/>                                |
      # Current XG cannot handle the XPath itself to contain the namespace part, cause the namespace must then be declared. If this would work the previous 2 tests probably need to be discusses whether they also require namespace prefix in the XPath.
      | namespace prefixed template | xb:someProperty | //table/@xb:someProperty             | property      | current | <table name="A" filter="yes" xb:someProperty="something"/>    | <table name="A" filter="no" xb:someProperty="something"/>    |

  @Debug    
  Scenario Outline: Multi namespace <Scenario> template placeholder injection
  Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A" id="111"/>
      </entities>
      """
    Given the following template named "Unit_Config_Template_TemplatePlaceholderInjection.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables aa:xmlns="http://firstnamspace.org" bb:xmlns="http://secondnamespace.org" xmlns="http://defaultnamespace.org">
        <table name="table_name" aa:id="456" bb:id="789" id="123"/>        
      </tables>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Template">
          <FileFormat currentAccessor="_" childAccessor="$" />
          <Output type="output_per_element" />
          <TemplatePlaceholderInjections>
            <TemplatePlaceholderInjection templateXPath="<templateXPath>" modelNode="id" scope="current" />
          </TemplatePlaceholderInjections>
        </XmlTemplate>
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
      <tables aa:xmlns="http://firstnamspace.org" bb:xmlns="http://secondnamespace.org" xmlns="http://defaultnamespace.org">
        <table name="A" <ExpectedResult>/>        
      </tables>
      """
            
    Examples:
    | Scenario | templateXPath | ExpectedResult |
    | Unqualified | //table[@name='table_name']/@id | aa:id="456" bb:id="789" id="111" |
    | aa namespace | //table[@name='table_name']/@aa:id | aa:id="111" bb:id="789" id="123" |
    | bb namespace | //table[@name='table_name']/@bb:id | aa:id="456" bb:id="111" id="123" |
    