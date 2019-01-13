-- The type of the audit date-time columns.
CREATE TYPE [dbo].[AuditDateTime]
	FROM DATETIME2(2) NOT NULL;