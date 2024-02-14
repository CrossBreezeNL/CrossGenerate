@Unit
Feature: Unit_Config_TextTemplate_CommentFormat
  In this feature we will describe the comment format feature for text templates specified in config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition />
      """

  Scenario Outline: Single line comment <Scenario>
    Given the following template named "ExampleTemplate.sql":
      """
      <Template>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Database">
          <FileFormat singleLineCommentPrefix="<singleLineCommentPrefix>" />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.sql" with content:
      """
      <ExpectedResult>
      """

    Examples: 
      | Scenario     | singleLineCommentPrefix | Template                      | ExpectedResult |
      | double dash  | --                      | -- @XGenComment(Some comment) |                |
      | double slash | //                      | // @XGenComment(Some comment) |                |
      | single hash  | #                       | # @XGenComment(Some comment)  |                |

  Scenario Outline: Multi line comment <Scenario>
    Given the following template named "ExampleTemplate.sql":
      """
      <Template>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <TextTemplate rootSectionName="Database">
          <FileFormat
            multiLineCommentPrefix="<multiLineCommentPrefix>"
            multiLineCommentSuffix="<multiLineCommentSuffix>"
          />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.sql" with content:
      """
      <ExpectedResult>
      """

    Examples: 
      | Scenario             | multiLineCommentPrefix | multiLineCommentSuffix | Template                              | ExpectedResult |
      | one line SQL         | /*                     | */                     | /* @XGenComment(Some comment) */      |                |
      | multiples lines SQL  | /*                     | */                     | /*\n@XGenComment(Some comment)\n*/    | /*\n*/       |
      | one line Java        | /**                    | */                     | /** @XGenComment(Some comment) */     |                |
      | multiples lines Java | /**                    | */                     | /**\n@XGenComment(Some comment)\n*/   | /**\n*/      |
      # The < and > are entity encoded, but are evaluated by cucumber before passing into CrossGenerate.
      | one line HTML        | &lt;!--                | --&gt;                 | <!-- @XGenComment(Some comment) -->   |                |
      | multiple lines HTML  | &lt;!--                | --&gt;                 | <!--\n@XGenComment(Some comment)\n--> | <!--\n-->    |
