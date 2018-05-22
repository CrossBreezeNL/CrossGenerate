CREATE TABLE [BusinessVault].[LS_Order_Customer] (
  [L_Order_Customer_SQN]      BIGINT NOT NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
    CONSTRAINT [PK_BusinessVault_LS_Order_Customer] PRIMARY KEY NONCLUSTERED ([L_Order_Customer_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_LS_Order_Customer_BusinessVault_L_Order_Customer] FOREIGN KEY ([L_Order_Customer_SQN]) REFERENCES [BusinessVault].[L_Order_Customer] ([L_Order_Customer_SQN])
);
GO
