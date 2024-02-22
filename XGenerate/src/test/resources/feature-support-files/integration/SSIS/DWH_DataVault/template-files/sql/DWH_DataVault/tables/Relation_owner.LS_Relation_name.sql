CREATE TABLE [Relation_owner].[LS_Relation_name] (
  [L_Relation_name_SQN]      BIGINT NOT NULL,
  -- @XGenTextSection(name="NonKeyAttribute") 
  [NonKeyAttribute_name]     NonKeyAttribute_fulldatatype    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  [EndDateTime]              datetime2(2) NOT NULL,
  [RecordHash] VARBINARY(16) NOT NULL, 
  CONSTRAINT [PK_Relation_owner_LS_Relation_name] PRIMARY KEY NONCLUSTERED ([L_Relation_name_SQN], [LoadDateTime]),
  CONSTRAINT [FK_Relation_owner_LS_Relation_name_Relation_owner_L_Relation_name] FOREIGN KEY ([L_Relation_name_SQN]) REFERENCES [Relation_owner].[L_Relation_name] ([L_Relation_name_SQN])
);
GO
