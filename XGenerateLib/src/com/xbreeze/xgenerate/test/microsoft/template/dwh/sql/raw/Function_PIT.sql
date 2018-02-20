-- @XGenRepeat section='Entities' begin='CREATE' end='GO'
CREATE FUNCTION [System_name].[udf_PIT_Entity_name] (
	 @PointInTime DATETIME2(2)
)
RETURNS TABLE
AS
RETURN
  SELECT
       [KeyAttribute_name_1]
      ,[KeyAttribute_name_2N]
      ,[NonKeyAttribute_name_1]
      ,[NonKeyAttribute_name_2N]
  FROM [Raw].[System_name_Entity_name]
  WHERE @PointInTime BETWEEN [LoadDateTime] AND [EndDateTime];
GO