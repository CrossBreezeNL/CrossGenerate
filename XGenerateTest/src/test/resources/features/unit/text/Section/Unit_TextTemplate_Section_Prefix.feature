@Unit
Feature: Unit_TextTemplate_Section_Prefix
  In this feature we will describe the section prefix feature in text templates.

  Scenario: Section with prefix on firstOnly
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS 
        -- @XGenSection(name="Attribute" prefix="(" prefixStyle="firstOnly")
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
      CREATE TABLE Order AS 
      (  Id int,
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
      CREATE TABLE Customer AS 
      (  Id int,
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

  Scenario: Section wih prefix on lastOnly
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" prefix="/* last */" prefixStyle="lastOnly")
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
        Id int,
        OrderDate datetime,
        OrderNumber varchar(50),
        CustomerId int,
        TotalAmount decimal(12,2),
        Id int,
        FirstName varchar(50),
        LastName varchar(100),
        City varchar(50),
        Country varchar(3),
      /* last */  Phone varchar(20),
        recordCreatedOn datetime2(3)
      );
      GO;
      CREATE TABLE Customer AS (
        Id int,
        OrderDate datetime,
        OrderNumber varchar(50),
        CustomerId int,
        TotalAmount decimal(12,2),
        Id int,
        FirstName varchar(50),
        LastName varchar(100),
        City varchar(50),
        Country varchar(3),
      /* last */  Phone varchar(20),
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """

  Scenario: Section with prefix on allButFirst
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" prefix="," prefixStyle="allButFirst")
        attribute_name attribute_fulldatatype
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
        Id int
      ,  OrderDate datetime
      ,  OrderNumber varchar(50)
      ,  CustomerId int
      ,  TotalAmount decimal(12,2)
      ,  Id int
      ,  FirstName varchar(50)
      ,  LastName varchar(100)
      ,  City varchar(50)
      ,  Country varchar(3)
      ,  Phone varchar(20)
      );
      GO;
      CREATE TABLE Customer AS (
        Id int
      ,  OrderDate datetime
      ,  OrderNumber varchar(50)
      ,  CustomerId int
      ,  TotalAmount decimal(12,2)
      ,  Id int
      ,  FirstName varchar(50)
      ,  LastName varchar(100)
      ,  City varchar(50)
      ,  Country varchar(3)
      ,  Phone varchar(20)
      );
      GO;
      
      """

  Scenario: Section with prefix on allButLast
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" prefix="/* not last */" prefixStyle="allButLast")
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
      /* not last */  Id int,
      /* not last */  OrderDate datetime,
      /* not last */  OrderNumber varchar(50),
      /* not last */  CustomerId int,
      /* not last */  TotalAmount decimal(12,2),
      /* not last */  Id int,
      /* not last */  FirstName varchar(50),
      /* not last */  LastName varchar(100),
      /* not last */  City varchar(50),
      /* not last */  Country varchar(3),
        Phone varchar(20),
        recordCreatedOn datetime2(3)
      );
      GO;
      CREATE TABLE Customer AS (
      /* not last */  Id int,
      /* not last */  OrderDate datetime,
      /* not last */  OrderNumber varchar(50),
      /* not last */  CustomerId int,
      /* not last */  TotalAmount decimal(12,2),
      /* not last */  Id int,
      /* not last */  FirstName varchar(50),
      /* not last */  LastName varchar(100),
      /* not last */  City varchar(50),
      /* not last */  Country varchar(3),
        Phone varchar(20),
        recordCreatedOn datetime2(3)
      );
      GO;
      
      """
