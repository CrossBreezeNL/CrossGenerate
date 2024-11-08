@Unit
Feature: Unit_Model_Xml_Include
  In this feature we will describe the feature to use Xml Include instructions in the model file.

  Scenario Outline: Xml include in model file <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>            
      <XGenConfig>
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
      <expected-result>

      """

    Examples: 
      | Scenario                            | NamespaceAware | Namespace                                  | expected-result |
      # When namespace aware is enabled and the namespace is in the model file, includes are resolved.
      | Namespace aware with namespace      | true           | xmlns:xi="http://www.w3.org/2001/XInclude" | A\nB            |
      # When the namespace is not in the model file, model includes are not resolved.
      | Namespace unaware with namespace    | false          | xmlns:xi="http://www.w3.org/2001/XInclude" | A               |
      # When parsing namespace unaware and the namespace is not in the the model file, includes are not resolved.
      | Namespace unaware without namespace | false          |                                            | A               |

  Scenario Outline: Xml include in model file <Scenario>
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>            
      <XGenConfig>
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
      <ErrorMessage>
      """

    Examples: 
      | Scenario                          | NamespaceAware | Namespace | ErrorMessage                                                                               |
      | Namespace aware without namespace | true           |           | Error while reading model XML file: The prefix "xi" for element "xi:include" is not bound. |
