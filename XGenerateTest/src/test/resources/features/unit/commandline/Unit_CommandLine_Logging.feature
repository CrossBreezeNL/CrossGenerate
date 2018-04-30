@Unit
Feature: Unit_CommandLine_Logging
  In this feature we'll test the command line options for loglevel and log location.

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
    And the following app config:
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

  Scenario: No logging from commandline
    When I run the generator
    Then I expect 1 generation result
    And no log file

  Scenario: logging from commandline, incorrect log level parameter
    Given the following additional comma separated commandline arguments:
      """
      -loglevel, super, -logdestination, C:\CrossGenerate\Test\Log\testlog.log
      """
    When I run the generator
    Then I expect 0 generation result
    And no log file

  Scenario: logging from commandline, only warnings and severe
    Given the following additional comma separated commandline arguments:
      """
      -loglevelfile, warning, -loglevelconsole, warning, -logdestination, C:\CrossGenerate\Test\Log\testlog.log
      """
    When I run the generator
    Then I expect 1 generation result
    And a log file containing "[severe]" but not containing "[info]"

  Scenario: logging from commandline, info warnings and severe
    Given the following additional comma separated commandline arguments:
      """
      -loglevelfile, info, -loglevelconsole, warning, -logdestination, C:\CrossGenerate\Test\Log\testlog.log
      """
    When I run the generator
    Then I expect 1 generation result
    And a log file containing "[info]" but not containing "[fine]"

  Scenario: logging from commandline, running in debug mode
    Given the following additional comma separated commandline arguments:
      """
      -loglevelfile, info, -loglevelconsole, warning, -logdestination, C:\CrossGenerate\Test\Log\testlog.log, -debug, true
      """
    When I run the generator
    Then I expect 1 generation result
    And a log file containing "[com.xbreeze.xgenerate." but not containing "[fine]"
