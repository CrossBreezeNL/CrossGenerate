@Unit
Feature: Unit_Config_TextTemplate_Section_Bounds
  In this feature we will describe the section bounds feature for text templates specified in config.

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
    And the following template named "DropTables.sql":
      """
      -- Begin of template
      DROP TABLE entity_name;
      GO;
      -- End of template
      """

  Scenario Outline: Section with begin and end character sequence, <Scenario>
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Entity" begin="DROP TABLE " includeBegin="<includeBegin>" end="GO;" includeEnd="<includeEnd>"/>
          </TextSections>
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
      """
      -- Begin of template
      <expectedLine1>
      <expectedLine2>
      <expectedLine3>
      -- End of template
      """

    Examples: 
      | Scenario             | includeBegin | includeEnd | expectedLine1     | expectedLine2           | expectedLine3 |
      | including both       | true         | true       | DROP TABLE Order; | GO;DROP TABLE Customer; | GO;           |
      | including begin only | true         | false      | DROP TABLE Order; | DROP TABLE Customer;    | GO;           |
      | including end only   | false        | true       | DROP TABLE Order; | GO;Customer;            | GO;           |
      | excluding both       | false        | false      | DROP TABLE Order; | Customer;               | GO;           |

  Scenario: Section with equal begin and end character sequence
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Entity" begin=" Entity_name=&quot;" end="&quot;" />
          </TextSections>
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Order" modelXPath="mappableObjects/entity[@name='Order']" />
            <SectionModelBinding section="Customer" modelXPath="mappableObjects/entity[@name='Customer']" />
            <SectionModelBinding section="Entity" modelXPath="mappableObjects/entity" />
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    And the following template named "TwoEqualSections.sql":
      """
      -- Begin of template
       Entity_name="..."
      -- End of template
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "TwoEqualSections.sql" with content:
      """
      -- Begin of template
       Order="..." Customer="..."
      -- End of template
      """

  Scenario: Section with begin character sequence and nr of lines
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Entity" begin="DROP TABLE" includeBegin="true" nrOfLines="2"/>
          </TextSections>
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
      """
      -- Begin of template
      DROP TABLE Order;
      GO;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Section with begin character sequence only (and implicit nrOfLines = 1)
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Entity" begin="DROP TABLE" includeBegin="true"/>
          </TextSections>
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
      """
      -- Begin of template
      DROP TABLE Order;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Section with begin character sequence and literalOnLastLine
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Entity" begin="DROP TABLE" includeBegin="true" literalOnLastLine="GO;"/>
          </TextSections>
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
      """
      -- Begin of template
      DROP TABLE Order;
      GO;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Section with literalOnFirstLine and end character sequence
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Entity" literalOnFirstLine="TABLE" end="GO;" includeEnd="true" />
          </TextSections>
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
      """
      -- Begin of template
      DROP TABLE Order;
      GO;DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Section with literalOnFirstLine and nrOfLines
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <TextSections>
            <TextSection name="Entity" literalOnFirstLine="TABLE" nrOfLines="2" />
          </TextSections>
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
      """
      -- Begin of template
      DROP TABLE Order;
      GO;
      DROP TABLE Customer;
      GO;
      -- End of template
      """
