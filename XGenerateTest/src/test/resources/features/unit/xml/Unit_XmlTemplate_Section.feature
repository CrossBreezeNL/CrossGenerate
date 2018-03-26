@Unit
@Debug
Feature: Unit_XmlSection
  In this feature we will describe the section annotation feature in XML templates
  Scenario: implicit root section only
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
        </system>
      </modeldefinition>
      """
    And the following template named "ExampleTemplate.xml":
      """
     <?xml version="1.0" encoding="UTF-8"?>
     <SetOfObjects description="@XGenSection(name=&quot;system&quot;)" id="system_id">
        <system name="system_name"/>
     </SetOfObjects>
      """
    And the following config:
      """      
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
			  <Template rootSectionName="system">
			    <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@*[lower-case(local-name())='description']" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
			    <Output type="output_per_element" />
			  </Template>
			  <Binding>
				   <SectionModelBinding section="system" modelXPath="/modeldefinition/system" placeholderName="system"/>				   
			  </Binding>
		  </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
     <?xml version="1.0" encoding="UTF-8"?>
     <SetOfObjects description="" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
        <system name="ExampleSource"/>
     </SetOfObjects>
      """
      
  Scenario: root and explicit section
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
     <SetOfObjects description="@XGenSection(name=&quot;system&quot;)" id="system_id">      
        <system name="system_name"/>
        <objects>
          <object name="entity_name" description="@XGenSection(name=&quot;entity&quot;)"/>
        </objects>
     </SetOfObjects>
      """
    And the following config:
      """      
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="system">
          <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@*[lower-case(local-name())='description']" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </Template>
        <Binding>
           <SectionModelBinding section="system" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="entity" modelXPath="./entity" placeholderName="entity"/>
           </SectionModelBinding>           
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
     <?xml version="1.0" encoding="UTF-8"?>
     <SetOfObjects description="" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
        <system name="ExampleSource"/>
        <objects>
          <object name="Order" description=""/>
          <object name="Customer" description=""/>
        </objects>
     </SetOfObjects>
      """     
      
  Scenario: root and explicit recurring section
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
     <SetOfObjects description="@XGenSection(name=&quot;system&quot;)" id="system_id">      
        <system name="system_name"/>
        <objects>
          <object name="entity_name" description="@XGenSection(name=&quot;entity&quot;)"/>
        </objects>
        <moreObjects>
          <anotherObject name="entity_name" description="@XGenSection(name=&quot;entity&quot;)"/>
        </moreObjects>
     </SetOfObjects>
      """
    And the following config:
      """      
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="system">
          <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@*[lower-case(local-name())='description']" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </Template>
        <Binding>
           <SectionModelBinding section="system" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="entity" modelXPath="./entity" placeholderName="entity"/>              
           </SectionModelBinding>           
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
     <?xml version="1.0" encoding="UTF-8"?>
     <SetOfObjects description="" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
        <system name="ExampleSource"/>
        <objects>
          <object name="Order" description=""/>
          <object name="Customer" description=""/>
        </objects>
         <moreObjects>
          <anotherObject name="Order" description=""/>
          <anotherObject name="Customer" description=""/>
         </moreObjects> 
     </SetOfObjects>
      """     
  Scenario: root and explicit recursive section
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
     <SetOfObjects description="@XGenSection(name=&quot;system&quot;)" id="system_id">      
        <system name="system_name"/>
        <objects>
          <object name="entity_name" description="@XGenSection(name=&quot;entity&quot;)">
            <objectReference name="entity_name" description="@XGenSection(name=&quot;entity&quot;)"/>
          </object>  
        </objects>
     </SetOfObjects>
      """
    And the following config:
      """      
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="system">
          <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@*[lower-case(local-name())='description']" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="output_per_element" />
        </Template>
        <Binding>
           <SectionModelBinding section="system" modelXPath="/modeldefinition/system" placeholderName="system">
            <SectionModelBinding section="entity" modelXPath="/modeldefinition/system/entity" placeholderName="entity">
              <SectionModelBinding section="entity" modelXPath="../entity" placeholderName="entity"/>
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
     <SetOfObjects description="" id="29e17cc2-efd2-4013-8f9a-5714081874b3">
        <system name="ExampleSource"/>
        <objects>
          <object name="Order" description="">
            <objectReference name="Order" description=""/>
            <objectReference name="Customer" description=""/>
          </object>
          <object name="Customer" description="">
            <objectReference name="Order" description=""/>
            <objectReference name="Customer" description=""/>
          </object>          
        </objects>       
     </SetOfObjects>
      """      