CREATE PROCEDURE [dbo].[usp_EnableUniqueIndexes]
AS
BEGIN

	-- @XGenTextSection(name='table')
	ALTER INDEX [IX_table_owner_table_name_KeyHash_RecordHash] ON [table_owner].[table_name] REBUILD;

END