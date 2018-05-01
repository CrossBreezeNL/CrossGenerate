@Unit
Feature: Unit_Config_XmlTemplate_PlaceholderFormat
  In this feature we will describe the placeholder format feature for XML templates specified in config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource">
          <description>Some description</description>
        </system>
      </modeldefinition>
      """

  Scenario: Current accessor default
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database name="database_name"/>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <Output type="single_output" />
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Database name="ExampleSource"/>
      """

  Scenario Outline: Accessor <Scenario>
    Given the following template named "ExampleTemplate.xml":
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <Template>
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <XmlTemplate rootSectionName="Database">
          <FileFormat <AccessorName>Accessor="<AccessorChar>" />
          <Output type="single_output" />
        </XmlTemplate>
        <Binding>
          <SectionModelBinding section="Database" modelXPath="/modeldefinition/system" placeholderName="database" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "ExampleTemplate.xml" with content:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <ExpectedResult>
      """

    Examples: 
      | Scenario            | AccessorName | AccessorChar | Template                          | ExpectedResult                   |
      | Current simple      | current      | _            | <Database name="database_name"/>                   | <Database name="ExampleSource"/>           |
      | Current multichar   | current      | blaat        | <Database name="databaseblaatname"/>               | <Database name="ExampleSource"/>           |
      # Cucumber escapes XML entities by default, so when entering &gt; it is resolved before passing it into the generator. So we need to double encode it.
      # In an IDE this resembles the option to do 'placeholder->attribute'. When saving the XML file in the IDE to will be stored as 'placeholder-&gt;attribute'.
      | Current XML encoded | current      | -&amp;gt;    | <Database name="database->name"/> | <Database name="ExampleSource"/> |
      | Child simple        | child        | $            | <Database description="database$description"/>     | <Database description="Some description"/> |
      | Child multichar     | child        | blaat        | <Database description="databaseblaatdescription"/> | <Database description="Some description"/> |
