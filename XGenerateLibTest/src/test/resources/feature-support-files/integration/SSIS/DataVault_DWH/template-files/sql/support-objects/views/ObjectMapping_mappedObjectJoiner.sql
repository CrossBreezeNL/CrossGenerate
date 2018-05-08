CREATE VIEW [dbo].[ObjectMapping_mappedObjectJoiner]
AS
SELECT
	[MappedAttribute_expression] AS [MappedKeyAttribute_expression],
	[MappedAttribute_expression] AS [MappedNonKeyAttribute_expression]
FROM [MappedObject_mappedObjectSystemName].[MappedObject_mappedObjectName];