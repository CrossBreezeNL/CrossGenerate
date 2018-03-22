@Unit
Feature: Unit_TextTemplate_Section_Suffix
  In this feature we will describe the section suffix feature in text templates.
  
  @KnownIssue
  Scenario: Section with suffix on firstOnly
    # KnownIssue: The suffix is appeneded to the end of a section, but if there are new lines at the end of a section it probably should but the suffix before the new lines.
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" suffix="/* only on first */" suffixStyle="firstOnly")
        attribute_name attribute_fulldatatype,
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
              <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
            </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
      """
      CREATE TABLE Order AS (
        Id int,/* only on first */
        OrderDate datetime,
        OrderNumber varchar(50),
        CustomerId int,
        TotalAmount decimal(12,2),
        Id int,
        FirstName varchar(50),
        LastName varchar(100),
        City varchar(50),
        Country varchar(3),
        Phone varchar(20),
        recordCreatedOn datetime2(3)
      );
      GO;
      CREATE TABLE Customer AS (
        Id int,/* only on first */
        OrderDate datetime,
        OrderNumber varchar(50),
        CustomerId int,
        TotalAmount decimal(12,2),
        Id int,
        FirstName varchar(50),
        LastName varchar(100),
        City varchar(50),
        Country varchar(3),
        Phone varchar(20),
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """

  Scenario: Section wih suffix on lastOnly
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" end="," suffix="/* only on last */" suffixStyle="lastOnly")
        attribute_name attribute_fulldatatype,
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
              <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
            </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
      """
      CREATE TABLE Order AS (
        Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20),/* only on last */
        recordCreatedOn datetime2(3)
      );
      GO;
      CREATE TABLE Customer AS (
        Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20),/* only on last */
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """

  Scenario: Section with suffix on allButFirst
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" end="," suffix="/*not on first */" suffixStyle="allButFirst")
        attribute_name attribute_fulldatatype,
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
              <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
            </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
      """
      CREATE TABLE Order AS (
        Id int,  OrderDate datetime,/*not on first */  OrderNumber varchar(50),/*not on first */  CustomerId int,/*not on first */  TotalAmount decimal(12,2),/*not on first */  Id int,/*not on first */  FirstName varchar(50),/*not on first */  LastName varchar(100),/*not on first */  City varchar(50),/*not on first */  Country varchar(3),/*not on first */  Phone varchar(20),/*not on first */
        recordCreatedOn datetime2(3)
      );
      GO;
      CREATE TABLE Customer AS (
        Id int,  OrderDate datetime,/*not on first */  OrderNumber varchar(50),/*not on first */  CustomerId int,/*not on first */  TotalAmount decimal(12,2),/*not on first */  Id int,/*not on first */  FirstName varchar(50),/*not on first */  LastName varchar(100),/*not on first */  City varchar(50),/*not on first */  Country varchar(3),/*not on first */  Phone varchar(20),/*not on first */
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """

  Scenario: Section with suffix on allButLast
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" end="fulldatatype" suffix="," suffixStyle="allButLast")
        attribute_name attribute_fulldatatype
        ,recordCreatedOn datetime2(3)
      );
      GO;
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model/>
        <Template rootSectionName="System">
          <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
          <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
            <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
              <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
            </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
      """
      CREATE TABLE Order AS (
        Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20)
        ,recordCreatedOn datetime2(3)
      );
      GO;
      CREATE TABLE Customer AS (
        Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20)
        ,recordCreatedOn datetime2(3)
      );
      GO;
      
      """