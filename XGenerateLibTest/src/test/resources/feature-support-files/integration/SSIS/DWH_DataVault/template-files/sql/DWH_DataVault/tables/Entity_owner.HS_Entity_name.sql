CREATE TABLE [Entity_owner].[HS_Entity_name] (
  [H_Entity_name_SQN]        BIGINT NOT NULL,
  -- @XGenTextSection(name="NonKeyAttribute") 
  [NonKeyAttribute_name]     NonKeyAttribute_fulldatatype    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_Entity_owner_HS_Entity_name] PRIMARY KEY NONCLUSTERED ([H_Entity_name_SQN], [LoadDateTime]),
  CONSTRAINT [FK_Entity_owner_HS_Entity_name_Entity_owner_H_Entity_name] FOREIGN KEY ([H_Entity_name_SQN]) REFERENCES [Entity_owner].[H_Entity_name] ([H_Entity_name_SQN])
);
GO
