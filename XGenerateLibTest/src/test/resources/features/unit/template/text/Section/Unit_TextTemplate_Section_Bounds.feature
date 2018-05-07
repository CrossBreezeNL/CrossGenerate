@Unit
Feature: Unit_TextTemplate_Section_Bounds
  In this feature we will describe the section bounds feature in text templates.
  
  Scenario: implicit root section only
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource">
        </system>
      </modeldefinition>
      """
    And the following template named "CreateSchema.sql":
      """
      CREATE SCHEMA system_name;
      GO;
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "CreateSchema.sql" with content:
      """
      CREATE SCHEMA ExampleSource;
      GO;
      """

  Scenario: Single line section
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity")
      DROP TABLE entity_name;
      GO;
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      DROP TABLE Order;
      DROP TABLE Customer;
      GO;
      """

  Scenario: Multiline section with literalOnLastLine
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" literalOnLastLine="GO;")
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      DROP TABLE Order;
      GO;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Multiline section with nrOfLines
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" nrOfLines="2")
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      DROP TABLE Order;
      GO;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Multiline section with end character sequence, including end
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" end="GO;" includeEnd="true")
      
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      
      DROP TABLE Order;
      GO;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Multiline section with end character sequence, excluding end
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" end="GO;" includeEnd="false")
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      DROP TABLE Order;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Section with literalOnFirstLine
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" literalOnFirstLine="DROP")
      -- section starts on the next line
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      -- section starts on the next line
      DROP TABLE Order;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Section with begin character sequence, including begin
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" begin="DROP TABLE" includeBegin="true")
      -- section starts on the next line
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      -- section starts on the next line
      DROP TABLE Order;
      DROP TABLE Customer;
      GO;
      -- End of template
      """

  Scenario: Section with begin character sequence, excluding begin
    # Here the begin is not included, but the new line of the begin line will be included.
    # So therefore there will be an extra newline at the beginning of each section.
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" begin="-- begin drop statement" includeBegin="false" literalOnLastLine="GO")
      -- section starts on the next line
      -- begin drop statement
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
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
      -- section starts on the next line
      -- begin drop statement
      DROP TABLE Order;
      GO;
      
      DROP TABLE Customer;
      GO;
      -- End of template
      """
