CREATE TABLE [BusinessVault].[HS_Country] (
  [H_Country_SQN]        BIGINT NOT NULL,
  [Description]     nvarchar(100)    NULL,
  [Continent]     nvarchar(100)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
    CONSTRAINT [PK_BusinessVault_HS_Country] PRIMARY KEY NONCLUSTERED ([H_Country_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Country_BusinessVault_H_Country] FOREIGN KEY ([H_Country_SQN]) REFERENCES [BusinessVault].[H_Country] ([H_Country_SQN])
);
GO
