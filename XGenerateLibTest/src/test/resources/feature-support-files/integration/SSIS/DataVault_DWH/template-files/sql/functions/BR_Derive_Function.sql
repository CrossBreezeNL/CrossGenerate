-- @XGenTextSection(name="DeriveRule" literalOnLastLine="GO")
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
