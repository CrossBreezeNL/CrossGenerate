@echo off
REM Set the location of the CrossGenerate jar.
set XG="C:\git\CrossBreeze\CrossGenerateJava\CrossGenerateJava\Run\XGenerate.jar"
REM Set the output directory.
set OutputDir=C:\CrossGenerate\Output
REM Get a locale independent date and time.
FOR /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /format:list') do SET datetime=%%I
SET datetime=%datetime:~0,8%_%datetime:~8,6%
REM Set the log file location.
SET LogFile=Generate_DWH_DataVault_%datetime%.log

echo Copying template output folder...
xcopy /E /Y /Q "template-output-folder" "%OutputDir%"

echo Generating Staging DDL and ETL...
 java -jar %XG% ^
    -c XGenAppConfig.xml ^
    -mtc source_model.xml::sql\DWH_Staging_System_name\schemas\Entity_owner.sql::DWH_Staging_SQL_Owner.xml ^
    -mtc source_model.xml::sql\DWH_Staging_System_name\tables\Entity_owner.Entity_name.sql::DWH_Staging_SQL_Entity.xml ^
    -mtc source_model.xml::sql\DWH_Staging_System_name\DWH_Staging_System_name.sqlproj::DWH_Staging_SQLProj.xml ^
    -mtc source_model.xml::ssis\DWH_Staging_System_name\Load_Entity_owner_Entity_name.dtsx::DWH_Staging_SSIS_Entity.xml ^
    -mtc source_model.xml::ssis\DWH_Staging_System_name\Load_DWH_Staging_System_name.dtsx::DWH_Staging_SSIS_Master.xml ^
    -mtc source_model.xml::ssis\DWH_Staging_System_name\DWH_Staging_System_name.dtproj::DWH_Staging_DTProj.xml ^
    -mtc reference_model.xml::sql\DWH_Staging_System_name\schemas\Entity_owner.sql::DWH_Staging_SQL_Owner.xml ^
    -mtc reference_model.xml::sql\DWH_Staging_System_name\tables\Entity_owner.Entity_name.sql::DWH_Staging_SQL_Entity.xml ^
    -mtc reference_model.xml::sql\DWH_Staging_System_name\DWH_Staging_System_name.sqlproj::DWH_Staging_SQLProj.xml ^
    -mtc reference_model.xml::ssis\DWH_Staging_System_name\Load_Entity_owner_Entity_name.dtsx::DWH_Staging_SSIS_Entity.xml ^
    -mtc reference_model.xml::ssis\DWH_Staging_System_name\Load_DWH_Staging_System_name.dtsx::DWH_Staging_SSIS_Master.xml ^
    -mtc reference_model.xml::ssis\DWH_Staging_System_name\DWH_Staging_System_name.dtproj::DWH_Staging_DTProj.xml ^
    -fld "%OutputDir%\%LogFile%"

echo Generating DWH DDL and ETL...
 java -jar %XG% ^
    -c XGenAppConfig.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\schemas\MappableObject_owner.sql::DWH_DataVault_SQL_MappableObject.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\tables\Entity_owner.H_Entity_name.sql::DWH_DataVault_SQL_Entity.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\tables\Entity_owner.HS_Entity_name.sql::DWH_DataVault_SQL_Entity.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\tables\Relation_owner.L_Relation_name.sql::DWH_DataVault_SQL_Relation.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\tables\Relation_owner.LS_Relation_name.sql::DWH_DataVault_SQL_Relation.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\functions\LookupRule_owner.udf_BR_LookupRule_name.sql::DWH_DataVault_SQL_LookupRule.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\functions\DeriveRule_owner.udf_BR_DeriveRule_name.sql::DWH_DataVault_SQL_DeriveRule.xml ^
    -mtc dwh_model.xml::sql\DWH_DataVault\DWH_DataVault.sqlproj::DWH_DataVault_SQLProj.xml ^
    -mtc dwh_model.xml::ssis\DWH_DataVault\Load_Entity_owner_H_Entity_name.dtsx::DWH_DataVault_SSIS.xml ^
    -mtc dwh_model.xml::ssis\DWH_DataVault\Load_Entity_owner_HS_Entity_name.dtsx::DWH_DataVault_SSIS.xml ^
    -mtc dwh_model.xml::ssis\DWH_DataVault\Load_Relation_owner_L_Relation_name.dtsx::DWH_DataVault_SSIS_relation.xml ^
    -mtc dwh_model.xml::ssis\DWH_DataVault\Load_Relation_owner_LS_Relation_name.dtsx::DWH_DataVault_SSIS_relation.xml ^
    -mtc dwh_model.xml::ssis\DWH_DataVault\Load_DWH_DataVault.dtsx::DWH_DataVault_SSIS_Master.xml ^
    -mtc dwh_model.xml::ssis\DWH_DataVault\DWH_DataVault.dtproj::DWH_DataVault_DTProj.xml ^
    -fld "%OutputDir%\%LogFile%" 

echo Done.

pause