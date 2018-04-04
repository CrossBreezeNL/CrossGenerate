@Unit
Feature: Unit_TextTemplate_Comment
  In this feature we will describe the comment feature in text templates.

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
          <FileFormat
            templateType="text"
            singleLineCommentPrefix="--"
            multiLineCommentPrefix="/*"
            multiLineCommentSuffix="*/"
            annotationPrefix="@XGen"
            annotationArgsPrefix="("
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="model" />
        </Binding>
      </XGenConfig>
      """

  Scenario Outline: Single line text comment <Scenario>
    And the following template named "TextTemplate_Comment_SingleLine_<Scenario>.xml":
      """
      <Comment-content>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "TextTemplate_Comment_SingleLine_<Scenario>.xml" with content:
      """
      <Expected-comment-output>
      """

    Examples: 
      | Scenario                 | Comment-content                                               | Expected-comment-output               |
      | SingleAnnotationOnly     | -- @XGenComment(Something)                                    |                                       |
      | SingleCommentBefore      | -- Some comment @XGenComment(Something)                       | -- Some comment                       |
      # In this case the output will have 2 space before the comment. This because how the comment scanning works.
      | SingleCommentAfter       | -- @XGenComment(Something) Some comment                       | --  Some comment                      |
      | SingleCommentBeforeAfter | -- Some comment 1. @XGenComment(Something) Some comment 2.    | -- Some comment 1. Some comment 2.    |
      | MultiAnnotationOnly      | /* @XGenComment(Something) */                                 |                                       |
      | MultiCommentBefore       | /* Some comment @XGenComment(Something) */                    | /* Some comment */                    |
      # In this case the output will have 2 space before the comment. This because how the comment scanning works.
      | MultiCommentAfter        | /* @XGenComment(Something) Some comment */                    | /*  Some comment */                   |
      | MultiCommentBeforeAfter  | /* Some comment 1. @XGenComment(Something) Some comment 2. */ | /* Some comment 1. Some comment 2. */ |

  Scenario Outline: Multi line text comment <Scenario>
    And the following template named "TextTemplate_Comment_MultiLine_<Scenario>.xml":
      """
      /*
      <Input-comment-1>
      <Input-comment-2>
      <Input-comment-3>
       */
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "TextTemplate_Comment_MultiLine_<Scenario>.xml" with content:
      """
      /*
      <Output-comment-1>
      <Output-comment-2>
      <Output-comment-3>
       */
      """

    Examples: 
      | Scenario           | Input-comment-1         | Input-comment-2         | Input-comment-3 | Output-comment-1 | Output-comment-2 | Output-comment-3 |
      | AnnotationOnly     | @XGenComment(Something) |                         |                 |                  |                  |                  |
      | CommentBefore      | Some comment            | @XGenComment(Something) |                 | Some comment     |                  |                  |
      | CommentAfter       |                         | @XGenComment(Something) | Some comment    |                  |                  | Some comment     |
      | CommentBeforeAfter | Some comment 1.         | @XGenComment(Something) | Some comment 2. | Some comment 1.  |                  | Some comment 2.  |
