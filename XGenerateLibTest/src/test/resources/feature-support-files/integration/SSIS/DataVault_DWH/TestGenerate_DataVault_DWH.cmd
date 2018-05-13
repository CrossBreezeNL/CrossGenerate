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

echo Copying template output folder...
xcopy /E /Y /Q "template-output-folder" "%OutputDir%"



echo Generating DWH DDL and ETL...
 java -jar %XG% -d true -fll Fine ^
    -c XGenAppConfig.xml ^
    -mtc dwh_model.xml::ssis\Load_Relation_owner_LS_Relation_name.dtsx::DataVault_DWH_SSIS_relation.xml  ^
    -fld "%OutputDir%\%LogFile%" 

echo Done.

pause