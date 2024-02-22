CREATE TABLE [dbo].[Customer] (
  [Id]          int    NULL,
  [FirstName]          varchar(50)    NULL,
  [LastName]          varchar(100)    NULL,
  [City]          varchar(50)    NULL,
  [Country]          varchar(3)    NULL,
  [Phone]          varchar(20)    NULL,
  [StageDateTime]           datetime2(2) NOT NULL,
);
GO
