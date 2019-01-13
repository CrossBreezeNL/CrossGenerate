CREATE PROCEDURE [dbo].[usp_EnableForeignKeys]
AS
BEGIN

	-- @XGenTextSection(name='table')
	ALTER TABLE [table_owner].[HS_table_name] WITH CHECK CHECK CONSTRAINT [FK_table_owner_HS_table_name_table_owner_H_table_name];   

END