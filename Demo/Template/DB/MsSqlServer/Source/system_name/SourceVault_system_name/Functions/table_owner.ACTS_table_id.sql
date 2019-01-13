CREATE FUNCTION [table_owner].[ACTS_table_id] (
) RETURNS TABLE 
AS
RETURN (
    SELECT
		 [table_name].[H_table_name_HSK] AS [H_system_name_table_owner_table_name_HSK]
        -- @XGenTextSection(name='keyColumn')
        ,[table_name].[keyColumn_name] AS [keyColumn_id]
        -- @XGenTextSection(name='nonKeyColumn')
        ,[table_name].[nonKeyColumn_name] AS [nonKeyColumn_id]
        -- @XGenTextSection(name='nonKeyComputedColumn')
        ,[table_name].[nonKeyComputedColumn_name] AS [nonKeyComputedColumn_id]
		-- @XGenTextSection(name='ignore')
		,businessKeyColumn_computedExpression
    FROM [table_owner].[HS_table_name] AS [table_name] WITH(NOLOCK)
	-- @XGenComment(Filter on active records.)
	WHERE [table_name].[RecordEndDateTime] = CAST('9999-12-31 00:00:00.00' AS DATETIME2(2))
);
