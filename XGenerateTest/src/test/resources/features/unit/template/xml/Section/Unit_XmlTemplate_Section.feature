@Unit
Feature: Unit_XmlTemplate_Section
  In this feature we will describe the section annotation feature in XML templates

  Scenario: Implicit root section only
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource" id="29e17cc2-efd2-4013-8f9a-5714081874b3"/>
      </modeldefinition>
      """
    And the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name"/>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat templateType="xml" currentAccessor="_"/>
          <Output type="output_per_element"/>
        </Template>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system"/>				   
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource"/>
      """

  @KnownIssue
  Scenario: Implicit root and explicit section
    # KnownIssue: Here there is a problem with the newlines and whitespaces, and also with the order of attribute.
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
    And the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenSection(name=&quot;Tables&quot;)"/>
        </Tables>
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </Template>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="./entity" placeholderName="entity"/>
          </SectionModelBinding>           
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
    
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="29e17cc2-efd2-4013-8f9a-5714081874b3" name="ExampleSource"/>
        <Tables>
          <Table name="Order" description=""/>
          <Table name="Customer" description=""/>
        </Objects>
      </Database>
      """

  @KnownIssue
  Scenario: Implicit root and explicit recurring section
    # KnownIssue: Here there is a problem with the newlines and whitespaces, and also with the order of attribute.
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
    And the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenSection(name=&quot;Tables&quot;)"/>
        </Tables>
        <MoreTables>
          <AnotherTable name="entity_name" description="@XGenSection(name=&quot;Tables&quot;)"/>
        </MoreTables>
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </Template>
        <Binding>
           <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="./entity" placeholderName="entity"/>              
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
        <Tables>
          <Table name="Order" description=""/>
          <Table name="Customer" description=""/>
        </Tables>
        <MoreTables>
          <AnotherTable name="Order" description=""/>
          <AnotherTable name="Customer" description=""/>
        </MoreTables> 
      </Database>
      """

  @KnownIssue
  Scenario: Implicit root and explicit recursive section
    # KnownIssue: Here there is a problem with the newlines and whitespaces, and also with the order of attribute.
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
    And the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenSection(name=&quot;Tables&quot;)">
            <AnotherTable name="entity_name" description="@XGenSection(name=&quot;Tables&quot;)"/>
          </Table>  
        </Tables>
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@*[lower-case(local-name())='description']" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </Template>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="./entity" placeholderName="entity">
              <SectionModelBinding section="Tables" modelXPath="../entity" placeholderName="entity"/>
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
        <Tables>
          <Table name="Order" description="">
            <AnotherTable name="Order" description=""/>
            <AnotherTable name="Customer" description=""/>
          </Table>
          <Table name="Customer" description="">
            <AnotherTable name="Order" description=""/>
            <AnotherTable name="Customer" description=""/>
          </Table>          
        </Tables>       
      </Database>
      """
      
  @KnownIssue
  Scenario: Implicit root and explicit nested section
    # KnownIssue: Here there is a problem with the newlines and whitespaces, and also with the order of attribute.
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
          <entity name="Order">
            <attributes>
              <attribute name="OrderId"/>
              <attribute name="OrderDate"/>
            </attributes>
          </entity>
          <entity name="Customer">
            <attributes>
              <attribute name="CustomerId"/>
              <attribute name="FirstName"/>
              <attribute name="LastName"/>
            </attributes>
          </entity>
        </system>
      </modeldefinition>
      """
    And the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database id="system_id" name="system_name">
        <Tables>
          <Table name="entity_name" description="@XGenSection(name=&quot;Tables&quot;)">
            <Columns>
              <Column name="attribute_name" description="@XGenSection(name=&quot;Columns&quot;)"/>
            </Columns>
          </Table>  
        </Tables>
      </Database>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="Database">
          <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@*[lower-case(local-name())='description']" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </Template>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="Tables" modelXPath="./entity" placeholderName="entity">
              <SectionModelBinding section="Columns" modelXPath="./attributes/attribute" placeholderName="attribute"/>
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
        <Tables>
          <Table name="Order" description="">
            <Columns>
              <Column name="OrderId"/>
              <Column name="OrderDate"/>
            </Columns>
          </Table>
          <Table name="Customer" description="">
            <Columns>
              <Column name="CustomerId"/>
              <Column name="FirstName"/>
              <Column name="LastName"/>
            </Columns>
          </Table>          
        </Tables>       
      </Database>
      """
