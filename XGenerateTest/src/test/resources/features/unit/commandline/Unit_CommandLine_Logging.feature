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
         <ConfigFolder>C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Config\</ConfigFolder>
         <ModelFolder>C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Model\</ModelFolder>
         <OutputFolder>C:\CrossGenerate\Test\Output\</OutputFolder>
         <TemplateFolder>C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Template\</TemplateFolder>
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
    And the log destination directory "C:\CrossGenerate\Test\Log" is empty.

  Scenario: No logging from commandline
    When I run the generator
    Then I expect 1 generation result
    And no log file

  Scenario: logging from commandline, different parameter combinations
    Given the following additional comma separated commandline arguments:
      """
      -fll, super, -filelogdestination, C:\CrossGenerate\Test\Log\testlog.log
      """
    When I run the generator
    Then I expect 0 generation result
    And no log file

  Scenario Outline: logging from commandline, scenario <Scenario>
    Given the following additional comma separated commandline arguments:
      """
      -Debug, true, -<ConsoleLogLevel>, <ConsoleLogLevelValue>, -<FileLogLevelParam>, <FileLogLevelParamValue>, -<FileLogDestinationParam>, <FileLogdestinationValue>
      """
    When I run the generator
    Then I expect 1 generation result
    And a log file containing "<ResultContains>" but not containing "<ResultNotContains>"

    Examples: 
      | Scenario                                     | FileLogLevelParam | FileLogLevelParamValue | ConsoleLogLevel | ConsoleLogLevelValue | FileLogDestinationParam | FileLogdestinationValue                   | ResultContains | ResultNotContains |
      | fullnames proper case, both warning          | fileLogLevel      | warning                | consoleLogLevel | warning              | fileLogDestination      | C:\\CrossGenerate\\Test\\Log\\testlog.log | [WARNING]      | [INFO   ]         |
      | fullnames lower case, both warning           | fileloglevel      | warning                | consoleloglevel | warning              | filelogdestination      | C:\\CrossGenerate\\Test\\Log\\testlog.log | [WARNING]      | [INFO   ]         |
      | shortnames, info to file, warning to console | fll               | info                   | cll             | warning              | fld                     | C:\\CrossGenerate\\Test\\Log\\testlog.log | [INFO   ]      | [FINE   ]         |
