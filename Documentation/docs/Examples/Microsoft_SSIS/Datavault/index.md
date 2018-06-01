# Microsoft SSIS - DataVault

This example shows how you can generate a DataVault using CrossGenerate. If you haven't done already, have a look at the [Simple Staging example](../Simple_staging) first to read up on the CrossGenerate template- and config concepts used in this example.

The implementation covers Hubs, Links and Satelites for Hubs and Links. The Hub and Hub-Sat loading package templates are explained in detail in subsequent pages as well as how to create a master package. Since Link and Link-Sat are very similar to Hub and Hub-Sat (with regards to CrossGenerate concepts used) these packages are not explained in separate pages but are included in the [downloadable example](#attachments).


## Model
In this example we will be generating DataVault packages for entities defined in the [DWH model](../Model/DWH_model).


## Template

### Load_Entity_owner_H_Entity_name.dtsx

Please see [Hub load package](Hub_package).

### Load_Entity_owner_HS_Entity_name.dtsx

Please see [Hub-Sat load package](HubSat_package).

### Load_DWH_DataVault.dtsx

Please see [Master package](Master_package).

### Documentation
For documentation on templates, please see [Template](../../../Template).


## Config

Besides the concepts introduced in the [Simple Staging example](../Simple_staging) this config introduces the feature to use text templates within an XML template.
As can be seen in the `TextTemplates` section, a node in the xml template can be marked as text template, which results in the node's context being interpreted similar to a text template. Within the node, text sections can then be defined using the @XGenTextSection annotation. Sections can also be defined in the config by adding `TextSections` to the `TextTemplate` node, as is done in the last `TextTemplate` definition in the config below.


### Full config example for Hub and Hub-Sat
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model>    
    <ModelAttributeInjections>
      <!-- Translate the SQL data type into the SSIS data type and store it in the 'etldatatype' attribute on the attribute elements in the model. -->
      <ModelAttributeInjection modelXPath="//attribute[@datatype='varchar']" targetAttribute="etldatatype" targetValue="str"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='varchar']" targetAttribute="codePage" targetValue="1252"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='nvarchar']" targetAttribute="etldatatype" targetValue="wstr"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='tinyint']" targetAttribute="etldatatype" targetValue="i2"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='smallint']" targetAttribute="etldatatype" targetValue="i2"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='bigint']" targetAttribute="etldatatype" targetValue="i8"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='int']" targetAttribute="etldatatype" targetValue="i4"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='timestamp']" targetAttribute="etldatatype" targetValue="bytes"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='datetime']" targetAttribute="etldatatype" targetValue="dbTimeStamp"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='datetime2']" targetAttribute="etldatatype" targetValue="dbTimeStamp"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='decimal']" targetAttribute="etldatatype" targetValue="numeric"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='bit']" targetAttribute="etldatatype" targetValue="bool"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='uniqueidentifier']" targetAttribute="etldatatype" targetValue="guid"/>
    </ModelAttributeInjections>
  </Model>
  <XmlTemplate rootSectionName="Entity">
    <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="output_per_element" />
    <XmlSections>
      <!-- The input columns of the 'OLE DB Destination Input' need to be repeated for every attribute. -->
      <!-- This element doesn't have a 'Description' attribute, so we need to create this section in this config file. -->
      <!-- This also applies to input columns for a union all -->
      <XmlSection name="KeyAttribute" templateXPath="//input[@name='Lookup Input']/inputColumns/inputColumn[@cachedName='KeyAttribute_name']"/>
      <XmlSection name="NonKeyAttribute" templateXPath="//input[@name='Lookup Input']/inputColumns/inputColumn[@cachedName='NonKeyAttribute_name']"/>
      <XmlSection name="KeyAttribute" templateXPath="//component[@componentClassID='Microsoft.UnionAll']/inputs/input/inputColumns/inputColumn[@cachedName='KeyAttribute_name']"/>
      <XmlSection name="NonKeyAttribute" templateXPath="//component[@componentClassID='Microsoft.UnionAll']/inputs/input/inputColumns/inputColumn[@cachedName='NonKeyAttribute_name']"/>
      <XmlSection name="Attribute" templateXPath="//input[@name='OLE DB Destination Input']/inputColumns/inputColumn[@cachedName='Attribute_name']"/>
    </XmlSections>
    <TemplateAttributeInjections>
      <!-- Inject an attribute for the scale, precision, cachedScale & cachedPrecision on elements where the name is 'KeyAttribute_name'. -->
      <TemplateAttributeInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]" attributeName="scale" attributeValue=""/>
      <TemplateAttributeInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]" attributeName="precision" attributeValue=""/>
      <TemplateAttributeInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]" attributeName="cachedScale" attributeValue=""/>
      <TemplateAttributeInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]" attributeName="cachedPrecision" attributeValue=""/>
    </TemplateAttributeInjections>
    <TemplatePlaceholderInjections>
      <!-- Inject a placeholder for the dataType, length, precision, scale, codePage attributes for the columns. -->
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@dataType"  modelNode="etldatatype" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@length"  modelNode="length" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@precision"  modelNode="precision" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@scale"  modelNode="scale" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@codePage"  modelNode="codePage" scope="current" />
      <!-- Inject a placeholder for the cachedDataType, cachedLength, cachedPrecision, cachedScale, cachedCodePage attributes for the columns. -->
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@cachedDataType" modelNode="etldatatype" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@cachedLength"  modelNode="length" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@cachedPrecision"  modelNode="precision" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@cachedScale"  modelNode="scale" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[(@name='Attribute_name' or @cachedName='Attribute_name' or @name='KeyAttribute_name' or @cachedName='KeyAttribute_name' or @name='NonKeyAttribute_name' or @cachedName='NonKeyAttribute_name')]/@cachedCodepage"  modelNode="codePage" scope="current" />
    </TemplatePlaceholderInjections>
    <TextTemplates>
      <TextTemplate node="//component[@componentClassID='Microsoft.OLEDBSource']/properties/property[@name='SqlCommand']">
        <FileFormat currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
      </TextTemplate>
      <TextTemplate node="//component[@name='LKP Entity_owner H_Entity_name']/properties/property[@name='SqlCommandParam']">
        <FileFormat currentAccessor="_" />
        <TextSections>
          <TextSection name="KeyAttribute" begin="[refTable].[KeyAttribute_name]" end="?" prefix="AND " />
        </TextSections>
      </TextTemplate>
      <TextTemplate node="//component[@name='LKP Entity_owner H_Entity_name']/properties/property[@name='ParameterMap']">
        <FileFormat currentAccessor="_" />
        <TextSections>
          <TextSection name="KeyAttribute" begin="#{" end="};" />
        </TextSections>
      </TextTemplate>
    </TextTemplates>
  </XmlTemplate>
  <Binding>
    <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="Entity">
      <Placeholders>
        <Placeholder name="System" modelXPath="../.." />
      </Placeholders>
      <SectionModelBinding section="Attribute" modelXPath="attributes/attribute" placeholderName="Attribute" />
      <SectionModelBinding section="NonKeyAttribute" modelXPath="attributes/attribute[not(boolean(@primary))]" placeholderName="NonKeyAttribute" />
      <SectionModelBinding section="KeyAttribute" modelXPath="attributes/attribute[boolean(@primary)]" placeholderName="KeyAttribute">
        <Placeholders>
          <Placeholder name="Entity" modelXPath="../.." />
        </Placeholders>
      </SectionModelBinding>
      <SectionModelBinding section="MappedKeyAttribute" modelXPath="mappableObjectMappings/mappableObjectMapping/attributeMappings/attributeMapping[boolean(@targetAttributePrimary)]" placeholderName="MappedKeyAttribute" />
      <SectionModelBinding section="MappedNonKeyAttribute" modelXPath="mappableObjectMappings/mappableObjectMapping/attributeMappings/attributeMapping[not(boolean(@targetAttributePrimary))]" placeholderName="MappedNonKeyAttribute" />
      <SectionModelBinding section="ObjectMapping" modelXPath="mappableObjectMappings/mappableObjectMapping" placeholderName="ObjectMapping" />
    </SectionModelBinding>
  </Binding>
</XGenConfig>
```

### Documentation
For documentation on the configuration, please see [Config](../../../Config).


## Output

### Hub load packages
- Load_BusinessVault_H_Country.dtsx
- Load_BusinessVault_H_Customer.dtsx
- Load_BusinessVault_H_Order.dtsx

Please see [Hub package](Hub_package#output).

### Hub-Sat load packages
- Load_BusinessVault_HS_Country.dtsx
- Load_BusinessVault_HS_Customer.dtsx
- Load_BusinessVault_HS_Order.dtsx

Please see [Hub-Sat package](HubSat_package#output).

### Link load packages
- Load_BusinessVault_L_Order_Customer.dtsx

### Link-Sat load packages
- Load_BusinessVault_LS_Order_Customer.dtsx

### Master package
- Load_DWH_DataVault.dtsx

Please see [Master package](Master_package#output).


## Attachments
The entire DataVault example, including SQL and SSIS templates, can be found in the following zip file:

- [CrossGenerate_Example_SSIS_DWH_DataVault.zip](CrossGenerate_Example_SSIS_DWH_DataVault.zip)