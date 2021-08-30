# SQL templates
In this section we will generate the templates of our tareget-database. The target database sould contain the folowing two schema's:

- Staging
- Archive

Foreach schema we will create a table-template and template of the stored procedures.

## Template of staging tables
In the code-block below you could see the SQL-template of the staging tables 

```sql
CREATE TABLE [staging].[table_name]
(
    [BatchId] INT NOT NULL, 
    -- @XGenTextSection(name='keyAttribute')
    [keyAttribute_name] VARCHAR(10) NOT NULL , 
    -- @XGenTextSection(name='nonKeyAttribute')	
    [nonKeyAttribute_name] VARCHAR(10) NULL, 
    PRIMARY KEY ([BatchId]
    -- @XGenTextSection(name='keyAttribute')
    ,[keyAttribute_name]
    )
)
```

## Template of archive tables 
In the code-block below you could see the SQL-template of the archive tables 

```sql
CREATE TABLE [archive].[table_name]
(
    [BatchId] INT NOT NULL, 
    -- @XGenTextSection(name='keyAttribute')
    [keyAttribute_name] VARCHAR(10) NOT NULL , 
    -- @XGenTextSection(name='nonKeyAttribute')	
    [nonKeyAttribute_name] VARCHAR(10) NULL, 
    [Update_BatchId] INT NULL,
    [IsValid]   Bit Default 1 NOT NULL
    PRIMARY KEY ([BatchId]
    -- @XGenTextSection(name='keyAttribute')
    ,[keyAttribute_name]
    )
)
```

## The Stored Procedure update_table_name
This stored procedure will be used to update the archive tables based on the changes in the staging tables. 

```sql
CREATE PROCEDURE [archive].[update_table_name]
	@batchId INT
AS
BEGIN
	-- update records in table_name for which a new, modified record is found in the staging table
	UPDATE a
		SET	IsValid = 0,
			Update_BatchId = @batchId
	FROM archive.[table_name] a
	INNER JOIN staging.[table_name] s
	  ON
		--@XGenTextSection(name='keyAttribute' suffix=' AND')
		a.keyAttribute_name = s.keyAttribute_name
	WHERE a.IsValid = 1
	AND (
	--@XGenTextSection(name='nonKeyAttribute' prefix = ' OR ')
	((a.nonKeyAttribute_name != s.nonKeyAttribute_name) OR (a.nonKeyAttribute_name IS NULL AND s.nonKeyAttribute_name IS NOT NULL) OR (a.nonKeyAttribute_name IS NOT NULL and s.nonKeyAttribute_name IS NULL))
	);


	--Insert all records from the staging table for which a valid record is not found in the archive
	INSERT INTO archive.[table_name]
	(
		--@XGenTextSection(name='keyAttribute')
		keyAttribute_name,
		--@XGenTextSection(name='nonKeyAttribute')
		nonKeyAttribute_name,
		BatchId,
		IsValid,
		Update_BatchId
	)
	SELECT 
		--@XGenTextSection(name='keyAttribute')
		s.keyAttribute_name,
		--@XGenTextSection(name='nonKeyAttribute')
		s.nonKeyAttribute_name,
		@batchId,
		1,
		NULL
	FROM staging.[table_name] s
	LEFT JOIN archive.[table_name] a
	   ON a.IsValid = 1
		--@XGenTextSection(name='keyAttribute')
	  AND a.keyAttribute_name = s.keyAttribute_name
	WHERE a.BatchId IS NULL;
END
```

## Attachments
The entire tutorial, including all config-files and templates, can be found in the following zip file:

- [Tutorial.zip](../CrossGenerate_Tutorial.zip).
Note: To view all templates of this tutorial, move to the folder (Templates).