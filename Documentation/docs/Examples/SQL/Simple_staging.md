# SQL - Simple Staging example

## Model
In this example we will be generating staging tables for each entity defined in the [Source model](../Model/Source_model).

## Template
We create a template for creating a staging table. We define the `@XGenSection` annotation to define the 'StagingTable' section to repeat. We also define the section named 'StagingColumn' to repeat.

``` sql
-- XGenSection(name="CreateTable" begin="CREATE TABLE" end="GO")
CREATE TABLE [System_name].[Entity_name] (
  -- @XGenSection(name="TableColumn")
  [Attribute_name]           Attribute_fulldatatype        NULL,
  [StageDateTime]            DATETIME2(2)              NOT NULL  
);
GO
```

## Config
In the config we set the `FileFormat` to SQL and add a `SectionModelBinding` element to bind the 'StagingTable' section _(defined in the template)_ to each 'entity' element in the model.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Template rootSectionName="CreateTable">
    <FileFormat templateType="text" currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
  </Template>
  <Binding>
    <SectionModelBinding section="CreateTable" modelXPath="system/mappableObjects/entity" placeholderName="Entity">
      <Placeholders>
        <!-- For the placeholder the modelXPath is relative to its section model xpath. -->
        <Placeholder name="System" modelXPath="../.." />
      </Placeholders>
      <SectionModelBinding section="TableColumn" modelXPath="attributes/attribute" placeholderName="Attribute" />
    </SectionModelBinding>
  </Binding>
</XGenConfig>
```

## Output
When running CrossGenerate the output using the given Model, Template and Config will be as follows:
``` sql
CREATE TABLE [ExampleSource].[Order]
(
  [Id] int null,
  [OrderDate] datetime null,
  [OrderNumber] varchar(50) null,
  [CustomerId] int null,
  [TotalAmount] decimal(12,2) null,
  [StageDateTime] datetime2(2) not null
);
GO

CREATE TABLE [ExampleSource].[Customer]
(
  [Id] int null,
  [FirstName] varchar(50) null,
  [LastName] varchar(100) null,
  [City] varchar(50) null,
  [Country] varchar(3) null,
  [Phone] varchar(20) null,
  [StageDateTime] datetime2(2) not null
);
GO
```