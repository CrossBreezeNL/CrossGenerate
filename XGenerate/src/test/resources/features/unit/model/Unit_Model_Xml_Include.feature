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
         <xi:include href="included-model-file.xml" <xi-include-ns> />      
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
      | Scenario                            | NamespaceAware | Namespace                                  | xi-include-ns                              | expected-result    |
      # When namespace aware and the XInclude namespace is on the root node, includes are resolved.
      | Namespace aware with namespace      | true           | xmlns:xi="http://www.w3.org/2001/XInclude" |                                            | A\nB               |
      # When not namespace aware, but the XInclude namespace is on the root node, model includes are resolved.
      | Namespace unaware with namespace    | false          | xmlns:xi="http://www.w3.org/2001/XInclude" |                                            | A\nB               |
      # When namespace aware, and the XInclude namespace is in xi:include node, model includes are resolved.
      | Namespace aware inline namespace | true              |                                            | xmlns:xi="http://www.w3.org/2001/XInclude" | A\nB               |
      # When namespace unaware, and the XInclude namespace is in xi:include node, model includes are resolved.
      | Namespace unaware inline namespace | false           |                                            | xmlns:xi="http://www.w3.org/2001/XInclude" | A\nB               |

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
      | Scenario                            | NamespaceAware | Namespace | ErrorMessage                                                                               |
      # When parsing namespace aware and the namespace is not in the the model file, it results in an error.
      | Namespace aware without namespace   | true           |           | Error while reading model: The prefix "xi" for element "xi:include" is not bound.          |
      # When parsing namespace unaware and the namespace is not in the the model file, it results in an error.
      | Namespace unaware without namespace | true           |           | Error while reading model: The prefix "xi" for element "xi:include" is not bound.          |
