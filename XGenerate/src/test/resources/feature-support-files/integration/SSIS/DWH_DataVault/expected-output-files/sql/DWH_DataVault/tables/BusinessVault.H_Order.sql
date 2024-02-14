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
