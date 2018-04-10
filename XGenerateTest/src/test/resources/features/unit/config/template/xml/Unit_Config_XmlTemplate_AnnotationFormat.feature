@Unit
Feature: Unit_Config_XmlTemplate_AnnotationFormat
  In this feature we will describe the annotation format feature for XML templates specified in config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition />
      """

  Scenario Outline: Annotation format <Scenario>
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database description="<templateAnnotationPrefix>Comment<annotationArgsPrefix>Some comment<annotationArgsSuffix>"/>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat
            templateType="xml"
            commentNodeXPath="<commentNodeXPath>"
            annotationPrefix="<annotationPrefix>"
            annotationArgsPrefix="<annotationArgsPrefix>"
            annotationArgsSuffix="<annotationArgsSuffix>"
          />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <expectedResult>
      """

    Examples: 
      | Scenario         | commentNodeXPath | annotationPrefix | annotationArgsPrefix | annotationArgsSuffix | expectedResult             |
      | Standard         | @description     | @XGen            | (                    | )                    | <Database description=""/> |
      | Diff prefix      | @description     | Bla              | (                    | )                    | <Database description=""/> |
      | Diff args prefix | @description     | @XGen            | Bla                  | )                    | <Database description=""/> |
      | Diff args suffix | @description     | @XGen            | (                    | Bla                  | <Database description=""/> |
      | All same         | @description     | @                | @                    | @                    | <Database description=""/> |

  Scenario: Annotation format default values
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database description="@XGenComment(Some comment)"/>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat
            templateType="xml"
            commentNodeXPath="@description"
          />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database description=""/>
      """
