CREATE FUNCTION [table_owner].[ACTR_table_id_uniqueKey_id] (
	-- @XGenTextSection(name='uniqueKeyColumn' suffix=',')
    @uniqueKeyColumn_id keyColumn_fulldatatype
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
    FROM [table_owner].[HS_table_name] AS [table_name] WITH(NOLOCK)
	-- @XGenComment(Filter on active records.)
	WHERE [RecordEndDateTime] = CAST('9999-12-31 00:00:00.00' AS DATETIME2(2))
        -- @XGenTextSection(name='uniqueKeyColumn')
        AND [keyColumn_name] = @uniqueKeyColumn_id
);
