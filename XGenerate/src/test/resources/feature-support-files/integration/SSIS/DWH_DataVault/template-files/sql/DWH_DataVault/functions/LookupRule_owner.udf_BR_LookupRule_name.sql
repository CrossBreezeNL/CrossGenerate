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
