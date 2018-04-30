@Unit
Feature: Unit_TextTemplate_Section_Suffix
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
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="Template">
          <FileFormat
            templateType="text" 
            singleLineCommentPrefix="--" 
            annotationPrefix="@XGen" 
            annotationArgsPrefix="(" 
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="model"> 
            <SectionModelBinding section="Column" modelXPath="attributes/attribute" placeholderName="column"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """

  Scenario Outline: Section with suffix single line <suffixStyle>
    And the following template named "Section_Suffix_single_line_<suffixStyle>.txt":
      """
      -- @XGenSection(name="Column" end="column_name" suffix="<suffix>" suffixStyle="<suffixStyle>")
      column_name
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Suffix_single_line_<suffixStyle>.txt" with content:
      """
      <expected-result-1><expected-result-2><expected-result-3>
      """

    Examples: 
      | suffixStyle | suffix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | FirstColumn/** first */    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | ThirdColumn/** last */      |
      | allButFirst | /** not first */ | FirstColumn                | SecondColumn/** not first */ | ThirdColumn/** not first */ |
      | allButLast  | /** not last */  | FirstColumn/** not last */ | SecondColumn/** not last */  | ThirdColumn                 |

  Scenario Outline: Section with suffix multi line <suffixStyle>
    And the following template named "Section_Suffix_multi_line_<suffixStyle>.txt":
      """
      -- @XGenSection(name="Column" suffix="<suffix>" suffixStyle="<suffixStyle>")
      column_name

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Suffix_multi_line_<suffixStyle>.txt" with content:
      """
      <expected-result-1>
      <expected-result-2>
      <expected-result-3>

      """

    Examples: 
      | suffixStyle | suffix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | FirstColumn/** first */    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | ThirdColumn/** last */      |
      | allButFirst | /** not first */ | FirstColumn                | SecondColumn/** not first */ | ThirdColumn/** not first */ |
      | allButLast  | /** not last */  | FirstColumn/** not last */ | SecondColumn/** not last */  | ThirdColumn                 |
