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

echo Generating Staging DDL and ETL...
 java -jar %XG% ^
    -c XGenAppConfig.xml ^
    -mtc source_model.xml::sql\DataVault_DWH\tables\Staging_Table_System_name.sql::DataVault_DWH_SQL.xml ^
    -mtc source_model.xml::ssis\DataVault_Staging_System_name\Load_System_name_Entity_name.dtsx::DataVault_DWH_SSIS.xml ^
    -mtc source_model.xml::ssis\DataVault_Staging_System_name\Load_DataVault_Staging_System_name.dtsx::DataVault_Staging_master.xml ^
    -mtc source_model.xml::ssis\DataVault_Staging_System_name\DataVault_Staging_System_name.dtproj::DataVault_Staging_DTProj.xml ^
    -mtc reference_model.xml::sql\DataVault_DWH\tables\Staging_Table_System_name.sql::DataVault_DWH_SQL.xml ^
    -mtc reference_model.xml::ssis\DataVault_Staging_System_name\Load_System_name_Entity_name.dtsx::DataVault_DWH_SSIS.xml ^
    -mtc reference_model.xml::ssis\DataVault_Staging_System_name\Load_DataVault_Staging_System_name.dtsx::DataVault_Staging_master.xml ^
    -mtc reference_model.xml::ssis\DataVault_Staging_System_name\DataVault_Staging_System_name.dtproj::DataVault_Staging_DTProj.xml ^
    -fld "%OutputDir%\%LogFile%"

echo Generating DWH DDL and ETL...
 java -jar %XG% ^
    -c XGenAppConfig.xml ^
    -mtc dwh_model.xml::sql\DataVault_DWH\tables\HUB_Table.sql::DataVault_DWH_SQL.xml ^
    -mtc dwh_model.xml::sql\DataVault_DWH\tables\HUB_SAT_Table.sql::DataVault_DWH_SQL.xml ^
    -mtc dwh_model.xml::sql\DataVault_DWH\tables\LNK_Table.sql::DataVault_DWH_SQL.xml ^
    -mtc dwh_model.xml::sql\DataVault_DWH\tables\LNK_SAT_Table.sql::DataVault_DWH_SQL.xml ^
    -mtc dwh_model.xml::sql\DataVault_DWH\functions\BR_Lookup_Function.sql::DataVault_DWH_SQL.xml ^
    -mtc dwh_model.xml::sql\DataVault_DWH\functions\BR_Derive_Function.sql::DataVault_DWH_SQL.xml ^
    -mtc dwh_model.xml::ssis\DataVault_DWH\Load_Entity_owner_H_Entity_name.dtsx::DataVault_DWH_SSIS.xml ^
    -mtc dwh_model.xml::ssis\DataVault_DWH\Load_Entity_owner_HS_Entity_name.dtsx::DataVault_DWH_SSIS.xml ^
    -mtc dwh_model.xml::ssis\DataVault_DWH\Load_Relation_owner_L_Relation_name.dtsx::DataVault_DWH_SSIS_relation.xml ^
    -mtc dwh_model.xml::ssis\DataVault_DWH\Load_Relation_owner_LS_Relation_name.dtsx::DataVault_DWH_SSIS_relation.xml ^
    -mtc dwh_model.xml::ssis\DataVault_DWH\Load_Datavault.dtsx::DataVault_DWH_SSIS_master.xml ^
    -mtc dwh_model.xml::ssis\DataVault_DWH\DataVault_DWH.dtproj::DataVault_DWH_DTProj.xml ^
    -fld "%OutputDir%\%LogFile%" 

echo Done.

pause