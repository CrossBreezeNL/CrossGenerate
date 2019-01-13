CREATE TABLE [table_owner].[H_table_name]
(
	-- @XGenComment(The hash key column storing the MD5 hash.)
	[H_table_name_HSK] [KeyHash],
	-- @XGenTextSection(name='keyColumn')
	[keyColumn_name] keyColumn_fulldatatype NULL,
	-- @XGenComment(The load process identifier)
	[RecordLoadProcess] [ProcessIdentifier],
	-- @XGenComment(The batch start date-time this record was loaded)
	[RecordLoadDateTime] [AuditDateTime],
	-- @XGenComment(Create a clustered PK on the hash key so they are stored in order for fast compare)
	CONSTRAINT [PK_table_owner_H_table_name] PRIMARY KEY CLUSTERED ([H_table_name_HSK])
) WITH (DATA_COMPRESSION = ROW);
