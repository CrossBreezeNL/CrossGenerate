-- The type of the record hash column, storing the MD5 hash of only key columns
CREATE TYPE [dbo].[KeyHash]
	FROM VARBINARY(16) NOT NULL;