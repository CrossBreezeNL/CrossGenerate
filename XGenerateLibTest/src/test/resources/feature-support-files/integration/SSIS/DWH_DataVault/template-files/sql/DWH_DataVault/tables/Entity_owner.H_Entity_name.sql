CREATE TABLE [Entity_owner].[H_Entity_name] (
  [H_Entity_name_SQN]        BIGINT NOT NULL IDENTITY(1,1),
  -- @XGenTextSection(name="KeyAttribute")
  [KeyAttribute_name]        KeyAttribute_fulldatatype    NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_Entity_owner_H_Entity_name] PRIMARY KEY NONCLUSTERED ([H_Entity_name_SQN]),
  CONSTRAINT [UK_Entity_owner_H_Entity_name] UNIQUE(
    -- @XGenTextSection(name="KeyAttribute" suffix=",")
    [KeyAttribute_name]
  )
);
GO
