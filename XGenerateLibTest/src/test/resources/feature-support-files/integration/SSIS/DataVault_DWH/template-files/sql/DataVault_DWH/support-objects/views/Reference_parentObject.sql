CREATE VIEW [RelatedEntity_parentOwner].[H_RelatedEntity_parentReferenceName]
	AS 
	SELECT KeyAttribute_name as Attribute_parentObjectAttributeId,
		   H_Entity_name_SQN as H_RelatedEntity_parentReferenceName_SQN
	FROM Entity_owner.H_Entity_name

