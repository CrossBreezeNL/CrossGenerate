@echo off
set xgenerate=java -jar .\XGenerate.jar

echo Generating Example...
echo.

REM Run XGenerate
REM  - App Config : XGenerateCfg.xml
REM  - Model      : ExampleModel.xml
REM  - Template   : Staging_Tables_system_name.sql
REM  - Config     : ExampleSQLConfig
%xgenerate% ^
 -config .\XGenerateCfg.xml ^
 -ps true ^
 -mtc model_1.xml::Unit_Config_Template_OutputType_table_name.txt1::config_1.xml

echo.
pause