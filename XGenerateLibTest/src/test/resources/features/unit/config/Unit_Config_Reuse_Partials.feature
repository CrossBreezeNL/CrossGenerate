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
          <xi:include href="<path>\<bindingFile>"/>
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
      | Scenario                                 | bindingFile                  | path                                                                                                                                                   | expectedResultA | expectedResultB |  |
      | No Nesting                               | entityBinding.xml            | C:\\GIT\\Repos\\CrossBreeze\\CrossGenerate\\XGenerateLibTest\\src\\test\\resources\\feature-support-files\\unit\\config\\Unit_Config_Reuse_Partials | A -> sys;       | B -> sys;       |  |
      | Nested include for placeholders absolute | entityBindingWithInclude.xml | C:\\GIT\\Repos\\CrossBreeze\\CrossGenerate\\XGenerateLibTest\\src\\test\\resources\\feature-support-files\\unit\\config\\Unit_Config_Reuse_Partials | A -> sys;       | B -> sys;       |  |
      | Nested include for placeholders relative | entityBindingWithIncludeRelative.xml | .                                                                              | A -> sys;       | B -> sys;       |  |

      
Scenario: using the same include twice, but not nested
   Given the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>            
    <XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
      <TextTemplate rootSectionName="Template">
        <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </TextTemplate>              
      <Binding>        
        <xi:include href=".\entityBindingWithIncludeMultiple.xml"/>
      </Binding>          
    </XGenConfig>
    """
      
		And the following template named "Unit_Config_Reuse_Partials.txt":
    """
    -- @XGenTextSection(name="TableA")
    table_name -> system_name;
		-- @XGenTextSection(name="TableB")
    table_name -> system_name;

    """
      
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Reuse_Partials.txt" with content:
      """
      A -> sys;
     	B -> sys;

      """
  
    
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
        <xi:include href=".\entityBindingWithIncludeNested.xml"/>
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
    Config include cycle detected at level 3, file file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/XGenerateLibTest/src/test/resources/feature-support-files/unit/config/Unit_Config_Reuse_Partials/entityBindingWithIncludeNested.xml is already included previously
    """