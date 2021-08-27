# SQL - DataVault example

In this example we create a DataVault structure. If you haven't done already, please read the [Simple staging example](../Simple_staging) first for a basic explanation of concepts used in the template and config. The tables implemented in this example are used in the [SSIS DataVault example](../../Microsoft_SSIS/DataVault).

## Model
In this example we will be generating DataVault tables for each entity and relation defined in the [DWH model](../../Model/DWH_model).

## Templates
For generating DataVault tables we need multiple templates, namely a SQL template file for each table type (Hub, Link, Hub-Sat and Link-Sat) since each table type is a different technical pattern. 

### Entity_owner.H_Entity_name.sql
In the Hub template a section KeyAttribute is defined and used for column specification as well as for the unique constraint.

``` sql
CREATE TABLE [Entity_owner].[H_Entity_name] (
  [H_Entity_name_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  -- @XGenTextSection(name="KeyAttribute")
  [KeyAttribute_name]        KeyAttribute_fulldatatype    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_Entity_owner_H_Entity_name] PRIMARY KEY NONCLUSTERED ([H_Entity_name_SQN]),
  CONSTRAINT [UK_Entity_owner_H_Entity_name] UNIQUE(
    -- @XGenTextSection(name="KeyAttribute" suffix=",")
    [KeyAttribute_name]
  )
);
GO
```

### Entity_owner.HS_Entity_name.sql
The Hub-Sat template has a section defined named NonKeyAttribute that is used to apply the non-key columns to the sattelite table.

```sql
CREATE TABLE [Entity_owner].[HS_Entity_name] (
  [H_Entity_name_SQN]        BIGINT NOT NULL,
  -- @XGenTextSection(name="NonKeyAttribute") 
  [NonKeyAttribute_name]     NonKeyAttribute_fulldatatype    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_Entity_owner_HS_Entity_name] PRIMARY KEY NONCLUSTERED ([H_Entity_name_SQN], [LoadDateTime]),
  CONSTRAINT [FK_Entity_owner_HS_Entity_name_Entity_owner_H_Entity_name] FOREIGN KEY ([H_Entity_name_SQN]) REFERENCES [Entity_owner].[H_Entity_name] ([H_Entity_name_SQN])
);
GO
```

### Relation_owner.L_Relation_name.sql
The Link table template has a section RelatedEntity that is used to define the appropriate technical Hub keys as columns to the table as well as to the unique constraint.

``` sql
CREATE TABLE [Relation_owner].[L_Relation_name] (
  [L_Relation_name_SQN]      BIGINT NOT NULL IDENTITY(1,1),
  -- @XGenTextSection(name="RelatedEntity") 
  [H_RelatedEntity_parentReferenceName_SQN] BIGINT    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_Relation_owner_H_Relation_name] PRIMARY KEY NONCLUSTERED ([L_Relation_name_SQN]),
  CONSTRAINT [UK_Relation_owner_H_Relation_name] UNIQUE(
    -- @XGenTextSection(name="RelatedEntity" suffix=",")
    [H_RelatedEntity_parentReferenceName_SQN]
  )
);
GO
```

### Relation_owner.LS_Relation_name.sql
Similar to the Hub-Sat table, the Link-Sat template also has a section named NonKeyAttribute.
```sql
CREATE TABLE [Relation_owner].[LS_Relation_name] (
  [L_Relation_name_SQN]      BIGINT NOT NULL,
  -- @XGenTextSection(name="NonKeyAttribute") 
  [NonKeyAttribute_name]     NonKeyAttribute_fulldatatype    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_Relation_owner_LS_Relation_name] PRIMARY KEY NONCLUSTERED ([L_Relation_name_SQN], [LoadDateTime]),
  CONSTRAINT [FK_Relation_owner_LS_Relation_name_Relation_owner_L_Relation_name] FOREIGN KEY ([L_Relation_name_SQN]) REFERENCES [Relation_owner].[L_Relation_name] ([L_Relation_name_SQN])
);
GO
```

