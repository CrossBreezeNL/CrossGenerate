-- @XGenTextSection(name="LookupRule" literalOnLastLine="GO")
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
      [MappedParameter_expression] AS [MappedParameter_targetAttributeName],
      -- @XGenTextSection(name="MappedAttribute" suffix=",")
      [MappedAttribute_expression] AS [MappedAttribute_targetAttributeName]
    -- @XGenTextSection(name="MappedObject")
    FROM [MappedObject_mappedObjectSystemName].[MappedObject_mappedObjectName] AS [MappedObject_expression]
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
