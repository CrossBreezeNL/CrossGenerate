# Model

For CrossGenerate the model can be seen as the functional specification of the software that needs to be generated. A model can contain several types of information, such as:

- The target datamodel to be generated (tables, columns, relations) 
- Data mappings from source to target models 
- Business rules that need to be applied

A model can be constructed using different tools.

- By querying the system catalog of a source database for table structures.
- Using modeling tools such as SAP PowerDesigner
- By using a custom made modeling tool, for instance a metadata database or Excel files.

The modeling tool used largely determines how much can be generated using CrossGenerate. A modeling tool that can specify data mappings and business rules provides for a much higher degree of automation than a modeling tool or approach that only specifies target model structure.

CrossGenerate's only requirement with regards to the modeling tool used is that the model can be provided to CrossGenerate in an XML file.

!!! important
    We currently use [SAP PowerDesigner](https://www.sap.com/products/powerdesigner-data-modeling-tools.html){target=_blank} for modelling, and to extend the standard modelling functionalities we created [PowerDesigner-MDDE-Extension](https://github.com/CrossBreezeNL/PowerDesigner-MDDE-Extension){target=_blank}, including a model XML export which can be used to feed CrossGenerate.
    (These extensions are all for LDM models because these fit the MDDE way of working best. In the past we have been using PDM models and also created an extension for these types of models. These extensions can be found [here](http://powerdesigner.x-breeze.com/){target=_blank}).

## Model splitting

CrossGenerate supports splitting a model into multiple XML files to enable re-use or splitting a large model file into pieces (like with [PowerDeComposer](https://github.com/CrossBreezeNL/PowerDeComposer)). For this the XML feature XML Inclusions is used.
In order to use XML inclusion, the appropriate XML namespace, xmlns:xi="http://www.w3.org/2001/XInclude", should be declared in each XML file that includes another XML file. XML files that are included in a model file can also include other XML files, which enables nested includes of model elements. The path to the XML file being included can be absolute or relative to model file where the include is defined.

The XML Inclusion support works exactly the same as for Config files. Examples of how this can be applied can be found [here](../Config#config-re-use).