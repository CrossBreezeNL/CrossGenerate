/*
 * Do not change the database path or name variables.
 * Any sqlcmd variables will be properly substituted during 
 * build and deployment.
 */
ALTER DATABASE [$(DatabaseName)]
	ADD FILE
	(
		NAME = [FGF_DV_EndedRecords],
		FILENAME = '$(DefaultDataPath)$(DefaultFilePrefix)_FGF_DV_EndedRecords.ndf'
	)
	TO FILEGROUP [FG_DV_EndedRecords];