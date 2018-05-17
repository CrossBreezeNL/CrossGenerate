@Unit
Feature: Unit_Config_XmlTemplate_TextTemplate_Attribute
  In this feature we will describe the TextTemplate config feature in an attribute in a XML template
  
  Background:
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
          <entity name="Order"/>
          <entity name="Customer"/>
        </system>
      </modeldefinition>
      """
      
  Scenario: TextTemplate without section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name" description="This is database: 'system_name'" />
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database/@description">
              <FileFormat currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
            </TextTemplate>
          </TextTemplates>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource" description="This is database: 'ExampleSource'" />
      """
      
  Scenario: TextTemplate with annotated single-line section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name" description="-- @XGenTextSection(name=&quot;Tables&quot; begin=&quot;entity&quot; end=&quot;name&quot; suffix=&quot;,&quot;) entity_name" />
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database/@description">
              <FileFormat currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
            </TextTemplate>
          </TextTemplates>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource" description="--  Order,Customer" />
      """

  Scenario: TextTemplate with annotated multi-line section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name" description="-- @XGenTextSection(name=&quot;Tables&quot;)
      entity_name
      " />
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database/@description">
              <FileFormat currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
            </TextTemplate>
          </TextTemplates>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource" description="Order
      Customer
      " />
      """
      
  Scenario: TextTemplate with configured multi-line section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name" description="-- Some comment in the template
      entity_name
      " />
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database/@description">
              <FileFormat currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
              <TextSections>
                <TextSection name="Tables" literalOnFirstLine="entity_name" />
              </TextSections>
            </TextTemplate>
          </TextTemplates>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource" description="-- Some comment in the template
      Order
      Customer
      " />
      """
