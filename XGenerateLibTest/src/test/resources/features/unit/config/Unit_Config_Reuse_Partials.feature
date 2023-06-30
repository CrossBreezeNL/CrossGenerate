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
          <xi:include href="<includeHref>" <xpointer> />
        </Binding>          
      </XGenConfig>
      """
    And the following template named "Unit_Config_Reuse_Partials.txt":
      """
      -- @XGenTextSection(name="Tables")
      table_name -> system_name;

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Reuse_Partials.txt" with content:
      """
      <expectedResultA>
      <expectedResultB>

      """

    Examples: 
      | Scenario                                       | includeHref                                                                                                                                                                                      | xpointer                                                            | expectedResultA | expectedResultB |
      | No Nesting                                     | C:\\git\\CrossBreeze\\CrossGenerateJava\\CrossGenerateJava\\XGenerateLibTest\\src\\test\\resources\\feature-support-files\\unit\\config\\Unit_Config_Reuse_Partials\\entityBinding.xml            |                                                                     | A -> sys;       | B -> sys;       |
      | Nested include for placeholders absolute       | C:\\git\\CrossBreeze\\CrossGenerateJava\\CrossGenerateJava\\XGenerateLibTest\\src\\test\\resources\\feature-support-files\\unit\\config\\Unit_Config_Reuse_Partials\\entityBindingWithInclude.xml |                                                                     | A -> sys;       | B -> sys;       |
      | Nested include for placeholders relative       | entityBindingWithIncludeRelative.xml                                                                                                                                                             |                                                                     | A -> sys;       | B -> sys;       |
      | Nested include for placeholders multiple times | entityBindingWithIncludeMultiple.xml                                                                                                                                                             |                                                                     | A -> sys;       | B -> sys;       |
      | Nested include and multiple include files      | entityBindingWithMultipleInclude.xml                                                                                                                                                             |                                                                     | A -> sys;       | B -> sys;       |
      | Nested include using xpointer                  | entityBindingWithXPointer.xml                                                                                                                                                                    |                                                                     | A -> sys;       | B -> sys;       |
      | Nested include using xpointer twice            | entityBindingWithXPointerTwice.xml                                                                                                                                                               |                                                                     | A -> sys;       | B -> sys;       |
      | Using xpointer with global accessor            | modelBindingWithParentElement.xml                                                                                                                                                                | xpointer="//SectionModelBinding[@section='Template']" | A -> sys;       | B -> sys;       |
      | Using xpointer with root accessor              | modelBindingWithParentElement.xml                                                                                                                                                                | xpointer="/SomeRootElement/SectionModelBinding[@section='Template']" | A -> sys;       | B -> sys;       |

  Scenario: using the same include twice, nested
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>            
      <XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
        <TextTemplate rootSectionName="Template">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </TextTemplate>              
        <Binding>        
          <xi:include href="entityBindingWithIncludeNested.xml"/>
        </Binding>          
      </XGenConfig>
      """
    And the following template named "Unit_Config_Reuse_Partials.txt":
      """
      -- @XGenTextSection(name="Tables")
      table_name -> system_name;
      """
    Then I expect the following error message:
      """
      Config include cycle detected at level 3, file file:///C:/git/CrossBreeze/CrossGenerateJava/CrossGenerateJava/XGenerateLibTest/target/test-classes/feature-support-files/unit/config/Unit_Config_Reuse_Partials/entityBindingWithIncludeNested.xml is already included previously
      """
