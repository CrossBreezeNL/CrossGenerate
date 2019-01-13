CREATE TABLE [table_owner].[table_name]
(
	-- @XGenTextSection(name='columnNormal')
	[columnNormal_name] columnNormal_fulldatatype NULL,
	-- @XGenTextSection(name='columnLargeText')
	[columnLargeText_name] NVARCHAR(1000) NULL,
	[StageDateTime] DATETIME2(2) NOT NULL,
	-- @XGenComment(The following two fields need to be here to be able to generate the hash columns, they won't be in the actual output.)
	-- @XGenTextSection(name='ignore' nrOfLines=4)
	[keyColumn_name] columnNormal_fulldatatype NULL,
	[nonKeyColumn_name] columnNormal_fulldatatype NULL,
	[nonKeyComputedColumn_computedExpression] columnNormal_fulldatatype NULL,
	-- @XGenComment(A persisted calculated field containing the hash of the key columns.)
	[KeyHash] AS (
		CONVERT(VARBINARY(16),
			HASHBYTES('MD5', N'|~|'
				-- @XGenTextSection(name='keyColumn' nrOfLines=9)
				+ CASE
					-- @XGenTextSection(name='keyRegularColumn')
					WHEN [keyColumn_name] IS NOT NULL THEN CONCAT(NULL, [keyColumn_name])
					-- @XGenTextSection(name='keyFloatColumn')
					WHEN [keyColumn_name] IS NOT NULL THEN CONCAT(NULL, CONVERT(varbinary(8),[keyColumn_name]))
					-- @XGenTextSection(name='keyGeometryColumn')
					WHEN [keyColumn_name] IS NOT NULL THEN CONCAT(NULL, CONVERT(GEOMETRY, [keyColumn_name]).STAsBinary())						
					ELSE N'<<NULL>>'
				  END + N'|~|'
			)
		)
	) PERSISTED NOT NULL,
	-- @XGenComment(A persisted calculated field containing the hash of the non-key columns.)
	[RecordHash] AS (
		CONVERT(VARBINARY(16),
			HASHBYTES('MD5', N'|~|'
				-- @XGenTextSection(name='nonKeyColumn' nrOfLines=9)
				+ CASE
					-- @XGenTextSection(name='nonKeyRegularColumn')
					WHEN [nonKeyColumn_name] IS NOT NULL THEN CONCAT(NULL, [nonKeyColumn_name])
					-- @XGenTextSection(name='nonKeyFloatColumn')
					WHEN [nonKeyColumn_name] IS NOT NULL THEN CONCAT(NULL, CONVERT(varbinary(8),[nonKeyColumn_name]))
					-- @XGenTextSection(name='nonKeyGeometryColumn')
					WHEN [nonKeyColumn_name] IS NOT NULL THEN CONCAT(NULL, CONVERT(GEOMETRY, [nonKeyColumn_name]).STAsBinary())		
					ELSE N'<<NULL>>'
				  END + N'|~|'
			)
		)
	) PERSISTED NOT NULL
) WITH (DATA_COMPRESSION = ROW);
GO

/**
 * Create a nonclustered unique index on KeyHash including RecordHash so the columns needed for comparing with Hub and Sat are available.
 */
CREATE UNIQUE NONCLUSTERED INDEX [IX_table_owner_table_name_KeyHash_RecordHash]
	ON [table_owner].[table_name] ([KeyHash]) INCLUDE ([RecordHash])
		WITH (DATA_COMPRESSION = ROW);
GO
