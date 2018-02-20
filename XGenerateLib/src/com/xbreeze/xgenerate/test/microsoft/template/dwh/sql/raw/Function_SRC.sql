-- @XGenComment The Historical Source function should return the new actual state of the target table.
-- @XGenRepeat section='Entities' begin='CREATE' end='GO'
CREATE FUNCTION [Raw].[udf_SRC_System_name_Entity_name] ()
RETURNS TABLE
AS
RETURN
  SELECT
       [KeyAttribute_name_1]
      ,[KeyAttribute_name_2N]
      ,[NonKeyAttribute_name_1]
      ,[NonKeyAttribute_name_2N]
      ,[StageDateTime]
  FROM [Staging].[System_name].[Entity_name];
GO