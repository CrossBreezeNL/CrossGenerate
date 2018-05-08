CREATE TABLE [BusinessVault].[H_Customer] (
  [H_Customer_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [Id]        int    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Customer] PRIMARY KEY NONCLUSTERED ([H_Customer_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Customer] UNIQUE(
    [Id]
  )
);
GO
CREATE TABLE [BusinessVault].[H_Order] (
  [H_Order_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [Id]        int    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Order] PRIMARY KEY NONCLUSTERED ([H_Order_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Order] UNIQUE(
    [Id]
  )
);
GO
CREATE TABLE [BusinessVault].[H_Country] (
  [H_Country_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  [Code]        nvarchar(3)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Country] PRIMARY KEY NONCLUSTERED ([H_Country_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Country] UNIQUE(
    [Code]
  )
);
GO
