CREATE TABLE [BusinessVault].[HS_Customer] (
  [H_Customer_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [FirstName]     nvarchar(50)    NULL,
  [LastName]     nvarchar(100)    NULL,
  [City]     nvarchar(50)    NULL,
  [Country_Code]     nvarchar(3)    NULL,
  [Country_Description]     nvarchar(100)    NULL,
  [Country_Continent]     nvarchar(100)    NULL,
  [Phone]     nvarchar(20)    NULL,
  [FullName]     nvarchar(151)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_HS_Customer] PRIMARY KEY NONCLUSTERED ([H_Customer_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Customer_BusinessVault_H_Customer] FOREIGN KEY ([H_Customer_SQN]) REFERENCES [BusinessVault].[H_Customer] ([H_Customer_SQN])
);
GO
CREATE TABLE [BusinessVault].[HS_Order] (
  [H_Order_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [OrderDate]     datetime2(3)    NULL,
  [OrderNumber]     nvarchar(50)    NULL,
  [CustomerId]     int    NULL,
  [TotalAmount]     decimal(12,2)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_HS_Order] PRIMARY KEY NONCLUSTERED ([H_Order_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Order_BusinessVault_H_Order] FOREIGN KEY ([H_Order_SQN]) REFERENCES [BusinessVault].[H_Order] ([H_Order_SQN])
);
GO
CREATE TABLE [BusinessVault].[HS_Country] (
  [H_Country_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [Description]     nvarchar(100)    NULL,
  [Continent]     nvarchar(100)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_HS_Country] PRIMARY KEY NONCLUSTERED ([H_Country_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Country_BusinessVault_H_Country] FOREIGN KEY ([H_Country_SQN]) REFERENCES [BusinessVault].[H_Country] ([H_Country_SQN])
);
GO
