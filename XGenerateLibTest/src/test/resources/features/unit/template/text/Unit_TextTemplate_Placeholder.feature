@Unit
Feature: Unit_TextTemplate_Placeholder
  In this feature we will describe the placeholder feature in text templates.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <attribute name="FirstColumn" property="SomeProperty" />
      </modeldefinition>
      """

  Scenario Outline: Placeholder handling
    And the following template named "Unit_TextTemplate_Placeholder.sql":
      """
      <Template>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Columns">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Columns" modelXPath="/modeldefinition/attribute" placeholderName="column" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_TextTemplate_Placeholder.sql" with content:
      """
      <ExpectedOutput>
      """

    Examples: 
      | Scenario | Template                    | ExpectedOutput           |
      | Single   | column_name                 | FirstColumn              |
      | Double   | column_name column_property | FirstColumn SomeProperty |

  Scenario Outline: Overridden placeholder <Scenario>
    And the following template named "Unit_TextTemplate_PlaceholderOverride.sql":
      """
      -- @XGenTextSection(name="Columns" <Placeholder>)
      <TemplateLine>

      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="TemplateFile">
         <FileFormat
            singleLineCommentPrefix="--" 
            annotationPrefix="@XGen" 
            annotationArgsPrefix="(" 
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="TemplateFile" modelXPath="/modeldefinition">
          	<SectionModelBinding section="Columns" modelXPath="attribute" placeholderName="column"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_TextTemplate_PlaceholderOverride.sql" with content:
      """
      <ExpectedOutput>

      """

    Examples: 
      | Scenario                                               | Placeholder                | TemplateLine  | ExpectedOutput |
      | No placeholder override                                |                            | column_name   | FirstColumn    |
      | overridden placeholder                                 | placeholderName="property" | property_name | FirstColumn    |
      | overriden placeholder, but use placeholder from config | placeholderName="property" | column_name   | column_name    |
