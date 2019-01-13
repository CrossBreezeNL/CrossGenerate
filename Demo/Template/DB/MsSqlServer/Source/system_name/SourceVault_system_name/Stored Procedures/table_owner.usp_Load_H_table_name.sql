CREATE PROCEDURE [table_owner].[usp_Load_H_table_name]
	 @ProcessID [ProcessIdentifier]
	,@ProcessDateTime [AuditDateTime]
	,@RecordsInserted BIGINT OUTPUT
AS
BEGIN

	-- @XGenComment(Select all distinct source hash keys and key columns)
	WITH [SourceKeys_CTE] AS (
		SELECT
			 [KeyHash] AS [H_table_name_HSK]
			-- @XGenTextSection(name='keyColumn')
			,[keyColumn_name]
		FROM [$(Staging_system_name)].[table_owner].[table_name] WITH(NOLOCK)
	)
	INSERT INTO [table_owner].[H_table_name] WITH (TABLOCK)
	(
		 [H_table_name_HSK]
		-- @XGenTextSection(name='keyColumn')
		,[keyColumn_name]
		,[RecordLoadProcess]
		,[RecordLoadDateTime]
	)
	-- @XGenComment(Find all new keys)
	SELECT
		 src.[H_table_name_HSK]
		-- @XGenTextSection(name='keyColumn')
		,src.[keyColumn_name]
		,@ProcessID
		,@ProcessDateTime
	FROM [SourceKeys_CTE] src
	WHERE NOT EXISTS (
		SELECT 1
		FROM [table_owner].[H_table_name] hub
		WHERE hub.[H_table_name_HSK] = src.[H_table_name_HSK]
	);

	-- Store inserted records.
	SET @RecordsInserted = ROWCOUNT_BIG();

END
