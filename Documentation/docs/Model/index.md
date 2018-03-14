# Model

For CrossGenerate the model can be seen as the functional specification of the software that needs to be generated. A model can contain several types of information, such as:

- The target datamodel to be generated (tables, columns, relations) 
- Data mappings from source to target models 
- Business rules that need to be applied

A model can be constructed using different tools. 

- By querying the system catalog of a source database for table structures.
- Using modeling tools such as Sap/Sybase PowerDesigner
- By using a custom made modeling tool, for instance a metadata database or Excel files.

The modeling tool used largely determines how much can be generated using CrossGenerate. A modeling tool that can specify data mappings and business rules provides for a much higher degree of automation than a modeling tool or approach that only specifies target model structure.

CrossGenerate's only requirement with regards to the modeling tool used is that the model can be provided to CrossGenerate in an XML file.