@Unit
Feature: Unit_TextTemplate_Section_Prefix
  In this feature we will describe the section prefix feature in text templates.

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
        <TextTemplate rootSectionName="Template">
          <FileFormat
            singleLineCommentPrefix="--" 
            annotationPrefix="@XGen" 
            annotationArgsPrefix="(" 
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition" placeholderName="model"> 
            <SectionModelBinding section="Column" modelXPath="attributes/attribute" placeholderName="column"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """

  Scenario Outline: Section with prefix single line <prefixStyle>
    And the following template named "Section_Prefix_single_line_<prefixStyle>.txt":
      """
      -- @XGenTextSection(name="Column" end="column_name" prefix="<prefix>" prefixStyle="<prefixStyle>")
      column_name
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_single_line_<prefixStyle>.txt" with content:
      """
      <expected-result-1><expected-result-2><expected-result-3>
      """

    Examples: 
      | prefixStyle | prefix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | /** first */FirstColumn    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | /** last */ThirdColumn      |
      | allButFirst | /** not first */ | FirstColumn                | /** not first */SecondColumn | /** not first */ThirdColumn |
      | allButLast  | /** not last */  | /** not last */FirstColumn | /** not last */SecondColumn  | ThirdColumn                 |
      | all         | /** all */       | /** all */FirstColumn      | /** all */SecondColumn       | /** all */ThirdColumn       |

  Scenario Outline: Section with prefix multi line <prefixStyle>
    And the following template named "Section_Prefix_multi_line_<prefixStyle>.txt":
      """
      -- @XGenTextSection(name="Column" prefix="<prefix>" prefixStyle="<prefixStyle>")
      column_name

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_multi_line_<prefixStyle>.txt" with content:
      """
      <expected-result-1>
      <expected-result-2>
      <expected-result-3>

      """

    Examples: 
      | prefixStyle | prefix           | expected-result-1          | expected-result-2            | expected-result-3           |
      | firstOnly   | /** first */     | /** first */FirstColumn    | SecondColumn                 | ThirdColumn                 |
      | lastOnly    | /** last */      | FirstColumn                | SecondColumn                 | /** last */ThirdColumn      |
      | allButFirst | /** not first */ | FirstColumn                | /** not first */SecondColumn | /** not first */ThirdColumn |
      | allButLast  | /** not last */  | /** not last */FirstColumn | /** not last */SecondColumn  | ThirdColumn                 |
      | all         | /** all */       | /** all */FirstColumn      | /** all */SecondColumn       | /** all */ThirdColumn       |

  Scenario: Section with prefix single line with whitespace
    And the following template named "Section_Prefix_single_line_indented.txt":
      """
        -- @XGenTextSection(name="Column" prefix=",")
        column_name

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_single_line_indented.txt" with content:
      """
        FirstColumn
        ,SecondColumn
        ,ThirdColumn

      """

  Scenario: Section with suffix single line with whitespace
    And the following template named "Section_Prefix_single_line_indented.txt":
      """
        -- @XGenTextSection(name="Column" suffix=",")
        column_name  

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_single_line_indented.txt" with content:
      """
        FirstColumn,  
        SecondColumn,  
        ThirdColumn  

      """
  
  Scenario: Section with whitespace prefix
    And the following template named "Section_Prefix_single_line_indented.txt":
      """
        -- @XGenTextSection(name="Column" prefix="&#160;")
        column_name

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_single_line_indented.txt" with content:
      """
        FirstColumn
         SecondColumn
         ThirdColumn

      """
