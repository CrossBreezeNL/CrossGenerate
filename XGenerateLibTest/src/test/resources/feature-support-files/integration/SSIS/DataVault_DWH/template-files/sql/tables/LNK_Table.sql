-- @XGenTextSection(name="Relation" literalOnLastLine="GO")
CREATE TABLE [Relation_owner].[L_Relation_name] (
  [L_Relation_name_SQN]      BIGINT NOT NULL IDENTITY(1,1),
  -- @XGenTextSection(name="RelatedEntity") 
  [H_RelatedEntity_parentReferenceName_SQN] BIGINT    NULL,
  [StageDateTime]            datetime2(2) NOT NULL,
  [LoadDateTime]             datetime2(2) NOT NULL,
  CONSTRAINT [PK_Relation_owner_H_Relation_name] PRIMARY KEY NONCLUSTERED ([L_Relation_name_SQN]),
  CONSTRAINT [UK_Relation_owner_H_Relation_name] UNIQUE(
    -- @XGenTextSection(name="RelatedEntity" suffix=",")
    [H_RelatedEntity_parentReferenceName_SQN]
  )
);
GO
