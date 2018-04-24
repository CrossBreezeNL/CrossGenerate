-- @XGenRepeat section='Entities' begin='CREATE' end='GO'
CREATE TABLE [Raw].[System_name_Entity_name] (
       [KeyAttribute_name_1]      KeyAttribute_datatype    NULL
      ,[KeyAttribute_name_2N]     KeyAttribute_datatype    NULL
      ,[NonKeyAttribute_name_1]   NonKeyAttribute_datatype NULL
      ,[NonKeyAttribute_name_2N]  NonKeyAttribute_datatype NULL
      ,[StageDateTime]            DATETIME2(2) NOT NULL
      ,[LoadDateTime]             DATETIME2(2) NOT NULL
      ,[EndDateTime]              DATETIME2(2) NOT NULL
);
GO