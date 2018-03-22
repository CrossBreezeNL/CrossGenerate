@Unit
Feature: Unit_XmlTemplate_Comment
  In this feature we will describe the comment feature in XML templates.

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
        <Template rootSectionName="Template">
          <FileFormat templateType="xml" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="model" />
        </Binding>
      </XGenConfig>
      """

  Scenario Outline: Comment in XML
    And the following template named "XmlFileWithComment.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <SomeElement description="<Comment-node-content>"/>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "XmlFileWithComment.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <SomeElement description="<Expected-comment-node-output>"/>
      """

    Examples: 
      | Scenario                 | Comment-node-content                                             | Expected-comment-node-output             |
      | Annotation only          | @XGenComment(Something)                                          |                                          |
      | Comment before           | Some comment. @XGenComment(Something)                            | Some comment.                            |
      | Comment after            | @XGenComment(Something) Some comment.                            | Some comment.                            |
      | Comment before and after | Some comment before. @XGenComment(Something) Some comment after. | Some comment before. Some comment after. |
