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
