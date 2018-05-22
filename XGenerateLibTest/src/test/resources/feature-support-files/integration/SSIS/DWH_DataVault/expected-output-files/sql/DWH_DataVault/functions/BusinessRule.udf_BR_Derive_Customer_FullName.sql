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
