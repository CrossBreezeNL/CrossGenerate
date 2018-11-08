# Microsoft SSIS - DataVault - Hub load package

In this example the Hub load package for the [DataVault](../DataVault) solution is described.

## Model
See [DWH Model](../../Model/DWH_model)

## Template
### Load_Entity_owner_H_Entity_name.dtsx

A SSIS package is created with the following components (note the @XGenXmlSection annotations in the description fields of various components):

#### Control Flow
On the Control Flow we have a Data Flow Task with the name 'DFT Load Entity_owner H_Entity_name'. During generation this will be resolved to the proper entity owner and name.

![Template Control Flow](img/hub_control_flow.png)
#### Data Flow
In the DataFlow task of this template we have:

- A OLE DB Source component with a query implementing the source to target mapping.
- A lookup to the target hub table to determine if the record already exists.
- A Derive Column component to add the LoadDateTime as a column to the output.
- A OLE DB Destination component to write the data to the Destination table.

Again the `Entity_owner` and `Entity_name` placeholders are used.

![Template Data Flow](img/hub_dataflow.png)

In this example all components are covered that require CrossGenerate specific settings.

##### Source

###### Connection Manager
In the connection manager window a SQL statement is constructed that implements the source to target mapping including any business logic that is applied. The functions implementing the business logic are covered in the [Business Logic example](../SQL/Businesslogic). Note that the SQL statement consists of several sections, annotated with the `@XGenTextSection` annotation. The text section functionality of CrossGenerate is nested in an XML template.

![Template Source Connection Manager](img/hub_source_connection.png)

###### Input and Output Properties - External Columns
When opening the Advanced Editor for the source component we can go to the 'Input and Output Properties' tab to find the 'KeyAttribute_name' column in the 'External Columns' list.
The defined output column needs to be repeated for every key attribute in our model, so we set the section named 'KeyAttribute' on the [External Columns/attribute_name] element. This way CrossGenerate knows which part in the template to repeat (in this case the [KeyAttribute_name] column).

![Template Source Input and Output Properties - External Columns](img/hub_source_external_columns.png)

###### Input and Output Properties - Output Columns and Error Columns
We do the same for the 'Output Columns' on the Source Output and Source Error Output, since this column also needs to be repeat for every key attribute defined in the model.

![Template Source Input and Output Properties - Output Columns](img/hub_source_output_columns.png)
![Template Source Input and Output Properties - Error Columns](img/hub_source_error_columns.png)

##### Lookup 
The lookup is used to ignore records that already exist.

###### General
Redirect missing records to no match output.
![Lookup General](img/hub_lookup_general.png)

###### Connection
Set the input to the template Hub table.
![Lookup Connection](img/hub_lookup_connection.png)

###### Columns
Map KeyAttribute_name for the lookup.
![Lookup Connection](img/hub_lookup_columns.png)

###### Input and Output Properties - Lookup Input - Input Columns
Using advanced editor to set a section annotation in the input column KeyAttribute_name.
![Input columns](img/hub_lookup_advanced_inputoutput.png)

##### Derived column
The derived colum transformation does not need any specific configuration for CrossGenerate.

##### Destination
###### Connection Manager
The destination needs to be set to the template Hub table
![Destination connection manager](img/hub_destination_connection.png)

###### Mappings
![Template Destination Mappings](img/hub_destination_columns.png)

###### Input and Output Properties - External Columns
![Template Destination Input and Output Properties - External Columns](img/hub_destination_external_columns.png)

###### Input and Output Properties - Input Columns
![Template Destination Input and Output Properties - Input Columns](img/hub_destination_input_columns.png)

### Documentation
For documentation on templates, please see [Template](../../Template).

## Config
See config section in [DataVault](./).

## Output
When running CrossGenerate with the given Model, Template and Config, the following packages are created:

- Load_BusinessVault_H_Country.dtsx
- Load_BusinessVault_H_Customer.dtsx
- Load_BusinessVault_H_Order.dtsx

### Load_BusinessVault_H_Customer.dtsx

#### Control Flow
[![Output Control Flow](img/hub_output_control_flow.png)](img/hub_output_control_flow.png)

#### Data Flow
![Output Data Flow](img/hub_output_dataflow.png)

##### Source

###### Connection Manager
![Output Source Connection Manager](img/hub_output_source_connection.png)

###### Columns
![Output Source Columns](img/hub_output_source_columns.png)

##### Lookup

###### Connection
![Output Lookup Connection](img/hub_output_lookup_connection.png)

###### Columns
![Output Lookup Columns](img/hub_output_lookup_columns.png)

###### Advanced
![Output Lookup Advanced](img/hub_output_lookup_advanced.png)

##### Destination

###### Connection Manager
![Output Destination Connection Manager](img/hub_output_destination_connection.png)

###### Mappings
![Output Destination Mappings](img/hub_output_destination_mapping.png)