@Unit
Feature: Unit_Config_XmlTemplate_TextTemplate_Element
  In this feature we will describe the TextTemplate config feature in an element in a XML template

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
          <entity name="Order">
            <attribute name="OrderId"/>
            <attribute name="OrderDate"/>
          </entity>
          <entity name="Customer">
            <attribute name="CustomerId"/>
            <attribute name="CustomerName"/>
          </entity>
        </system>
      </modeldefinition>
      """

  Scenario: TextTemplate without section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        system_name
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database">
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
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        ExampleSource
      </Database>
      """

  Scenario Outline: TextTemplate with single <Scenario> annotated section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        -- @XGenTextSection(name=<QuoteStyle>Tables<QuoteStyle> nrOfLines=<QuoteStyle>2<QuoteStyle>)
        -- Some comment for entity_name
        entity_name
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database">
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
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        -- Some comment for Order
        Order
        -- Some comment for Customer
        Customer
      </Database>
      """

    Examples: 
      | Scenario    | QuoteStyle |
      | not escaped | "          |
      | escaped     | &quot;     |
      | no quotes   |            |


  Scenario: TextTemplate with XPath node in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        -- @XGenTextSection(name=&quot;Tables&quot;)
        entity_name
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="//*[contains(text(), '@XGenTextSection')]">
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
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        Order
        Customer
      </Database>
      """

  Scenario: TextTemplate with single configured section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        -- Some comment in the template
        entity_name
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database">
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
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        -- Some comment in the template
        Order
        Customer
      </Database>
      """

  Scenario: TextTemplate with multiple sections annotated in template
    # Scenario based on task 220:
    # https://x-breeze.visualstudio.com/CrossGenerate/_workitems/edit/220
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        -- Some comment in the template
        -- @XGenTextSection(name="Tables" literalOnLastLine="Field:")
        Table: entity_name
        -- @XGenTextSection(name="Columns")
            Field: attribute_name
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database">
              <FileFormat currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />             
            </TextTemplate>
          </TextTemplates>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="entity" placeholderName="entity">
              <SectionModelBinding section="Columns" modelXPath="attribute" placeholderName="attribute"/>
            </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        -- Some comment in the template
        Table: Order
            Field: OrderId
            Field: OrderDate
        Table: Customer
            Field: CustomerId
            Field: CustomerName
      </Database>
      """

  Scenario: Multiple TextTemplates with single configured section in XMLTemplate
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Table name="entity_name" description="@XGenXmlSection(name='Tables')">
          -- @XGenTextSection(name='Columns')
          attribute_name
        </Table>
        <SomeOtherElement />
        <AnotherTable name="entity_name" description="@XGenXmlSection(name='Tables')">
          -- @XGenTextSection(name='Columns')
          attribute_name
        </AnotherTable>
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
          <TextTemplates>
            <TextTemplate node="/Database/*[@name='entity_name']">
              <FileFormat currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
            </TextTemplate>
          </TextTemplates>
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="entity" placeholderName="entity">
              <SectionModelBinding section="Columns" modelXPath="attribute" placeholderName="attribute"/>
            </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        <Table name="Order" description="">
          OrderId
          OrderDate
        </Table>
        <Table name="Customer" description="">
          CustomerId
          CustomerName
        </Table>
        <SomeOtherElement />
        <AnotherTable name="Order" description="">
          OrderId
          OrderDate
        </AnotherTable>
        <AnotherTable name="Customer" description="">
          CustomerId
          CustomerName
        </AnotherTable>
      </Database>
      """
