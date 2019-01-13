@ECHO OFF

REM Get the source name as first parameter from the command line.
SET sourceName=%1
SET sourceType=%2

REM Derive the script path using the full script path.
SET BatchFolderPath=%cd%
SET ScriptFilePath=%~dp0
SET ScriptFolderPath=%ScriptFilePath:~0,-1%

REM Set the working directory to the current script location.
cd %ScriptFolderPath%

REM Set the XG command.
SET xgenerate=java -jar %ScriptFolderPath%\..\..\Run\XGenerate.jar
REM Set the devenv command.
SET devenv="C:\Program Files (x86)\Microsoft Visual Studio\2017\SQL\Common7\IDE\devenv"
REM Set the output directory.
SET OutputDir=%ScriptFolderPath%\..\Output
REM Get a locale independent date and time.
SET datetime=%date:~-4%%date:~-7,2%%date:~-10,2%_%time:~0,2%%time:~3,2%%time:~6,2%
REM Set the log file location.
SET LogFile=Generate_Stg_Vault_%sourceName%_%datetime%.log
SET LogFileFullPath="%OutputDir%\%LogFile%"
SET TemplateSubFolder=%ScriptFolderPath%\..\Template\DB\MsSqlServer
SET OutputSubFolder=%OutputDir%\DB

:PreGeneration
REM If the output folder for this source existst, remove it.
IF EXIST %OutputSubFolder%\Source\%sourceName%\ (
    ECHO Removing output folder for %sourceName%...
    RMDIR /S /Q "%OutputSubFolder%\Source\%sourceName%" >> %LogFileFullPath%
    IF errorlevel 1 GOTO ErrorHandler
)

:Generation
ECHO Generating %sourceName% Staging and SourceVault...
ECHO Generating %sourceName% Staging and SourceVault... >> %LogFileFullPath%
%xgenerate% ^
 -config %ScriptFolderPath%\XGenAppConfig_DB_MsSqlServer.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/Staging_system_name/Schemas/table_owner.sql"::Source/Staging_Vault_owner.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/Staging_system_name/Tables/table_owner.table_name.sql"::Source/Staging/%sourceType%/Staging_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/Staging_system_name/Stored Procedures/dbo.usp_DisableUniqueIndexes.sql"::Source/Staging_Vault_procs.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/Staging_system_name/Stored Procedures/dbo.usp_EnableUniqueIndexes.sql"::Source/Staging_Vault_procs.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/Staging_system_name/Staging_system_name.sqlproj"::Source/Staging/Staging_sqlproj.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Schemas/table_owner.sql"::Source/Staging_Vault_owner.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Tables/table_owner.H_table_name.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Tables/table_owner.HS_table_name.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Stored Procedures/table_owner.usp_Load_H_table_name.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Stored Procedures/table_owner.usp_Load_HS_table_name.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Stored Procedures/dbo.usp_DisableForeignKeys.sql"::Source/Staging_Vault_procs.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Stored Procedures/dbo.usp_EnableForeignKeys.sql"::Source/Staging_Vault_procs.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/table_owner.ACTR_table_id.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/table_owner.ACTR_table_id_HSK.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/table_owner.ACTS_table_id.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/table_owner.HIST_table_id.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/table_owner.PITR_table_id.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/table_owner.PITR_table_id_HSK.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/table_owner.PITS_table_id.sql"::Source/SourceVault/SourceVault_table.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/EntryPoints/table_owner.ACTR_table_id_uniqueKey_id.sql"::Source/SourceVault/SourceVault_entryPoint_function.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/Functions/EntryPoints/table_owner.PITR_table_id_uniqueKey_id.sql"::Source/SourceVault/SourceVault_entryPoint_function.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/SourceVault_system_name.publish.xml"::Source/Staging_Vault_system.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/SourceVault_system_name/SourceVault_system_name.sqlproj"::Source/SourceVault/SourceVault_sqlproj.xml ^
 -mtc Source/%sourceName%.xmodel.xml::"Source/system_name/system_name.sln"::Source/Staging_Vault_sln.xml ^
 -fld %LogFileFullPath% -fll WARNING
IF errorlevel 1 GOTO ErrorHandler
ECHO Generation done. >> %LogFileFullPath%

:PostGeneration
ECHO Copying the PreDeployment script...
ECHO Copying the PreDeployment script... >> %LogFileFullPath%
MKDIR "%OutputSubFolder%\Common"
MKDIR "%OutputSubFolder%\Common\DacPac"
COPY "%TemplateSubFolder%\Common\DacPac\*.dacpac" "%OutputSubFolder%\Common\DacPac\" >> %LogFileFullPath%
IF errorlevel 1 GOTO ErrorHandler

ECHO Copying the Common DacPacs...
ECHO Copying the Common DacPacs... >> %LogFileFullPath%
MKDIR "%OutputSubFolder%\Source\%sourceName%\Staging_%sourceName%\Scripts\"
COPY "%TemplateSubFolder%\Source\system_name\Staging_system_name\Scripts\Script.PreDeployment.sql" "%OutputSubFolder%\Source\%sourceName%\Staging_%sourceName%\Scripts\" >> %LogFileFullPath%
IF errorlevel 1 GOTO ErrorHandler

:Build
ECHO Building solution
ECHO Building solution >> %LogFileFullPath%
REM Devenv docs: https://msdn.microsoft.com/nl-nl/library/xee0c8y7.aspx
%devenv% "%OutputSubFolder%\Source\%sourceName%\%sourceName%.sln" /Build Debug /Out %LogFileFullPath% >nul
IF errorlevel 1 GOTO ErrorHandler
REM Sleep for 1 second so the log file is released.
TIMEOUT /T 1

:PostBuild
ECHO Moving dacpacs and publish profiles
ECHO Moving dacpacs and publish profiles >> %LogFileFullPath%
MKDIR "%OutputSubFolder%\DacPac\"
MOVE "%OutputSubFolder%\Source\%sourceName%\Staging_%sourceName%\bin\Debug\Staging_%sourceName%.dacpac" "%OutputSubFolder%\DacPac\" >> "%LogFileFullPath%"
COPY "%TemplateSubFolder%\Source\system_name\Staging_system_name\Staging.publish.xml" "%OutputSubFolder%\DacPac\" >> "%LogFileFullPath%"
MOVE "%OutputSubFolder%\Source\%sourceName%\SourceVault_%sourceName%\bin\Debug\SourceVault_%sourceName%.dacpac" "%OutputSubFolder%\DacPac\" >> "%LogFileFullPath%"
COPY "%OutputSubFolder%\Source\%sourceName%\SourceVault_%sourceName%\SourceVault_%sourceName%.publish.xml" "%OutputSubFolder%\DacPac\" >> "%LogFileFullPath%"
IF errorlevel 1 GOTO ErrorHandler

ECHO.
ECHO Done.
ECHO Done. >> %LogFileFullPath%
REM Set the working directory back to wat it was.
CD %BatchFolderPath%
ECHO.

PAUSE
REM If we get to this point no errors occured, so we skip to the end of the file.
GOTO :EOF

:ErrorHandler
REM Write a message on the stderr.
ECHO.
ECHO !!! An error occured during generation, please consult the log file 1>&2
ECHO.
REM Set the working directory back to wat it was.
CD %BatchFolderPath%
PAUSE
