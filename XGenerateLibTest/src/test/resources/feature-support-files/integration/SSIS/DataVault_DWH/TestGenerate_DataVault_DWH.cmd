@echo off
REM Set the location of the CrossGenerate jar.
set XG="C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\Run\XGenerate.jar"
REM Set the output directory.
set OutputDir=C:\CrossGenerate\Output
REM Get a locale independent date and time.
FOR /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do SET datetime=%%I
SET datetime=%datetime:~0,8%_%datetime:~8,6%
REM Set the log file location.
SET LogFile=Generate_DataVault_DWH_%datetime%.log

echo Generating DWH DDL and ETL...
 java -jar %XG% -d true -fll fine ^
    -c XGenAppConfig.xml ^
    -mtc source_model.xml::sql\DWH_Staging_System_name\schemas\Entity_owner.sql::DWH_Staging_SQL_Owner.xml ^
    -fld "%OutputDir%\%LogFile%" 

echo Done.

pause