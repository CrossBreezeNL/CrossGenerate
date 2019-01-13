CREATE TABLE [table_owner].[HS_table_name]
(
	-- @XGenComment(The hash key column storing the MD5 hash.)
	[H_table_name_HSK] [KeyHash],
	-- @XGenComment(Also store the key columns in the sats, to overcome the need to join with the HUB)
	-- @XGenTextSection(name='keyColumn')
	[keyColumn_name] keyColumn_fulldatatype NULL,
	-- @XGenTextSection(name='nonKeyColumn')
	[nonKeyColumn_name] nonKeyColumn_fulldatatype NULL,
	-- @XGenTextSection(name='nonKeyComputedColumn')
	[nonKeyComputedColumn_name] nonKeyComputedColumn_fulldatatype NULL,
	-- @XGenTextSection(name='ignore')
	[businessKeyColumn_computedExpression] nonKeyColumn_fulldatatype NULL,
	-- @XGenComment(The MD5 hash of the record, only non-key columns)
	[RecordHash] [RecordHash],
	-- @XGenComment(The load process identifier)
	[RecordLoadProcess] [ProcessIdentifier] NOT NULL,
	-- @XGenComment(The end process identifier)
	[RecordEndProcess] [ProcessIdentifier] NULL,
	-- @XGenComment(The batch start date-time this record was loaded)
	[RecordLoadDateTime] [AuditDateTime] NOT NULL,
	-- @XGenComment(The batch start date-time this record was ended)
	[RecordEndDateTime] [AuditDateTime] NULL,
	CONSTRAINT [PK_table_owner_HS_table_name] PRIMARY KEY NONCLUSTERED ([H_table_name_HSK], [RecordLoadDateTime]),
	CONSTRAINT [FK_table_owner_HS_table_name_table_owner_H_table_name] FOREIGN KEY ([H_table_name_HSK]) REFERENCES [table_owner].[H_table_name] ([H_table_name_HSK])
);
GO

/**
 * Disable the foreign key to the HUB so it's not enforced.
 */
ALTER TABLE [table_owner].[HS_table_name] NOCHECK CONSTRAINT [FK_table_owner_HS_table_name_table_owner_H_table_name];
GO

/**
 * Create a clustered index on RecordEndDateTime and RecordLoadDateTime so fetching ACT and PIT records is fast.
 */
CREATE CLUSTERED INDEX [IX_table_owner_HS_table_name_PIT]
	ON [table_owner].[HS_table_name] ([RecordEndDateTime], [RecordLoadDateTime])
		WITH (DATA_COMPRESSION = ROW ON PARTITIONS (1), DATA_COMPRESSION = ROW ON PARTITIONS (2))
	ON [PS_DV_RecordEndDateTime]([RecordEndDateTime]);
GO

-- Create a unique index on KeyHash only on the ActiveRecords filegroup.
CREATE UNIQUE NONCLUSTERED INDEX [IX_table_owner_HS_table_name_KeyHash_RecordHash]
	ON [table_owner].[HS_table_name] ([H_table_name_HSK]) INCLUDE ([RecordHash])
	-- Only index active records.
	WHERE [RecordEndDateTime] = '9999-12-31 00:00:00.00'
		WITH (DATA_COMPRESSION = ROW)
	ON [FG_DV_ActiveRecords];
GO