-- A view on the table without a schema name, so you can select using only the table.
CREATE VIEW [table_name]
AS
SELECT *
FROM [table_owner].[table_name];