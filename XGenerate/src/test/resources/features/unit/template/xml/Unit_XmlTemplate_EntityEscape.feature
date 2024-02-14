@Unit
Feature: Unit_XmlTemplate_EntityEscape
  In this feature we will describe entity escaping for XML templates.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition />
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <XmlTemplate rootSectionName="Template">
          <Output type="single_output" />
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" />
        </Binding>
      </XGenConfig>
      """

  Scenario Outline: <Scenario> escaping
    Given the following template named "EntityEscaping.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <root>
        <TemplatePart>
      </root>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "EntityEscaping.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <root>
        <TemplatePart>
      </root>
      """

    Examples: 
      | Scenario              | TemplatePart |
      | XML tag chars         | <Something/> |
      | entity name lt        | &lt;         |
      | entity name quot      | &quot;       |
      | entity number 9       | &#9;         |
      | entity number 255     | &#255;       |
      | entity hexadecimal 9  | &#x9;        |
      | entity hexadecimal ff | &#xff;       |
