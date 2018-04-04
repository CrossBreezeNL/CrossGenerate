@Unit
Feature: Unit_TextTemplateFromConfig_Section_Bounds
  In this feature we will describe the section bounds feature for text templates specified in config.

  Background:
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
      """
      -- Begin of template
      DROP TABLE entity_name;
      GO;
      -- End of template
      """
     
  Scenario: Section with begin and end character sequence, including both
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" begin="DROP TABLE" includeBegin="true" end="GO;" includeEnd="true"/>
          </Sections>
        </Template>
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
 
  Scenario: Section with begin and end character sequence, including begin only
    
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" begin="DROP TABLE" includeBegin="true" end="GO;" includeEnd="false"/>
          </Sections>
        </Template>
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
      
  
  Scenario: Section with begin and end character sequence, including end only
   
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" begin="DROP TABLE" includeBegin="false" end="GO;" includeEnd="true"/>
          </Sections>
        </Template>
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
      GO; Customer;
      GO;
      -- End of template
      """
      
  Scenario: Section with begin and end character sequence, excluding both
  
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" begin="DROP TABLE" includeBegin="false" end="GO;" includeEnd="false"/>
          </Sections>
        </Template>
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
       Customer;
      GO;
      -- End of template
      """         
     
  Scenario: Section with begin character sequence and nr of lines
   
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" begin="DROP TABLE" includeBegin="true" nrOfLines="2"/>
          </Sections>
        </Template>
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
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" begin="DROP TABLE" includeBegin="true"/>
          </Sections>
        </Template>
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
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" begin="DROP TABLE" includeBegin="true" literalOnLastLine="GO;"/>
          </Sections>
        </Template>
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
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" literalOnFirstLine="TABLE" end="GO;" includeEnd="true" />
          </Sections>
        </Template>
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
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
          <Sections>
            <Section name="Entity" literalOnFirstLine="TABLE" nrOfLines="2" />
          </Sections>
        </Template>
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