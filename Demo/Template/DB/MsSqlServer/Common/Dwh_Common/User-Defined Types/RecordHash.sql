-- The type of the record hash column, storing the MD5 hash of only non-key columns
CREATE TYPE [dbo].[RecordHash]
	FROM VARBINARY(16) NOT NULL;