@Unit
Feature: Unit_Config_XmlTemplate_TemplateNodeRemoval
  In this feature we will describe the TemplateNodeRemoval feature in the template config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A" />
      </entities>
      """

  Scenario Outline: Simple <Scenario> template node removal
    Given the following template named "Unit_Config_XmlTemplate_TemplateNodeRemoval.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <table name="table_name" someAttribute="1" />
        <table name="table_name" someAttribute="2" />
      </tables>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Template">
          <Output type="output_per_element" />
          <TemplateNodeRemovals>
            <TemplateNodeRemoval templateXPath="<templateXPath>" />
          </TemplateNodeRemovals>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table"/>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_XmlTemplate_TemplateNodeRemoval.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <tables>
        <expectedResult1>
        <expectedResult2>
      </tables>
      """

    Examples: 
      | Scenario           | templateXPath                              | expectedResult1                      | expectedResult2     |
      | element            | //table                                    |                                      |                     |
      | element filtered   | //table[@someAttribute='2']                | <table name="A" someAttribute="1" /> |                     |
      | attribute          | //table/@someAttribute                     | <table name="A"  />                  | <table name="A"  /> |
      | attribute filtered | //table[@someAttribute='2']/@someAttribute | <table name="A" someAttribute="1" /> | <table name="A"  /> |
