@Unit
Feature: Unit_Config_TextTemplate_Section_Prefix
  In this feature we will describe the section prefix feature for text templates used in config.

  Background: 
    Given I have the following model:
      """
        <modeldefinition>
         <attributes>
           <attribute name="FirstColumn" />
           <attribute name="SecondColumn" />
           <attribute name="ThirdColumn" />
         </attributes>
        </modeldefinition>
      """
    And the following template named "Section_Prefix.txt":
      """
      -- Begin of template
      column_name
      -- End of template
      """

  Scenario Outline: Section with prefix single line <prefixStyle>
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Template">
          <FileFormat
            singleLineCommentPrefix="--" 
            annotationPrefix="@XGen" 
            annotationArgsPrefix="(" 
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Column" literalOnFirstLine="column" end="name" includeEnd="true" prefix="<prefix>" prefixStyle="<prefixStyle>"/> 
          </TextSections>
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="model"> 
            <SectionModelBinding section="Column" modelXPath="attributes/attribute" placeholderName="column"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix.txt" with content:
      """
      -- Begin of template
      <expected-result-1><expected-result-2><expected-result-3>
      -- End of template
      """

    Examples: 
      | prefixStyle | prefix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | /** first */FirstColumn    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | /** last */ThirdColumn      |
      | allButFirst | /** not first */ | FirstColumn                | /** not first */SecondColumn | /** not first */ThirdColumn |
      | allButLast  | /** not last */  | /** not last */FirstColumn | /** not last */SecondColumn  | ThirdColumn                 |
      | all         | /** all */       | /** all */FirstColumn      | /** all */SecondColumn       | /** all */ThirdColumn       |

  Scenario Outline: Section with prefix multi line <prefixStyle>
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Template">
          <FileFormat
            singleLineCommentPrefix="--" 
            annotationPrefix="@XGen" 
            annotationArgsPrefix="(" 
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Column" literalOnFirstLine="column" prefix="<prefix>" prefixStyle="<prefixStyle>"/> 
          </TextSections>
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="model"> 
            <SectionModelBinding section="Column" modelXPath="attributes/attribute" placeholderName="column"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix.txt" with content:
      """
      -- Begin of template
      <expected-result-1>
      <expected-result-2>
      <expected-result-3>
      -- End of template
      """

    Examples: 
      | prefixStyle | prefix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | /** first */FirstColumn    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | /** last */ThirdColumn      |
      | allButFirst | /** not first */ | FirstColumn                | /** not first */SecondColumn | /** not first */ThirdColumn |
      | allButLast  | /** not last */  | /** not last */FirstColumn | /** not last */SecondColumn  | ThirdColumn                 |
      | all         | /** all */       | /** all */FirstColumn      | /** all */SecondColumn       | /** all */ThirdColumn       |

  # For some reason only a new line (&#10; or &#xa;) will not be outputted, but in combination with a space (&#160;) it will.
  @KnownIssue
  Scenario Outline: Section with whitespace prefix single line <Scenario>
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Template">
          <FileFormat />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Column" literalOnFirstLine="column" end="name" includeEnd="true" prefix="<Prefix>" prefixStyle="allButFirst"/> 
          </TextSections>
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="model"> 
            <SectionModelBinding section="Column" modelXPath="attributes/attribute" placeholderName="column"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix.txt" with content:
      """
      -- Begin of template
      FirstColumn 
      SecondColumn 
      ThirdColumn
      -- End of template
      """

  Examples:
    | Scenario      | Prefix              |
    | without space @KnownIssue | &amp;#10;           |
    | with space    | &amp;#160;&amp;#10; |