### Documentation
For documentation on templates, please see [Template](../../../Template).

## Config
To generate the DataVault structure we need two configurations, one for the Hub and Hub-Sat tables that binds on entity and one for the Link and Link-Sat tables that binds on relation.

### Config for Hub and Hub-Sat tables
The config is very similar to the one used to generate the staging table, however note that in this config multiple `SectionModelBinding` entries are added to bind `NonKeyAttribute` to attributes that have not been marked as primary and `KeyAttribute` to the attributes that are marked as primary. In other words, SectionModelBinding is used to route parts of the model to the appropriate sections in a template. In this case this enables creating a DataVault structure from a relational model (the model itself not being in DataVault structure).

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model/>
  <TextTemplate rootSectionName="Entity">
    <FileFormat currentAccessor="_" childAccessor="$" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="output_per_element" />
  </TextTemplate>
  <Binding>
		<SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="Entity">
			<Placeholders>
				<Placeholder name="System" modelXPath="../.." />
			</Placeholders>
			<SectionModelBinding section="Attribute" modelXPath="attributes/attribute" placeholderName="Attribute" />
			<SectionModelBinding section="NonKeyAttribute" modelXPath="attributes/attribute[not(boolean(@primary))]" placeholderName="NonKeyAttribute" />
			<SectionModelBinding section="KeyAttribute" modelXPath="attributes/attribute[boolean(@primary)]" placeholderName="KeyAttribute" />
		</SectionModelBinding>
  </Binding>
</XGenConfig>
```

### Config for Link and Link-Sat tables
The config for Link and Link-Sat is similar to the config for Hub and Hub-Sat tables. The differences are that in this config, the sections are bound to relation instead of entity and an additional `SectionModelBinding` is defined for binding `RelatedEntity` to the references within a relation.
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model/>
  <TextTemplate rootSectionName="Relation">
    <FileFormat currentAccessor="_" childAccessor="$" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="output_per_element" />
  </TextTemplate>
  <Binding>
		<SectionModelBinding section="Relation" modelXPath="/modeldefinition/system/mappableObjects/relation" placeholderName="Relation">
			<Placeholders>
				<Placeholder name="System" modelXPath="../.." />
			</Placeholders>
			<SectionModelBinding section="Attribute" modelXPath="attributes/attribute" placeholderName="Attribute" />
			<SectionModelBinding section="NonKeyAttribute" modelXPath="attributes/attribute[not(boolean(@primary))]" placeholderName="NonKeyAttribute" />
			<SectionModelBinding section="KeyAttribute" modelXPath="attributes/attribute[boolean(@primary)]" placeholderName="KeyAttribute" />
			<SectionModelBinding section="RelatedEntity" modelXPath="references/reference" placeholderName="RelatedEntity" />
		</SectionModelBinding>
  </Binding>
</XGenConfig>
```


### Documentation
For documentation on the configuration, please see [Config](../../../Config).

## Output
When running CrossGenerate the output using the given Model, Templates and Configs will be as follows:

### Hub tables
The Hub tables generated using the Hub template and Hub/Hub-Sat config.

#### BusinessVault.H_Country.sql
``` sql
CREATE TABLE [BusinessVault].[H_Country] (
  [H_Country_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [Code]        nvarchar(3)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Country] PRIMARY KEY NONCLUSTERED ([H_Country_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Country] UNIQUE(
    [Code]
  )
);
GO
```
#### BusinessVault.H_Customer.sql
``` sql
CREATE TABLE [BusinessVault].[H_Customer] (
  [H_Customer_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [Id]        int    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Customer] PRIMARY KEY NONCLUSTERED ([H_Customer_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Customer] UNIQUE(
    [Id]
  )
);
GO
```
#### BusinessVault.H_Order.sql
``` sql
CREATE TABLE [BusinessVault].[H_Order] (
  [H_Order_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [Id]        int    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Order] PRIMARY KEY NONCLUSTERED ([H_Order_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Order] UNIQUE(
    [Id]
  )
);
GO
```

