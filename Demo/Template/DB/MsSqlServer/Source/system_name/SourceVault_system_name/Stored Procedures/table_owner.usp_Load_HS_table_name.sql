CREATE PROCEDURE [table_owner].[usp_Load_HS_table_name]
	 @ProcessID [ProcessIdentifier]
	,@ProcessDateTime [AuditDateTime]
	,@RecordsRead BIGINT OUTPUT
	,@RecordsUpdated BIGINT OUTPUT
	,@RecordsInserted BIGINT OUTPUT
AS
BEGIN

	DECLARE @ActiveRecordEndDateTime [AuditDateTime] = '9999-12-31 00:00:00.00';

	-- Read nr of records from staging table
	SELECT @RecordsRead = COUNT_BIG(*)
	FROM [$(Staging_system_name)].[table_owner].[table_name] WITH(NOLOCK);


	-- @XGenTextSection(name='updateDelete' literalOnLastLine='End of updateDelete')
	-- Update records that do no longer exist or have changed
	WITH [Source_CTE] AS(
		SELECT
			 [KeyHash] AS [H_table_name_HSK]
			-- @XGenTextSection(name='keyColumn')
			,[keyColumn_name]
			-- @XGenTextSection(name='nonKeyColumn')
			,[nonKeyColumn_name]
			-- @XGenTextSection(name='nonKeyComputedColumn')
			,nonKeyComputedColumn_computedExpression AS [nonKeyComputedColumn_name]
			,[RecordHash]
		FROM [$(Staging_system_name)].[table_owner].[table_name] WITH(NOLOCK)
	), [Target_CTE] AS (
		SELECT *
		FROM [table_owner].[HS_table_name]
		-- @XGenComment(Only select active records.)
		WHERE [RecordEndDateTime] = @ActiveRecordEndDateTime
	)
	UPDATE trg
	SET  trg.[RecordEndDateTime] = @ProcessDateTime
		,trg.[RecordLoadProcess] = @ProcessID
	FROM [Target_CTE] trg
	-- @XGenComment(The record has changed in the source (Same KeyHash, different RecordHash).)
	-- @XGenComment(Or the record is deleted in the source (KeyHash doesn't exist).)
	WHERE NOT EXISTS (
		SELECT 1
		FROM [Source_CTE] AS src
		WHERE src.[H_table_name_HSK] = trg.[H_table_name_HSK]
		  AND src.[RecordHash] = trg.[RecordHash]
	); --End of updateDelete

	-- @XGenTextSection(name='update' literalOnLastLine='End of update')
	-- Update records that do no longer exist or have changed
	WITH [Source_CTE] AS(
		SELECT
			 [KeyHash] AS [H_table_name_HSK]
			-- @XGenTextSection(name='keyColumn')
			,[keyColumn_name]
			-- @XGenTextSection(name='nonKeyColumn')
			,[nonKeyColumn_name]
			-- @XGenTextSection(name='nonKeyComputedColumn')
			,nonKeyComputedColumn_computedExpression AS [nonKeyComputedColumn_name]
			,[RecordHash]
		FROM [$(Staging_system_name)].[table_owner].[table_name] WITH(NOLOCK)
	), [Target_CTE] AS (
		SELECT *
		FROM [table_owner].[HS_table_name]
		-- @XGenComment(Only select active records.)
		WHERE [RecordEndDateTime] = @ActiveRecordEndDateTime
	)
	UPDATE trg
	SET  trg.[RecordEndDateTime] = @ProcessDateTime
		,trg.[RecordLoadProcess] = @ProcessID
	FROM [Source_CTE] AS src
	INNER JOIN [Target_CTE] trg
		ON trg.[H_table_name_HSK] = src.[H_table_name_HSK]
	-- @XGenComment(The record has changed in the source (Same KeyHash, different RecordHash).)
	WHERE src.[RecordHash] <> trg.[RecordHash]; --End of update

	-- Store updated records.
	SET @RecordsUpdated = ROWCOUNT_BIG();


	-- Insert records that are new or changed.
	WITH [Source_CTE] AS(
		SELECT
			 [KeyHash] AS [H_table_name_HSK]
			-- @XGenTextSection(name='keyColumn')
			,[keyColumn_name]
			-- @XGenTextSection(name='nonKeyColumn')
			,[nonKeyColumn_name]
			-- @XGenTextSection(name='nonKeyComputedColumn')
			,nonKeyComputedColumn_computedExpression AS [nonKeyComputedColumn_name]
			,[RecordHash]
		FROM [$(Staging_system_name)].[table_owner].[table_name] WITH(NOLOCK)
	), [Target_CTE] AS (
		SELECT [H_table_name_HSK]
		FROM [table_owner].[HS_table_name]
		-- @XGenComment(Only select active records.)
		WHERE [RecordEndDateTime] = @ActiveRecordEndDateTime
	)
	INSERT INTO [table_owner].[HS_table_name] WITH (TABLOCK)
	(
			 [H_table_name_HSK]
			-- @XGenTextSection(name='keyColumn')
			,[keyColumn_name]
			-- @XGenTextSection(name='nonKeyColumn')
			,[nonKeyColumn_name]
			-- @XGenTextSection(name='nonKeyComputedColumn')
			,[nonKeyComputedColumn_name]
			,[RecordHash]
			,[RecordLoadProcess]
			,[RecordLoadDateTime]
			,[RecordEndDateTime]
	)
	SELECT
		 src.[H_table_name_HSK]
		-- @XGenTextSection(name='keyColumn')
		,src.[keyColumn_name]
		-- @XGenTextSection(name='nonKeyColumn')
		,src.[nonKeyColumn_name]
		-- @XGenTextSection(name='nonKeyComputedColumn')
		,src.[nonKeyComputedColumn_name]
		,src.[RecordHash]
		,@ProcessID
		,@ProcessDateTime
		,@ActiveRecordEndDateTime
	FROM [Source_CTE] src
	WHERE NOT EXISTS (
		SELECT 1
		FROM [Target_CTE] AS trg
		-- @XGenComment(No need to also filter on the RecordHash, since within the active record scope the changed records don't exist anymore.)
		WHERE trg.[H_table_name_HSK] = src.[H_table_name_HSK]
	);

	-- Store inserted records.
	SET @RecordsInserted = ROWCOUNT_BIG();

END
