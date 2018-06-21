@Unit
Feature: Unit_XmlTemplate_Placeholder
  In this feature we will describe placeholder resolution in XML templates

  Scenario Outline: XmlTemplate placeholder resolution <scenario>
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource" content="<content>" />
      </modeldefinition>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system"/>                      
        </Binding>
      </XGenConfig>
      """
    And the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database name="my&amp; db">
       system_content
      </Database>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database name="my&amp; db">
       <content>
      </Database>
      """

    Examples: 
      | scenario      | content          |
      | quotes        | 'test'           |
      | Ampersand     | test &amp; test  |
      | Less then     | test &lt; test   |
      | escaped quote | test &quot; test |
