CREATE FUNCTION [table_owner].[PITR_table_id_HSK] (
    @H_system_name_table_owner_table_name_HSK [KeyHash],
    @PointInTime [AuditDateTime]
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
	WHERE [H_table_name_HSK] = @H_system_name_table_owner_table_name_HSK
		-- @XGenComment(Filter on active records on the PointInTime.)
		AND @PointInTime >= [RecordLoadDateTime]
		AND @PointInTime < [RecordEndDateTime]
);
