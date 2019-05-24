@Unit
Feature: Unit_Config_Binding_SectionModelBinding_Namespaces
  In this feature we will describe the SectionModelBinding feature in the binding config using namespaces in the model.

  @Debug
  Scenario Outline: Binding with namespace <Scenario>
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities xmlns:ns="http://testnamespace.com/a" primary="A">
        <entity name="A" description="default ns" <DefaultNamespaceInModel>/>
        <ns:entity name="A" description="a namespace"/>        
      </entities>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
        	<ModelNamespaces>
        		<ModelNamespace prefix="<NamespacePrefixInConfig>" namespace="<NamespaceInConfig>"/>
        	</ModelNamespaces>
        </Model>
        <TextTemplate rootSectionName="tables">
         <FileFormat singleLineCommentPrefix="--" />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
        <SectionModelBinding section="tables" modelXPath="/entities">
          <SectionModelBinding section="table" modelXPath="<Entity>" />
        </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    And the following template named "Unit_Config_Binding_SectionModelBinding.txt":
      """
      -- @XGenTextSection(name='table' end='description')
      table_name table_description
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Binding_SectionModelBinding.txt" with content:
      """
      <Output>
      """

    #in Xpath/XSLT, a default namespace requires a prefix as well, see also https://stackoverflow.com/questions/1344158/xslt-with-xml-source-that-has-a-default-namespace-set-to-xmlns
    Examples: 
      | Scenario                          | DefaultNamespaceInModel                  | NamespacePrefixInConfig | NamespaceInConfig                | Entity               | Output        |
      | Default defined without alias     | xmlns="http://testnamespace.com/default" |                         | http://testnamespace.com/default | entity[@name='A']    |               |
      | Default defined with alias        | xmlns="http://testnamespace.com/default" | my                      | http://testnamespace.com/default | my:entity[@name='A'] | A default ns  |
      | Default but not defined in config | xmlns="http://testnamespace.com/default" |                         |                                  | entity[@name='A']    |               |
      | No Default in model               |                                          |                         |                                  | entity[@name='A']    | A default ns  |
      | A namespace                       |                                          | ns                      | http://testnamespace.com/a       | ns:entity[@name='A'] | A a namespace |
