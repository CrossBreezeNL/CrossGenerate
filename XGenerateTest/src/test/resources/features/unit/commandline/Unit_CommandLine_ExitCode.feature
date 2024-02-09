@Unit
Feature: Unit_CommandLine_ExitCode
  In this feature we'll test the exit code of CrossGenerate.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A"/>
        <entity name="B"/>
        <entity name="C"/>
      </entities>
      """
    And the following template named "Unit_Config_Template_OutputType_table_name.txt":
      """
      table_name      
      """
    And the following config:
      """
         <XGenConfig>
          <Model/>
          <TextTemplate rootSectionName="Template">
            <Output type="single_output" />
          </TextTemplate>
          <Binding>
            <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
          </Binding>
        </XGenConfig>
      """
    And the following app config:
      """
      <?xml version="1.0" encoding="utf-8"?>
      <XGenAppConfig>
       <App>
         <ConfigFolder>C:\git\CrossBreeze\CrossGenerateJava\CrossGenerateJava\XGenerateTest\src\test\resources\feature-support-files\Config\</ConfigFolder>
         <ModelFolder>C:\git\CrossBreeze\CrossGenerateJava\CrossGenerateJava\XGenerateTest\src\test\resources\feature-support-files\Model\</ModelFolder>
         <OutputFolder>C:\CrossGenerate\Test\Output\</OutputFolder>
         <TemplateFolder>C:\git\CrossBreeze\CrossGenerateJava\CrossGenerateJava\XGenerateTest\src\test\resources\feature-support-files\Template\</TemplateFolder>
       </App>
      </XGenAppConfig>
      """
    And the directory "C:\CrossGenerate\Test\Log" is empty.

  Scenario: Successfull run
    When I run the generator
    Then I expect exit code 0
  
  Scenario: Run which result in error
  	Given the following config:
      """
         <XGenConfig>
          <Model/>
          <TextTemplate/>
          <Binding/>
        </XGenConfig>
      """
    When I run the generator
    Then I expect exit code 1