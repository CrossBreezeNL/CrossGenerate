@Unit
Feature: Unit_TextTemplate_Section_Bounds
  In this feature we will describe the section bounds feature in text templates.

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

  Scenario: implicit root section only
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
    Given the following template named "DropTables.sql":
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
    Given the following template named "DropTables.sql":
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
    Given the following template named "DropTables.sql":
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
    Given the following template named "DropTables.sql":
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
    Given the following template named "DropTables.sql":
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
    Given the following template named "DropTables.sql":
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
    Given the following template named "DropTables.sql":
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
    Given the following template named "DropTables.sql":
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

  Scenario: Section with multiple sections and annotations on one line
    Given the following template named "DropTables.sql":
      """
      -- @XGenTextSection(name="Entity" begin="DROP INDEX" includeBegin="true" end="DROP TABLE" includeEnd="false") @XGenTextSection(name="entity" begin='DROP TABLE' includeBegin="true" end='TABLE entity_name;' includeEnd="true")    
      DROP INDEX IDX_entity_name; DROP TABLE entity_name;
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
      --     
      DROP INDEX IDX_Order; DROP INDEX IDX_Customer; DROP TABLE Order;DROP TABLE Customer;
      """

  Scenario: Section with multi-line comment single-line annotation
    Given the following template named "DropTables.sql":
      """
      <!-- @XGenTextSection(name='Entity') -->
      DROP TABLE entity_name;
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" multiLineCommentPrefix="&lt;!--" multiLineCommentSuffix="--&gt;" />
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
      
      """
    
  Scenario: Section with multi-line comment multi-line annotation
    Given the following template named "DropTables.sql":
      """
      <!--
        Some comment
        @XGenTextSection(name='Entity' literalOnFirstLine='DROP TABLE')
       -->
      DROP TABLE entity_name;
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" multiLineCommentPrefix="&lt;!--" multiLineCommentSuffix="--&gt;" />
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
      <!--
        Some comment
       -->
      DROP TABLE Order;
      DROP TABLE Customer;
      
      """

  @Debug
  Scenario: Section inside multi-line comment with annotation
    Given the following template named "DropTables.sql":
      """
      <!--
        @XGenTextSection(name='Entity')
        entity_name;
       -->
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <TextTemplate rootSectionName="System">
          <FileFormat annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" multiLineCommentPrefix="&lt;!--" multiLineCommentSuffix="--&gt;" />
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
      <!--
        Order
        Customer
       -->
      """
