# Microsoft SSIS - DataVault - Hub-Sat load package

In this example the Hub-Sat load package for the [DataVault](../DataVault) solution is described.

## Model
See [DWH Model](../../model/DWH_Model)

## Template
### Load_Entity_owner_HS_Entity_name.dtsx

A SSIS package is created with the following components (note the @XGenXmlSection annotations in the description fields of various components):

#### Control Flow
On the Control Flow we have a Data Flow Task with the name 'DFT Load Entity_owner HS_Entity_name'. During generation this will be resolved to the proper entity owner and name.

![Template Control Flow](img/hubsat_control_flow.png)]

#### Data Flow
In the DataFlow task of this template we have:

- A OLE DB Source component with a query implementing the source to target mapping
- A lookup to the target Hub table to obtain the technical Hub key.
- A lookup to the target Hub-Sat table to obtain the current hash value (if any).
- A Derive Column component to add the LoadDateTime and EndDateTime as a columns to the output.
- A Conditional Split to route records (new or modified) to the appropriate next component.
- A Multicast for modified records to enable insert of new version and end-date the existing version.
- A union all combining the new and modified records for insertion.
- An SQL command updating existing records (setting enddate) if a new, changed version is introduced.
- A Destination for inserting new and modified records.

Again the `Entity_owner` and `Entity_name` placeholders are used.

![Template Data Flow](img/hubsat_dataflow.png)

Since setting the appropriate section annotation on input/output and extended columns through the advanced editor has been covered in the [Simple Staging](../Simple_staging) and [Hub load package](./Hub_package) examples, we only focus on CrossGenerate settings specific for this Hub-Sat loading pattern.

##### Source

###### Connection Manager
Note that there is a similar but not equal SQL statement when compared to the Hub load template package. The SQL statement in this Hub-Sat template also exposes nonkey columns and creates a hash column for change detection.

![Template Source Connection Manager](img/hubsat_source_connection.png)

##### Input and Output Properties - External Columns, Input and Output columns
Compared to the Hub package, the Hub-Sat packages has a NonKeyAttribute_name column next to the KeyAttribute_name column. For both these columns, the section annotation needs to be present in the External, Input and Output columns of each relevant component, as illustrated below for the External Columns of the Source component.

![Template Source Input and Output Properties - External Columns](img/hubsat_source_external_columns.png)

##### Hub Lookup 
###### General
Fail on no match, (since Hub key cannot be found).
![Lookup General](img/hubsat_lookup_hub_general.png)

###### Connection
Set the input to the template Hub table.
![Lookup Connection](img/hubsat_lookup_hub_connection.png)

###### Columns
Map KeyAttribute_name for the lookup
![Lookup Connection](img/hubsat_lookup_hub_columns.png)

##### Hub-Sat Lookup
###### General
Ignore failure, since the Hub-Sat record does not have to exist.
![Lookup General](img/hubsat_lookup_sat_general.png)

###### Connection
A Query is defined for getting the current (active) records from the Hub-Sat table.
![Lookup Connection](img/hubsat_lookup_sat_connection.png)

###### Columns
![Lookup Columns](img/hubsat_lookup_sat_columns.png)

##### Conditional Split
![Conditional Split](img/hubsat_conditional_split.png)

##### Destination
###### Connection Manager
The destination needs to be set to the template Hub-Sat table.
![Destination connection manager](img/hubsat_destination_connection.png)

###### Mappings
![Template Destination Mappings](img/hubsat_destination_mapping.png)

### Documentation
For documentation on templates, please see [Template](../../Template).

## Config
See config section in [DataVault](./).

## Output
When running CrossGenerate with the given Model, Template and Config, the following packages are created:

- Load_BusinessVault_HS_Country.dtsx
- Load_BusinessVault_HS_Customer.dtsx
- Load_BusinessVault_HS_Order.dtsx

### Load_BusinessVault_HS_Customer.dtsx

#### Control Flow
[![Output Control Flow](img/hub_output_control_flow.png)](img/hubsat_output_control_flow.png)

#### Data Flow
![Output Data Flow](img/hubsat_output_dataflow.png)

##### Source

###### Connection Manager
Note that the business rules are applied on the SQL statement.
![Output Source Connection Manager](img/hubsat_output_source_connection.png)

###### Columns
![Output Source Columns](img/hubsat_output_source_columns.png)

##### Lookup

###### Connection
![Output Lookup Connection](img/hubsat_output_lookup_connection.png)

###### Columns
![Output Lookup Columns](img/hubsat_output_lookup_columns.png)

##### Enddate BusinesVault HS_Customer

###### SQL Command
![Output Enddate SQL Command](img/hubsat_output_enddate_sql.png)

###### Column Mappings
![Output Enddate Columns](img/hubsat_output_enddate_parameters.png)

##### Destination

###### Connection Manager
![Output Destination Connection Manager](img/hubsat_output_destination_connection.png)

###### Mappings
![Output Destination Mappings](img/hubsat_output_destination_mapping.png)