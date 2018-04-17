@Unit
Feature: Unit_Config_XmlTemplate_CommentNode
  In this feature we will describe the comment node feature for XML templates specified in config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition />
      """

  Scenario Outline: Comment node is <Scenario>
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Template>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat
            templateType="xml"
            commentNodeXPath="<commentNodeXPath>"
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
      <ExpectedResult>
      """

    Examples: 
      | Scenario               | commentNodeXPath   | Template                                                                                  | ExpectedResult                                                  |
      | child attribute        | @description       | <Database description="@XGenComment(Some comment)"/>                                      | <Database description=""/>                                      |
      | nested child attribute | child/@description | <Database><child description="@XGenComment(Some comment)"/></Database>                    | <Database><child description=""/></Database>                    |
      | child element          | description        | <Database><description>@XGenComment(Some comment)</description></Database>                | <Database><description></description></Database>                |
      | nested child element   | child/description  | <Database><child><description>@XGenComment(Some comment)</description></child></Database> | <Database><child><description></description></child></Database> |
