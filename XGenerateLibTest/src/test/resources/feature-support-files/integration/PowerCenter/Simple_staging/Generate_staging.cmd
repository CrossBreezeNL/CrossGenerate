@echo off
REM Set the location of the CrossGenerate jar.
set XG="C:\GIT\Repos\CrossBreeze\CrossGenerate\CrossGenerate\Run\XGenerate.jar"
REM Set the output directory.
set OutputDir=C:\CrossGenerate\Output\INFA
REM Get a locale independent date and time.
FOR /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do SET datetime=%%I
SET datetime=%datetime:~0,8%_%datetime:~8,6%
REM Set the log file location.
SET LogFile=Generate_Staging_INFA_%datetime%.log


echo Generating Staging DDL and ETL...
 java -jar %XG% ^
    -c XGenAppConfig.xml ^
    -mtc source_model.xml::stg_Entity_name.sql::ExampleDDLConfig.xml ^
    -mtc source_model.xml::stg_load_system_name.xml::ExamplePowerCenterConfig.xml ^
    -fld "%OutputDir%\%LogFile%"

echo Done.

pause