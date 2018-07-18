@Unit
Feature: Unit_Config_Reuse_Partials
  In this feature we will describe the feature to reuse parts of a config by including (sub) XML files.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <system name="sys">
       <entities>
         <entity name="A"/>
         <entity name="B"/>      
       </entities>      
      </system>
      """
    And the following template named "Unit_Config_Reuse_Partials.txt":
      """ 
      -- @XGenTextSection(name="Tables")
      table_name -> system_name;
      
      """
@Debug
  Scenario Outline: Reuse of binding <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>            
      <XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
        <TextTemplate rootSectionName="Template">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </TextTemplate>              
        <Binding>        
          <xi:include href="C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\XGenerateLibTest\src\test\resources\feature-support-files\unit\config\Reuse_Partials\<bindingFile>"/>
        </Binding>          
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Reuse_Partials.txt" with content:
      """
      <expectedResultA>
      <expectedResultB>
      
      """

    Examples: 
      | Scenario              | bindingFile       | expectedResultA | expectedResultB | 
      | No Nesting | entityBinding.xml | A -> sys;        | B -> sys;        |
      | Nested include for placeholders | entityBindingWithInclude.xml | A -> sys;        | B -> sys;        |
      
      
