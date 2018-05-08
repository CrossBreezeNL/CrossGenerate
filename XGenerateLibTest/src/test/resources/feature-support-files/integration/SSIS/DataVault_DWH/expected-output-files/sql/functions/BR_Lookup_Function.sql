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
      [Reference_Country.Country_Code] AS [Country_Code]
      [Reference_Country.Country_Description] AS [Country_Description]
      [Reference_Country.Country_Continent] AS [Country_Continent]
    FROM [Reference].[Country] AS [Reference_Country]
  )
  SELECT
    [Country_Description],
    [Country_Continent]
  FROM [Source_CTE]
  WHERE
        [Country_Code] = @Country_Code
;
GO
