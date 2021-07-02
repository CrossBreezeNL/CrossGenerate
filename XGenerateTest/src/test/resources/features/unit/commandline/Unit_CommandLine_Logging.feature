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
         <ConfigFolder>C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Config\</ConfigFolder>
         <ModelFolder>C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Model\</ModelFolder>
         <OutputFolder>C:\CrossGenerate\Test\Output\</OutputFolder>
         <TemplateFolder>C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Template\</TemplateFolder>
       </App>
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

  Scenario Outline: file logging from commandline, scenario <Scenario>
    Given the following additional comma separated commandline arguments:
      """
      -Debug, true, -<FileLogLevelParam>, <FileLogLevelParamValue>, -<FileLogDestinationParam>, C:\\CrossGenerate\\Test\\Log\\testlog.log
      """
    When I run the generator
    Then I expect 1 generation result
    And a log file containing "<ResultContains>" but not containing "<ResultNotContains>"

    Examples: 
      | Scenario                                      | FileLogLevelParam | FileLogLevelParamValue | FileLogDestinationParam | ResultContains | ResultNotContains |
      | all fullnames proper case  warning            | fileLogLevel      | warning                | fileLogDestination      | [WARNING]      | [INFO   ]         |
      | all fullnames lower case,  warning            | fileloglevel      | warning                | filelogdestination      | [WARNING]      | [INFO   ]         |
      | loglevel shortname, lower case, warning       | fll               | warning                | filelogdestination      | [WARNING]      | [INFO   ]         |
      | loglevel shortname, upper case, warning       | FLL               | warning                | filelogdestination      | [WARNING]      | [INFO   ]         |
      | logdestination shortname, lower case, warning | fll               | warning                | fld                     | [WARNING]      | [INFO   ]         |
      | logdestinaion shortname, upper case, warning  | fll               | warning                | FLD                     | [WARNING]      | [INFO   ]         |
      | Log level is info                             | fll               | info                   | fld                     | [INFO   ]      | [FINE   ]         |
      | log level is fine                             | fll               | fine                   | fld                     | [FINE   ]      | [(nothing)  ]     |

  Scenario Outline: console logging from commandline, scenario <Scenario>
    Given the following additional comma separated commandline arguments:
      """
      -Debug, true, -<ConsoleLogLevelParam>, <ConsoleLogLevelParamValue>
      """
    When I run the generator
    Then I expect 1 generation result
    And a console output containing "<ResultContains>" but not containing "<ResultNotContains>"

    Examples: 
      | Scenario                      | ConsoleLogLevelParam | ConsoleLogLevelParamValue | ResultContains | ResultNotContains |
      | Fullname proper case  warning | consoleLogLevel      | warning                   | [WARNING]      | [INFO   ]         |
      | Fullname lowercase  warning   | consoleloglevel      | warning                   | [WARNING]      | [INFO   ]         |
      | Shortname lowercase  warning  | cll                  | warning                   | [WARNING]      | [INFO   ]         |
      | Shortname uppercase warning   | CLL                  | warning                   | [WARNING]      | [INFO   ]         |
      | log level is info             | CLL                  | info                      | [INFO   ]      | [FINE   ]         |
      | log level is fine             | CLL                  | fine                      | [FINE   ]      | [(nothing)   ]    |
