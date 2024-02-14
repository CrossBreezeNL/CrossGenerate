@Unit
Feature: Unit_Config_TextTemplate_AnnotationFormat
  In this feature we will describe the annotation format feature for text templates specified in config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition />
      """

  Scenario Outline: Section with begin and end character sequence, <Scenario>
    Given the following template named "Unit_Config_TextTemplate_AnnotationFormat.sql":
      """
      -- <annotationPrefix>Comment<annotationArgsPrefix>Some comment<annotationArgsSuffix>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Template">
          <FileFormat
            singleLineCommentPrefix="--"
            annotationPrefix="<annotationPrefix>"
            annotationArgsPrefix="<annotationArgsPrefix>"
            annotationArgsSuffix="<annotationArgsSuffix>"
          />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_TextTemplate_AnnotationFormat.sql" with content:
      """
      <expectedResult>
      """

    Examples: 
      | Scenario         | annotationPrefix | annotationArgsPrefix | annotationArgsSuffix | expectedResult |
      | Standard         | @XGen            | (                    | )                    |                |
      | Diff prefix      | Bla              | (                    | )                    |                |
      | Diff args prefix | @XGen            | Bla                  | )                    |                |
      | Diff args suffix | @XGen            | (                    | Bla                  |                |
      | All same         | @                | @                    | @                    |                |

  Scenario: Annotation format default values
    Given the following template named "Unit_Config_TextTemplate_AnnotationFormat.sql":
      """
      -- @XGenComment(Some comment)
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Template">
          <FileFormat singleLineCommentPrefix="--" />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_TextTemplate_AnnotationFormat.sql" with content:
      """
      """