### Hub-Sat tables
Generated using the Hub-Sat template and Hub/Hub-Sat config.

#### BusinessVault.HS_Country.sql
``` sql
CREATE TABLE [BusinessVault].[HS_Country] (
  [H_Country_SQN]        BIGINT NOT NULL,
  [Description]     nvarchar(100)    NULL,
  [Continent]     nvarchar(100)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_BusinessVault_HS_Country] PRIMARY KEY NONCLUSTERED ([H_Country_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Country_BusinessVault_H_Country] FOREIGN KEY ([H_Country_SQN]) REFERENCES [BusinessVault].[H_Country] ([H_Country_SQN])
);
GO
```
#### BusinessVault.HS_Customer.sql
```sql
CREATE TABLE [BusinessVault].[HS_Customer] (
  [H_Customer_SQN]        BIGINT NOT NULL,
  [FirstName]     varchar(50)    NULL,
  [LastName]     varchar(100)    NULL,
  [City]     varchar(50)    NULL,
  [Country_Code]     varchar(3)    NULL,
  [Country_Description]     nvarchar(100)    NULL,
  [Country_Continent]     nvarchar(100)    NULL,
  [Phone]     varchar(20)    NULL,
  [FullName]     varchar(151)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_BusinessVault_HS_Customer] PRIMARY KEY NONCLUSTERED ([H_Customer_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Customer_BusinessVault_H_Customer] FOREIGN KEY ([H_Customer_SQN]) REFERENCES [BusinessVault].[H_Customer] ([H_Customer_SQN])
);
GO
```
#### BusinessVault.HS_Order.sql
```sql
CREATE TABLE [BusinessVault].[HS_Order] (
  [H_Order_SQN]        BIGINT NOT NULL,
  [OrderDate]     datetime    NULL,
  [OrderNumber]     varchar(50)    NULL,
  [CustomerId]     int    NULL,
  [TotalAmount]     decimal(12,2)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_BusinessVault_HS_Order] PRIMARY KEY NONCLUSTERED ([H_Order_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Order_BusinessVault_H_Order] FOREIGN KEY ([H_Order_SQN]) REFERENCES [BusinessVault].[H_Order] ([H_Order_SQN])
);
GO
```

### Link tables
Generated using the Link template and Link/Link-Sat config.

#### BusinessVault.L_Order_Customer.sql
```sql
CREATE TABLE [BusinessVault].[L_Order_Customer] (
  [L_Order_Customer_SQN]      BIGINT NOT NULL IDENTITY(1,1),
  [H_Customer_SQN] BIGINT    NULL,
  [H_Order_SQN] BIGINT    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Order_Customer] PRIMARY KEY NONCLUSTERED ([L_Order_Customer_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Order_Customer] UNIQUE(
    [H_Customer_SQN],
    [H_Order_SQN]
  )
);
GO
```

### Link-Sat tables
Generated using the Link-Sat template and the Link/Link-Sat config.

#### BusinessVault.LS_Order_Customer.sql
```sql
CREATE TABLE [BusinessVault].[LS_Order_Customer] (
  [L_Order_Customer_SQN]      BIGINT NOT NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_BusinessVault_LS_Order_Customer] PRIMARY KEY NONCLUSTERED ([L_Order_Customer_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_LS_Order_Customer_BusinessVault_L_Order_Customer] FOREIGN KEY ([L_Order_Customer_SQN]) REFERENCES [BusinessVault].[L_Order_Customer] ([L_Order_Customer_SQN])
);
GO
```

## Attachments
The entire DataVault example, including SQL and SSIS templates, can be found in the [SSIS DataVault example](../../Microsoft_SSIS/DataVault#attachments).