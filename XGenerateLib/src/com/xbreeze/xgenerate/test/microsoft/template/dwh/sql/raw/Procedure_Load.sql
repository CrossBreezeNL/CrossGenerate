-- @XGenRepeat section='Entities' begin='CREATE' end='GO'
CREATE PROCEDURE [Raw].[usp_Load_System_name_Entity_name] (
   @LoadDateTime DATETIME2(2)	
) AS
BEGIN
  DECLARE @EndDateTime DATETIME2(2) = '9999-12-31';

  INSERT INTO [Raw].[System_name_Entity_name] (
     [KeyAttribute_name_1]
    ,[KeyAttribute_name_2N]
    ,[NonKeyAttribute_name_1]
    ,[NonKeyAttribute_name_2N]
    ,[StageDateTime]
    ,[LoadDateTime]
    ,[EndDateTime]
  )
  -- Write the inserted keys into a new temp table.
  OUTPUT
     INSERTED.[KeyAttribute_name_1]
    ,INSERTED.[KeyAttribute_name_2N]
  INTO [#tmp_System_name_Entity_name_InsertedKeys]
  -- Write rows which currently don't exist in the table.
  SELECT
     [KeyAttribute_name_1]
    ,[KeyAttribute_name_2N]
    ,[NonKeyAttribute_name_1]
    ,[NonKeyAttribute_name_2N]
    ,[StageDateTime]
    ,@LoadDateTime
    ,@EndDateTime
  FROM [Raw].[udf_SRC_System_name_Entity_name]() src
  LEFT JOIN [Raw].[udf_ACT_System_name_Entity_name] () trg
       ON (trg.[KeyAttribute_name_1] = src.[KeyAttribute_name_1] OR (trg.[KeyAttribute_name_1] IS NULL AND src.[KeyAttribute_name_1] IS NULL))
      AND (trg.[KeyAttribute_name_2N] = src.[KeyAttribute_name_2N] OR (trg.[KeyAttribute_name_2N] IS NULL AND src.[KeyAttribute_name_2N] IS NULL))
      AND (trg.[NonKeyAttribute_name_1] = src.[NonKeyAttribute_name_1] OR (trg.[NonKeyAttribute_name_1] IS NULL AND src.[NonKeyAttribute_name_1] IS NULL))
      AND (trg.[NonKeyAttribute_name_2N] = src.[NonKeyAttribute_name_2N] OR (trg.[NonKeyAttribute_name_2N] IS NULL AND src.[NonKeyAttribute_name_2N] IS NULL));
      
  -- EndDate the records for the keys which just have been inserted.
  UPDATE [Raw].[System_name_Entity_name]
  SET [EndDateTime] = @LoadDateTime
  FROM [Raw].[System_name_Entity_name] AS src
  INNER JOIN [#tmp_System_name_Entity_name_InsertedKeys] AS key
     ON (key.[KeyAttribute_name_1] = src.[KeyAttribute_name_1] OR (key.[KeyAttribute_name_1] IS NULL AND src.[KeyAttribute_name_1] IS NULL))
    AND (key.[KeyAttribute_name_2N] = src.[KeyAttribute_name_2N] OR (key.[KeyAttribute_name_2N] IS NULL AND src.[KeyAttribute_name_2N] IS NULL));
      
END
GO