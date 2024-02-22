#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SSIS
@Integration
Feature: Integration_SSIS_DWH_DataVault
  This feature file contains the DataVault DWH example when using SSIS in CrossGenerate templates

  Scenario: Generate HUB Table
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/sql/DWH_DataVault/tables/Entity_owner.H_Entity_name.sql"
    And the following config file: "config-files/DWH_DataVault_SQL_Entity.xml"
    When I run the generator
    Then I expect 3 generation results
    And an output named "BusinessVault.H_Country.sql" with contents equal to file: "expected-output-files/sql/DWH_DataVault/tables/BusinessVault.H_Country.sql"
    And an output named "BusinessVault.H_Customer.sql" with contents equal to file: "expected-output-files/sql/DWH_DataVault/tables/BusinessVault.H_Customer.sql"
    And an output named "BusinessVault.H_Order.sql" with contents equal to file: "expected-output-files/sql/DWH_DataVault/tables/BusinessVault.H_Order.sql"

  Scenario: Generate HUB-SAT Table
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/sql/DWH_DataVault/tables/Entity_owner.HS_Entity_name.sql"
    And the following config file: "config-files/DWH_DataVault_SQL_Entity.xml"
    When I run the generator
    Then I expect 3 generation results
    And an output named "BusinessVault.HS_Country.sql" with contents equal to file: "expected-output-files/sql/DWH_DataVault/tables/BusinessVault.HS_Country.sql"
    And an output named "BusinessVault.HS_Customer.sql" with contents equal to file: "expected-output-files/sql/DWH_DataVault/tables/BusinessVault.HS_Customer.sql"
    And an output named "BusinessVault.HS_Order.sql" with contents equal to file: "expected-output-files/sql/DWH_DataVault/tables/BusinessVault.HS_Order.sql"

  Scenario Outline: Generate <Scenario>
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/sql/DWH_DataVault/<Folder><Template>"
    And the following config file: "config-files/<ConfigFile>"
    When I run the generator
    Then I expect 1 generation results
    And an output named "<OutputFile>" with contents equal to file: "expected-output-files/sql/DWH_DataVault/<Folder><OutputFile>"

    Examples: 
      | Scenario            | Folder     | Template                                    | ConfigFile                       | OutputFile                                       |
      | LNK Tables          | tables/    | Relation_owner.L_Relation_name.sql          | DWH_DataVault_SQL_Relation.xml   | BusinessVault.L_Order_Customer.sql               |
      | LNK SAT Tables      | tables/    | Relation_owner.LS_Relation_name.sql         | DWH_DataVault_SQL_Relation.xml   | BusinessVault.LS_Order_Customer.sql              |
      | BR Lookup Functions | functions/ | LookupRule_owner.udf_BR_LookupRule_name.sql | DWH_DataVault_SQL_LookupRule.xml | BusinessRule.udf_BR_Lookup_Country.sql           |
      | BR Derive Functions | functions/ | DeriveRule_owner.udf_BR_DeriveRule_name.sql | DWH_DataVault_SQL_DeriveRule.xml | BusinessRule.udf_BR_Derive_Customer_FullName.sql |
      | SqlProj File        |            | DWH_DataVault.sqlproj                       | DWH_DataVault_SQLProj.xml        | DWH_DataVault.sqlproj                            |

  Scenario: Generate HUB Load Package
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/ssis/DWH_DataVault/Load_Entity_owner_H_Entity_name.dtsx"
    And the following config file: "config-files/DWH_DataVault_SSIS.xml"
    When I run the generator
    Then I expect 3 generation results
    And an output named "Load_BusinessVault_H_Customer.dtsx" with contents equal to file: "expected-output-files/ssis/DWH_DataVault/Load_BusinessVault_H_Customer.dtsx"
    And an output named "Load_BusinessVault_H_Order.dtsx" with contents equal to file: "expected-output-files/ssis/DWH_DataVault/Load_BusinessVault_H_Order.dtsx"
    And an output named "Load_BusinessVault_H_Country.dtsx" with contents equal to file: "expected-output-files/ssis/DWH_DataVault/Load_BusinessVault_H_Country.dtsx"

  Scenario: Generate Load HUB-SAT Package
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/ssis/DWH_DataVault/Load_Entity_owner_HS_Entity_name.dtsx"
    And the following config file: "config-files/DWH_DataVault_SSIS.xml"
    When I run the generator
    Then I expect 3 generation results
    And an output named "Load_BusinessVault_HS_Customer.dtsx" with contents equal to file: "expected-output-files/ssis/DWH_DataVault/Load_BusinessVault_HS_Customer.dtsx"
    And an output named "Load_BusinessVault_HS_Order.dtsx" with contents equal to file: "expected-output-files/ssis/DWH_DataVault/Load_BusinessVault_HS_Order.dtsx"
    And an output named "Load_BusinessVault_HS_Country.dtsx" with contents equal to file: "expected-output-files/ssis/DWH_DataVault/Load_BusinessVault_HS_Country.dtsx"

  Scenario Outline: Generate <Scenario>
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/ssis/DWH_DataVault/<TemplateFile>"
    And the following config file: "config-files/<ConfigFile>.xml"
    When I run the generator
    Then I expect 1 generation results
    And an output named "<OutputFile>" with contents equal to file: "expected-output-files/ssis/DWH_DataVault/<OutputFile>"

    Examples: 
      | Scenario          | TemplateFile                              | ConfigFile                  | OutputFile                                |
      | LNK package       | Load_Relation_owner_L_Relation_name.dtsx  | DWH_DataVault_SSIS_relation | Load_BusinessVault_L_Order_Customer.dtsx  |
      | LNK-SAT package   | Load_Relation_owner_LS_Relation_name.dtsx | DWH_DataVault_SSIS_relation | Load_BusinessVault_LS_Order_Customer.dtsx |
      | DWH Masterpackage | Load_DWH_DataVault.dtsx                   | DWH_DataVault_SSIS_Master   | Load_DWH_DataVault.dtsx                   |
      | DtProj file       | DWH_DataVault.dtproj                      | DWH_DataVault_DTProj        | DWH_DataVault.dtproj                      |
