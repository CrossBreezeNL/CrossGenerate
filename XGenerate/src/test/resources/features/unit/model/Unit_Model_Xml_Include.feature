@Unit
Feature: Unit_Model_Xml_Include
  In this feature we will describe the feature to use Xml Include instructions in the model file.

  Scenario Outline: Xml include in model file <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>            
      <XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
        <Model namespaceAware="<NamespaceAware>" />
        <TextTemplate rootSectionName="Template">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </TextTemplate>              
        <Binding>        
      <SectionModelBinding section="Template" modelXPath ="/system" placeholderName="system">
      <SectionModelBinding section="Tables" modelXPath="./entities/entity" placeholderName="table">
      	<Placeholders>
      		<Placeholder name="system" modelXPath="../.." />
      	</Placeholders>
      </SectionModelBinding>
      </SectionModelBinding>
        </Binding>          
      </XGenConfig>
      """
    And I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <system name="sys" <Namespace>>
       <entities>
         <entity name="A"/>
         <xi:include href="included-model-file.xml" />      
       </entities>      
      </system>
      """
    And the following template named "Unit_Config_Reuse_Partials.txt":
      """
      -- @XGenTextSection(name="Tables")
      table_name

      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Reuse_Partials.txt" with content:
      """
      A
      B

      """

    Examples: 
      | Scenario                            | NamespaceAware | Namespace                                  |
      | Namespace aware with namespace      | true           | xmlns:xi="http://www.w3.org/2001/XInclude" |
      | Namespace unaware with namespace    | false          | xmlns:xi="http://www.w3.org/2001/XInclude" |
      | Namespace unaware without namespace | false          |                                            |

  Scenario Outline: Xml include in model file <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>            
      <XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
        <Model namespaceAware="<NamespaceAware>" />
        <TextTemplate rootSectionName="Template">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </TextTemplate>              
        <Binding>        
      <SectionModelBinding section="Template" modelXPath ="/system" placeholderName="system">
      <SectionModelBinding section="Tables" modelXPath="./entities/entity" placeholderName="table">
      	<Placeholders>
      		<Placeholder name="system" modelXPath="../.." />
      	</Placeholders>
      </SectionModelBinding>
      </SectionModelBinding>
        </Binding>          
      </XGenConfig>
      """
    And I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <system name="sys" <Namespace>>
       <entities>
         <entity name="A"/>
         <xi:include href="included-model-file.xml" />      
       </entities>      
      </system>
      """
    Then I expect the following error message:
      """
			Error while reading model: Error while parsing file as XML document: Name space qualification Exception: Element not qualified
			
			Line Number: 5 Offset: 4.
      """

    Examples: 
      | Scenario                          | NamespaceAware | Namespace |
      | Namespace aware without namespace | true           |           |
