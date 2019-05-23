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
 -mtc ExampleModel.xml::sql/Staging_Tables_system_name.sql::ExampleSQLConfig.xml

echo.
pause