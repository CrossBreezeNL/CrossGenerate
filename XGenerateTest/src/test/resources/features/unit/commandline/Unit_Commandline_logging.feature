@Unit
Feature: Unit_commandline_logging
  In this feature we'll test the commandline options for loglevel and log location.

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
        <Template rootSectionName="Template">
          <FileFormat templateType="text" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """      
  Scenario: No logging from commandline
    Given the following app config:
      """
      <?xml version="1.0" encoding="utf-8"?>
			<XGenAppConfig>
			  <App>
			    <ConfigFolder>C:\CrossGenerate\Test\Config\</ConfigFolder>
			    <ModelFolder>C:\CrossGenerate\Test\Model\</ModelFolder>
			    <OutputFolder>C:\CrossGenerate\Test\Output\</OutputFolder>
			    <TemplateFolder>C:\CrossGenerate\Test\Template\</TemplateFolder>
			  </App>
			  <License>
			    <ContractId>0</ContractId>
			    <DeveloperMode>true</DeveloperMode>
			    <LicenseKey>0</LicenseKey>
			    <Tag></Tag>
			    <Url>file:///C:/GIT/Repos/CrossBreeze/CrossGenerate/CrossGenerate/XGenerateLib/target/classes/</Url>
			    <Version>2.0</Version>
			  </License>
			</XGenAppConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And no log file
    
