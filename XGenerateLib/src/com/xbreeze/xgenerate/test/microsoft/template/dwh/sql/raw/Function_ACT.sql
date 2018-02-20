-- @XGenComment Create a function for every Entity which is marked for generation.
-- @XGenRepeat section='Entities' begin='CREATE' end='GO'
CREATE FUNCTION [System_name].[udf_ACT_Entity_name] ()
RETURNS TABLE
AS
RETURN
  SELECT
       [KeyAttribute_name_1]
      ,[KeyAttribute_name_2N]
      ,[NonKeyAttribute_name_1]
      ,[NonKeyAttribute_name_2N]
  FROM [Raw].[System_name_Entity_name]
  WHERE [EndDateTime] = '9999-12-31'
GO