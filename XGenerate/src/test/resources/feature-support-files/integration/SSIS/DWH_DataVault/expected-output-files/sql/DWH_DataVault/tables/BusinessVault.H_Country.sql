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
