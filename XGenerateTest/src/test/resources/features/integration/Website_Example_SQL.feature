#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SQL
@Integration
Feature: Integration_Website_Example_SQL

  @KnownIssue
  Scenario: Re-create original code
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <system name="ExampleSource">
        <entity name="Order">
          <attribute name="Id" fulldatatype="int" />
          <attribute name="CustomerId" fulldatatype="int" />
          <attribute name="TotalAmount" fulldatatype="decimal(12,2)" />
        </entity>
        <entity name="Customer">
          <attribute name="Id" fulldatatype="int" />
          <attribute name="FirstName" fulldatatype="varchar(50)" />
          <attribute name="LastName" fulldatatype="varchar(100)" />
          <attribute name="City" fulldatatype="varchar(50)" />
        </entity>
      </system>
      """
    And the following template named "Staging_Tables_schema_name.sql":
      """
      CREATE SCHEMA [schema_name];
      GO
      -- @XGenSection(name="CreateTable" literalOnLastLine="GO")
      CREATE TABLE [schema_name].[table_name] (
        -- @XGenSection(name="TableColumn" suffix="," prefixStyle="allButLast")
        [column_name]            column_fulldatatype        NULL
      );
      GO
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="StagingScript">
          <FileFormat templateType="text" singleLineCommentPrefix="--" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="StagingScript" modelXPath="/system" placeholderName="schema">
              <SectionModelBinding section="CreateTable" modelXPath="entity" placeholderName="table">
                <Placeholders>
                  <Placeholder name="schema" modelXPath="../../system" />
                </Placeholders>
                <SectionModelBinding section="TableColumn" modelXPath="./attribute" placeholderName="column" />
              </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation results
    And an output named "Staging_Tables_ExampleSource.sql" with content:
      """
      CREATE SCHEMA [ExampleSource];
      GO
      CREATE TABLE [ExampleSource].[Order] (
        [Id]            int        NULL,
        [CustomerId]            int        NULL,
        [TotalAmount]            decimal(12,2)        NULL
      );
      GO
      CREATE TABLE [ExampleSource].[Customer] (
        [Id]            int        NULL,
        [FirstName]            varchar(50)        NULL,
        [LastName]            varchar(100)        NULL,
        [City]            varchar(50)        NULL
      );
      GO
      
      """

  Scenario: Template and model enriched
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <system name="ExampleSource">
        <entity name="Order">
          <attribute name="Id" fulldatatype="int" />
          <attribute name="OrderDate" fulldatatype="datetime" />
          <attribute name="OrderNumber" fulldatatype="varchar(50)" />
          <attribute name="CustomerId" fulldatatype="int" />
          <attribute name="TotalAmount" fulldatatype="decimal(12,2)" />
        </entity>
        <entity name="Customer">
          <attribute name="Id" fulldatatype="int" />
          <attribute name="FirstName" fulldatatype="varchar(50)" />
          <attribute name="LastName" fulldatatype="varchar(100)" />
          <attribute name="City" fulldatatype="varchar(50)" />
          <attribute name="Country" fulldatatype="varchar(3)" />
          <attribute name="Phone" fulldatatype="varchar(20)" />
        </entity>
      </system>
      """
    And the following template named "Staging_Tables_schema_name.sql":
      """
      CREATE SCHEMA [schema_name];
      GO
      -- @XGenSection(name="CreateTable" literalOnLastLine="GO")
      CREATE TABLE [schema_name].[table_name] (
        -- @XGenSection(name="TableColumn")
        [column_name]            column_fulldatatype        NULL,
        [StageDateTime]          datetime2(2)           NOT NULL
      );
      GO
      
      """
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Template rootSectionName="StagingScript">
          <FileFormat templateType="text" singleLineCommentPrefix="--" />
          <Output type="single_output" />
        </Template>
        <Binding>
          <SectionModelBinding section="StagingScript" modelXPath="/system" placeholderName="schema">
              <SectionModelBinding section="CreateTable" modelXPath="entity" placeholderName="table">
                <Placeholders>
                  <Placeholder name="schema" modelXPath="../../system" />
                </Placeholders>
                <SectionModelBinding section="TableColumn" modelXPath="./attribute" placeholderName="column" />
              </SectionModelBinding>
          </SectionModelBinding>
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation results
    And an output named "Staging_Tables_ExampleSource.sql" with content:
      """
      CREATE SCHEMA [ExampleSource];
      GO
      CREATE TABLE [ExampleSource].[Order] (
        [Id]            int        NULL,
        [OrderDate]            datetime        NULL,
        [OrderNumber]            varchar(50)        NULL,
        [CustomerId]            int        NULL,
        [TotalAmount]            decimal(12,2)        NULL,
        [StageDateTime]          datetime2(2)           NOT NULL
      );
      GO
      CREATE TABLE [ExampleSource].[Customer] (
        [Id]            int        NULL,
        [FirstName]            varchar(50)        NULL,
        [LastName]            varchar(100)        NULL,
        [City]            varchar(50)        NULL,
        [Country]            varchar(3)        NULL,
        [Phone]            varchar(20)        NULL,
        [StageDateTime]          datetime2(2)           NOT NULL
      );
      GO
      
      """
