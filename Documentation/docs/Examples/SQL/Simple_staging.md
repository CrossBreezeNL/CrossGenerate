# SQL - Simple Staging example

## Model
In this example we will be generating staging tables for each entity defined in the [Source model](../Model/Source_model).

## Template
We create a template for creating a staging table, in the file name we use the 'system_name' placeholder to have the system name in the resulting output file.
We define the `@XGenSection` annotation to define the 'CreateTable' section to repeat for each table.
We also define the section named 'TableColumn' to repeat for each column in the table.

### Staging_Tables_system_name.sql

``` sql
-- @XGenSection(name="CreateTable" literalOnLastLine="GO")
CREATE TABLE [system_name].[entity_name] (
  -- @XGenSection(name="TableColumn")
  [attribute_name]           attribute_fulldatatype        NULL,
  [StageDateTime]            datetime2(2)              NOT NULL  
);
GO
```

### Documentation
For documentation on templates, please see [Template](../../Template).

## Config
In the config we:

- set the `FileFormat` templateType to 'text' and set the comment and annotation format.
- add a `SectionModelBinding` element to bind the 'CreateTable' section _(defined in the template)_ to each 'entity' element in the model.
- within the 'CreateTable' section binding add a `SectionModelBinding` to bind the 'TableColumn' section _(defined in the template)_ to each 'attribute' element in the model.

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model/>
  <Template rootSectionName="System">
    <FileFormat templateType="text" currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="single_output" />
  </Template>
  <Binding>
    <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
    <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system">
    	<!-- Bind the 'CreateTable' template section on the mappableObjects/entity elements in the model. -->
	    <SectionModelBinding section="CreateTable" modelXPath="mappableObjects/entity" placeholderName="entity">
	      <Placeholders>
	        <!-- For the placeholder the modelXPath is relative to its section model XPath. -->
	        <Placeholder name="system" modelXPath="../.." />
	      </Placeholders>
	      <!-- Bind the 'TableColumn' template section on the attributes/attribute elements in the model. -->
	      <SectionModelBinding section="TableColumn" modelXPath="attributes/attribute" placeholderName="attribute" />
	    </SectionModelBinding>
    </SectionModelBinding>
  </Binding>
</XGenConfig>
```

### Documentation
For documentation on the configuration, please see [Config](../../Config).

## Output
When running CrossGenerate the output using the given Model, Template and Config will be as follows:

### Staging_Tables_ExampleSource.sql
``` sql
CREATE TABLE [ExampleSource].[Order] (
  [Id]           int        NULL,
  [OrderDate]           datetime        NULL,
  [OrderNumber]           varchar(50)        NULL,
  [CustomerId]           int        NULL,
  [TotalAmount]           decimal(12,2)        NULL,
  [StageDateTime]            datetime2(2)              NOT NULL  
);
GO
CREATE TABLE [ExampleSource].[Customer] (
  [Id]           int        NULL,
  [FirstName]           varchar(50)        NULL,
  [LastName]           varchar(100)        NULL,
  [City]           varchar(50)        NULL,
  [Country]           varchar(3)        NULL,
  [Phone]           varchar(20)        NULL,
  [StageDateTime]            datetime2(2)              NOT NULL  
);
GO
```

## Attachments
The sample solution with the template database scripts can be found in the following zip file:

- [CrossGenerate_Example_SQL_Simple_staging.zip](CrossGenerate_Example_SQL_Simple_staging.zip)