@Unit
Feature: Unit_XmlTemplate_Section_Whitespace
  In this feature we will describe the section annotation whitespace feature in XML templates
  
  Background:
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
          <entity name="Order">
            <attribute name="OrderId" />
            <attribute name="OrderDate" />
          </entity>
          <entity name="Customer">
            <attribute name="CustomerId" />
            <attribute name="FirstName" />
            <attribute name="LastName" />
          </entity>
        </system>
      </modeldefinition>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="./entity" placeholderName="entity">
              <SectionModelBinding section="Attributes" modelXPath="./attribute" placeholderName="attribute"/>
            </SectionModelBinding>
          </SectionModelBinding>           
        </Binding>
      </XGenConfig>
      """

  Scenario: Whitespace before section
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
        
          <Table name="entity_name" description="@XGenXmlSection(name=&quot;Tables&quot;)"/>
        </Tables>
      </Database>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        <Tables>
        
          <Table name="Order" description=""/>
          <Table name="Customer" description=""/>
        </Tables>
      </Database>
      """
      
  Scenario: Whitespace after section
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenXmlSection(name=&quot;Tables&quot;)"/>
          
        </Tables>
      </Database>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        <Tables>
          <Table name="Order" description=""/>
          <Table name="Customer" description=""/>
          
        </Tables>
      </Database>
      """
      
  Scenario: Whitespace before nested section
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenXmlSection(name=&quot;Tables&quot;)">
            <Columns>
            
              <Column name="attribute_name" description="@XGenXmlSection(name=&quot;Attributes&quot;)"/>
            </Columns>
          </Table>
        </Tables>
      </Database>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        <Tables>
          <Table name="Order" description="">
            <Columns>
            
              <Column name="OrderId" description=""/>
              <Column name="OrderDate" description=""/>
            </Columns>
          </Table>
          <Table name="Customer" description="">
            <Columns>
            
              <Column name="CustomerId" description=""/>
              <Column name="FirstName" description=""/>
              <Column name="LastName" description=""/>
            </Columns>
          </Table>
        </Tables>
      </Database>
      """
      
  Scenario: Whitespace after nested section
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenXmlSection(name=&quot;Tables&quot;)">
            <Columns>
              <Column name="attribute_name" description="@XGenXmlSection(name=&quot;Attributes&quot;)"/>
              
            </Columns>
          </Table>
        </Tables>
      </Database>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        <Tables>
          <Table name="Order" description="">
            <Columns>
              <Column name="OrderId" description=""/>
              <Column name="OrderDate" description=""/>
              
            </Columns>
          </Table>
          <Table name="Customer" description="">
            <Columns>
              <Column name="CustomerId" description=""/>
              <Column name="FirstName" description=""/>
              <Column name="LastName" description=""/>
              
            </Columns>
          </Table>
        </Tables>
      </Database>
      """
      
  Scenario: Whitespace between adjecent section
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenXmlSection(name=&quot;Tables&quot;)"/>
          
          <AnotherTable name="entity_name" description="@XGenXmlSection(name=&quot;Tables&quot;)"/>
        </Tables>
      </Database>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource">
        <Tables>
          <Table name="Order" description=""/>
          <Table name="Customer" description=""/>
          
          <AnotherTable name="Order" description=""/>
          <AnotherTable name="Customer" description=""/>
        </Tables>
      </Database>
      """