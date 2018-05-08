#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SSIS
@Integration
Feature: Integration_SSIS_DataVault_DWH
  This feature file contains the DataVault DWH example when using SSIS in CrossGenerate templates

  Scenario Outline: Generate <Scenario>
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/sql/<TemplateType>/<Template>"
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

  Scenario: Generate Load HUB Package
    Given I have the following model file: "../../../common/dwh_model.xml"
    And the following template file: "template-files/ssis/Load_Entity_owner_H_Entity_name.dtsx"
    And the following config file: "config-files/DataVault_DWH_SSIS.xml"
    When I run the generator
    Then I expect 3 generation results
    And an output named "Load_BusinessVault_H_Customer.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_H_Customer.dtsx"
    And an output named "Load_BusinessVault_H_Order.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_H_Order.dtsx"
    And an output named "Load_BusinessVault_H_Country.dtsx" with contents equal to file: "expected-output-files/ssis/Load_BusinessVault_H_Country.dtsx"
