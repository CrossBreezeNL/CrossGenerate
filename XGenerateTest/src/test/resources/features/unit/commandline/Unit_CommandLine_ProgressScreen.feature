@Unit
Feature: Unit_CommandLine_ProgressScreen
  In this feature we'll test the progress screen feature.

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
    # In the below template we have an unbounded section to make sure there is a warning in the console/log output.
    And the following template named "Unit_Config_Template_OutputType_table_name.txt":
      """
      table_name
      -- @XGenTextSection(name='UnboundedSection')
      Unbounded section contents.
      """
    And the following config:
      """
         <XGenConfig>
          <Model/>
          <TextTemplate rootSectionName="Template">
          	<FileFormat singleLineCommentPrefix="--" />
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
         <ConfigFolder>C:\git\GitHub\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Config\</ConfigFolder>
         <ModelFolder>C:\git\GitHub\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Model\</ModelFolder>
         <OutputFolder>C:\CrossGenerate\Test\Output\</OutputFolder>
         <TemplateFolder>C:\git\GitHub\CrossGenerate\XGenerateTest\src\test\resources\feature-support-files\Template\</TemplateFolder>
       </App>
      </XGenAppConfig>
      """
    And the directory "C:\CrossGenerate\Test\Log" is empty.

  Scenario: Generate with progress screen
    Given the following additional comma separated commandline arguments:
      """
      -ps, true
      """
    When I run the generator
    Then I expect 1 generation result
    And no log file
    And I expect exit code 0
