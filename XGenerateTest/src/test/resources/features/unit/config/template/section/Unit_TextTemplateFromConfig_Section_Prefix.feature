@Unit
Feature: Unit_TextTemplate_SectionFromConfig_Prefix
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
        <Template rootSectionName="Template">
          <FileFormat
            templateType="text" 
            singleLineCommentPrefix="--" 
            annotationPrefix="@XGen" 
            annotationArgsPrefix="(" 
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
          <Sections>
            <Section name="Column" literalOnFirstLine="column" end="name" includeEnd="true" prefix="<prefix>" prefixStyle="<prefixStyle>"/> 
          </Sections>
        </Template>
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


  Scenario Outline: Section with prefix multi line <prefixStyle>   
      
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
          <Sections>
            <Section name="Column" literalOnFirstLine="column" prefix="<prefix>" prefixStyle="<prefixStyle>"/> 
          </Sections>
        </Template>
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
