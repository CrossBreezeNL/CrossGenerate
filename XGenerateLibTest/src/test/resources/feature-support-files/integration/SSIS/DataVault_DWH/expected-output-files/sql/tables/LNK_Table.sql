CREATE TABLE [BusinessVault].[L_Order_Customer] (
  [L_Order_Customer_SQN]      BIGINT NOT NULL IDENTITY(1,1),
  [H_Customer_SQN] BIGINT    NULL,
  [H_Order_SQN] BIGINT    NULL,
  [StageDateTime]            datetime2(2) NOT NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_BusinessVault_H_Order_Customer] PRIMARY KEY NONCLUSTERED ([L_Order_Customer_SQN]),
  CONSTRAINT [UK_BusinessVault_H_Order_Customer] UNIQUE(
    [H_Customer_SQN],
    [H_Order_SQN]
  )
);
GO
