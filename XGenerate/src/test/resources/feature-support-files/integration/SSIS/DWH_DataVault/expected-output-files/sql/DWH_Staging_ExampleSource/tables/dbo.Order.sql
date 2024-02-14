CREATE TABLE [dbo].[Order] (
  [Id]          int    NULL,
  [OrderDate]          datetime    NULL,
  [OrderNumber]          varchar(50)    NULL,
  [CustomerId]          int    NULL,
  [TotalAmount]          decimal(12,2)    NULL,
  [StageDateTime]           datetime2(2) NOT NULL,
);
GO
