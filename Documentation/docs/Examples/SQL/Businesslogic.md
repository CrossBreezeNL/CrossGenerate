# SQL - Business logic

In this example we create user defined functions implementing business logic. If you haven't done already, please read the [Simple staging example](./Simple_staging) first for a basic explanation of concepts used in the template and config. The functions implemented in this example are used in the [SSIS DataVault example](../Microsoft_SSIS/DataVault).

## Model
In this example we will be generating the business logic (business rules) defined in the [DWH model](../Model/DWH_model).

## Template
We create a template for each type of business rule. The example model contains two types of rules: a derived field and a lookup.

### DeriveRule_owner.udf_BR_DeriveRule_name.sql
The derived field template creates a function with the appropriate signature based on the parameters and attributes defined in the model. For the implementation the business rule child element `expression` is referenced. 

!!! note
      When a model attribute is accessed an underscore is used as accessor (for example Parameter_name) a child element is accessed using the dollar sign (for example DeriveRule$expression). The accessors can be configured in the config.

``` sql
-- @XGenComment(This is the function template for all derive rules in the DWH)
CREATE FUNCTION [DeriveRule_owner].[udf_BR_DeriveRule_name] (
  -- @XGenTextSection(name="Parameter" suffix=",")
  @Parameter_name Parameter_fulldatatype
)
RETURNS TABLE
RETURN
  SELECT
    -- @XGenTextSection(name="Attribute" suffix=",")
    [Rule].[Attribute_name]
  FROM DeriveRule$expression AS [Rule]
GO
```

### LookupRule_owner.udf_BR_LookupRule_name.sql
In the model a lookup business rule has a mapped source entity. With the information in the model an implementation can be generated that performs a lookup query on the mapped source entity.

```sql
-- @XGenComment(This is the function template for all lookup rules in the DWH)
CREATE FUNCTION [LookupRule_owner].[udf_BR_LookupRule_name] (
  -- @XGenTextSection(name="Parameter" suffix=",")
  @Parameter_name Parameter_fulldatatype
)
RETURNS TABLE
RETURN
  WITH [Source_CTE] (
    -- @XGenTextSection(name="Parameter")
    [Parameter_name],
    -- @XGenTextSection(name="Attribute" suffix=",")
    [Attribute_name]
  ) AS (
    SELECT
      -- @XGenTextSection(name="MappedParameter")
      MappedParameter_expression AS [MappedParameter_targetAttributeName],
      -- @XGenTextSection(name="MappedAttribute" suffix=",")
      MappedAttribute_expression AS [MappedAttribute_targetAttributeName]
    -- @XGenTextSection(name="MappableObjectMapping")
    FROM MappableObjectMapping_mappedObjectJoiner
  )
  SELECT
    -- @XGenTextSection(name="Attribute" suffix=",")
    [Attribute_name]
  FROM [Source_CTE]
  WHERE
    -- @XGenTextSection(name="Parameter" prefix="AND ")
    [Parameter_name] = @Parameter_name
;
GO
```

### Documentation
For documentation on templates, please see [Template](../../Template).

## Config
Each type of business rule requires it's specific configuration, for this example we therefore have two configs.

### Config for derived field
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model/>
  <TextTemplate rootSectionName="DeriveRule">
    <FileFormat currentAccessor="_" childAccessor="$" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="output_per_element" />
  </TextTemplate>
  <Binding>
		<SectionModelBinding section="DeriveRule" modelXPath="/modeldefinition/system/mappableObjects/businessRule[@stereotype='derive']" placeholderName="DeriveRule">
			<SectionModelBinding section="Parameter" modelXPath="attributes/parameter" placeholderName="Parameter" />
			<SectionModelBinding section="Attribute" modelXPath="attributes/attribute" placeholderName="Attribute" />
		</SectionModelBinding>
  </Binding>
</XGenConfig>
```
### Config for lookup rule
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model/>
  <TextTemplate rootSectionName="LookupRule">
    <FileFormat currentAccessor="_" childAccessor="$" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="output_per_element" />
  </TextTemplate>
  <Binding>
		<SectionModelBinding section="LookupRule" modelXPath="/modeldefinition/system/mappableObjects/businessRule[@stereotype='lookup']" placeholderName="LookupRule">
			<SectionModelBinding section="Parameter" modelXPath="attributes/parameter" placeholderName="Parameter" />
			<SectionModelBinding section="Attribute" modelXPath="attributes/attribute" placeholderName="Attribute" />
			<SectionModelBinding section="MappableObjectMapping" modelXPath="mappableObjectMappings/mappableObjectMapping" placeholderName="MappableObjectMapping" />
			<SectionModelBinding section="MappedParameter" modelXPath="mappableObjectMappings/mappableObjectMapping/attributeMappings/attributeMapping[@targetAttributeStereotype='parameter']" placeholderName="MappedParameter" />
			<SectionModelBinding section="MappedAttribute" modelXPath="mappableObjectMappings/mappableObjectMapping/attributeMappings/attributeMapping[@targetAttributeStereotype='attribute']" placeholderName="MappedAttribute" />
		</SectionModelBinding>
  </Binding>
</XGenConfig>
```

### Documentation
For documentation on the configuration, please see [Config](../../Config).

## Output
When running CrossGenerate the output using the given Model, Templates and Configs will be as follows:

### BusinessRule.udf_BR_Derive_Customer_FullName.sql
``` sql
CREATE FUNCTION [BusinessRule].[udf_BR_Derive_Customer_FullName] (
  @FirstName varchar(50),
  @LastName varchar(100)
)
RETURNS TABLE
RETURN
  SELECT
    [Rule].[FullName]
  FROM (SELECT @FirstName + ' ' + @LastName AS [FullName]) AS [Rule]
GO
```
### BusinessRule.udf_BR_Lookup_Country.sql
``` sql
CREATE FUNCTION [BusinessRule].[udf_BR_Lookup_Country] (
  @Country_Code varchar(3)
)
RETURNS TABLE
RETURN
  WITH [Source_CTE] (
    [Country_Code],
    [Country_Description],
    [Country_Continent]
  ) AS (
    SELECT
      Reference_Country.Country_Code AS [Country_Code],
      Reference_Country.Country_Description AS [Country_Description],
      Reference_Country.Country_Continent AS [Country_Continent]
    FROM [$(DWH_Staging_Reference)].[dbo].[Country] AS [Reference_Country]
  )
  SELECT
    [Country_Description],
    [Country_Continent]
  FROM [Source_CTE]
  WHERE
        [Country_Code] = @Country_Code
;
GO
```

## Attachments
The entire DataVault example, including SQL and SSIS templates, can be found in the [SSIS DataVault example](../Microsoft_SSIS/DataVault#attachments).