@Unit
Feature: Unit_Config_TextTemplate_Section_Configured_But_Missing
  In this feature we will describe the scenario of a section that is defined in the config but missing in the template.

  Background: 
    Given I have the following model:
      """
      <modeldefinition>
      <system name="ExampleSource">
       <mappableObjects>
         <entity name="Order" />
         <entity name="Customer" />
       </mappableObjects>
      </system>
      </modeldefinition>
      """

  Scenario Outline: Config with section defined not present in template, optional is <Optional>
    Given the following template named "Template.txt":
      """
      -- Begin of template
      entity_name
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="Template">
          <FileFormat
            singleLineCommentPrefix="--" 
            annotationPrefix="@XGen" 
            annotationArgsPrefix="(" 
            annotationArgsSuffix=")"
          />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="entity" begin="entity" includeBegin="true"/>
            <TextSection name="missing" begin="dummy" optional="<Optional>" />
          </TextSections>
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/modeldefinition/system" placeholderName="model"> 
            <SectionModelBinding section="entity" modelXPath="mappableObjects/entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Template.txt" with content:
      """
      -- Begin of template
      Order
      Customer
      -- End of template
      """
    And I expect the following log message:
      """
      <ErrorLevel> The begin part of the section 'missing' can't be found (begin: 'dummy') 
      """

    Examples: 
      | Optional | ErrorLevel |
      | false    | [WARNING]  |
      | true     | [INFO   ]  |
