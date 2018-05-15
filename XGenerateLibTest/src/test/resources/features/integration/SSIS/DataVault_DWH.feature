#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SSIS
@Integration
Feature: Integration_SSIS_DataVault_DWH
  This feature file contains the DataVault DWH example when using SSIS in CrossGenerate templates

  Scenario Outline: Generate <Scenario>
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/sql/Datavault_DWH/<TemplateType>/<Template>"
    And the following config file: "config-files/DataVault_DWH_SQL.xml"
    When I run the generator
    Then I expect 1 generation results
    And an output named "<Template>" with contents equal to file: "expected-output-files/sql/<TemplateType>/<Template>"

    Examples: 
      | Scenario            | TemplateType | Template               |
      | HUB Tables          | tables       | HUB_Table.sql          |
      | HUB SAT Tables      | tables       | HUB_SAT_Table.sql      |
      | LNK Tables          | tables       | LNK_Table.sql          |
      | LNK SAT Tables      | tables       | LNK_SAT_Table.sql      |
      | BR Lookup Functions | functions    | BR_Lookup_Function.sql |
      | BR Derive Functions | functions    | BR_Derive_Function.sql |

  Scenario: Generate Load HUB Package
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/ssis/Datavault_DWH/Load_Entity_owner_H_Entity_name.dtsx"
    And the following config file: "config-files/DataVault_DWH_SSIS.xml"
    When I run the generator
    Then I expect 3 generation results
    And an output named "Load_BusinessVault_H_Customer.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_H_Customer.dtsx"
    And an output named "Load_BusinessVault_H_Order.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_H_Order.dtsx"
    And an output named "Load_BusinessVault_H_Country.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_H_Country.dtsx"

  Scenario: Generate Load HUB-SAT Package
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/ssis/Datavault_DWH/Load_Entity_owner_HS_Entity_name.dtsx"
    And the following config file: "config-files/DataVault_DWH_SSIS.xml"
    When I run the generator
    Then I expect 3 generation results
    And an output named "Load_BusinessVault_HS_Customer.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_HS_Customer.dtsx"
    And an output named "Load_BusinessVault_HS_Order.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_HS_Order.dtsx"
    And an output named "Load_BusinessVault_HS_Country.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_HS_Country.dtsx"

  Scenario Outline: Generate <Scenario>
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/ssis/Datavault_DWH/<TemplateFile>"
    And the following config file: "config-files/<ConfigFile>.xml"
    When I run the generator
    Then I expect 1 generation results
    And an output named "<OutputResult>" with contents equal to file: "expected-output-files/ssis/<OutputFile>"

    Examples: 
      | Scenario          | TemplateFile                              | ConfigFile                  | OutputResult                              | OutputFile                                |
      | LNK package       | Load_Relation_owner_L_Relation_name.dtsx  | DataVault_DWH_SSIS_relation | Load_BusinessVault_L_Order_Customer.dtsx  | Load_BusinessVault_L_Order_Customer.dtsx  |
      | LNK-SAT package   | Load_Relation_owner_LS_Relation_name.dtsx | DataVault_DWH_SSIS_relation | Load_BusinessVault_LS_Order_Customer.dtsx | Load_BusinessVault_LS_Order_Customer.dtsx |
      | DWH Masterpackage | Load_Datavault.dtsx                       | DataVault_DWH_SSIS_master   | Load_Datavault.dtsx                       | Load_Datavault.dtsx                       |
      | Project file      | DataVault_DWH.dtproj                      | DataVault_DWH_DTProj        | DataVault_DWH.dtproj                      | DataVault_DWH.dtproj                      |


