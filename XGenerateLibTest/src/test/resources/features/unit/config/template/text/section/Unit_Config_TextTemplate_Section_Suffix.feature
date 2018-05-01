@Unit
Feature: Unit_Config_TextTemplate_Section_Suffix
  In this feature we will describe the section suffix feature in text templates.

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
    And the following template named "Section_Suffix.txt":
      """
      -- Begin of template
      column_name
      -- End of template
      """

  Scenario Outline: Section with suffix single line <suffixStyle>
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
          <Sections>
            <Section name="Column" begin="column" includeBegin="true" end="name" includeEnd="true" suffix="<suffix>" suffixStyle="<suffixStyle>"/>
          </Sections>
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
    And an output named "Section_Suffix.txt" with content:
      """
      -- Begin of template
      <expected-result-1><expected-result-2><expected-result-3>
      -- End of template
      """

    Examples: 
      | suffixStyle | suffix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | FirstColumn/** first */    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | ThirdColumn/** last */      |
      | allButFirst | /** not first */ | FirstColumn                | SecondColumn/** not first */ | ThirdColumn/** not first */ |
      | allButLast  | /** not last */  | FirstColumn/** not last */ | SecondColumn/** not last */  | ThirdColumn                 |

  Scenario Outline: Section with suffix multi line <suffixStyle>
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
          <Sections>
            <Section name="Column" begin="column" includeBegin="true" suffix="<suffix>" suffixStyle="<suffixStyle>"/>
          </Sections>
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
    And an output named "Section_Suffix.txt" with content:
      """
      -- Begin of template
      <expected-result-1>
      <expected-result-2>
      <expected-result-3>
      -- End of template
      """

    Examples: 
      | suffixStyle | suffix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | FirstColumn/** first */    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | ThirdColumn/** last */      |
      | allButFirst | /** not first */ | FirstColumn                | SecondColumn/** not first */ | ThirdColumn/** not first */ |
      | allButLast  | /** not last */  | FirstColumn/** not last */ | SecondColumn/** not last */  | ThirdColumn                 |
