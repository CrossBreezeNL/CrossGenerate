CREATE TABLE [BusinessVault].[HS_Order] (
  [H_Order_SQN]        BIGINT NOT NULL,
  [OrderDate]     datetime    NULL,
  [OrderNumber]     varchar(50)    NULL,
  [CustomerId]     int    NULL,
  [TotalAmount]     decimal(12,2)    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_BusinessVault_HS_Order] PRIMARY KEY NONCLUSTERED ([H_Order_SQN], [LoadDateTime]),
  CONSTRAINT [FK_BusinessVault_HS_Order_BusinessVault_H_Order] FOREIGN KEY ([H_Order_SQN]) REFERENCES [BusinessVault].[H_Order] ([H_Order_SQN])
);
GO
