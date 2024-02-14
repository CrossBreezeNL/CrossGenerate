CREATE TABLE [BusinessVault].[HS_Customer] (
  [H_Customer_SQN]        BIGINT NOT NULL,
  [FirstName]     varchar(50)    NULL,
  [LastName]     varchar(100)    NULL,
  [City]     varchar(50)    NULL,
  [Country_Code]     varchar(3)    NULL,
  [Country_Description]     nvarchar(100)    NULL,
  [Country_Continent]     nvarchar(100)    NULL,
  [Phone]     varchar(20)    NULL,
  [FullName]     varchar(151)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_BusinessVault_HS_Customer] PRIMARY KEY NONCLUSTERED ([H_Customer_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Customer_BusinessVault_H_Customer] FOREIGN KEY ([H_Customer_SQN]) REFERENCES [BusinessVault].[H_Customer] ([H_Customer_SQN])
);
GO
