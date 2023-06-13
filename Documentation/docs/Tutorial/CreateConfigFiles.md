# Create the config file

After we generated our model-file, we can use it as input for CrossGenerate to generate the database project and the SSIS project. For this we create CrossGenerate config-files to configure the annotations in the template which will be used later in our templates. in this tutorial we need to create the following config files:

- sql_entity
- sql_system
- ssis_entity
- system
- system_text

## sql_entity.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>

  <Model>
    <ModelAttributeInjections>
        <!-- Override the length of the fields where the length is max to 1000. -->
        <ModelAttributeInjection modelXPath="//columns/column[lower-case(@fulldatatype) = 'nvarchar(max)']" targetAttribute="fulldatatype" targetValue="nvarchar(1000)"/>
        <!-- Override the fulldatatype for timestamp and rowversion to binary. -->
        <ModelAttributeInjection modelXPath="//columns/column[@datatype='rowversion' or @datatype='timestamp']" targetAttribute="fulldatatype" targetValue="binary(8)"/>
    </ModelAttributeInjections>
  </Model>

  <TextTemplate rootSectionName="table">
    <FileFormat currentAccessor="_" childAccessor="$" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="output_per_element" />
  </TextTemplate>

  <Binding>
    <SectionModelBinding section="table" modelXPath="/database/tables/table">
        <Placeholders>
            <Placeholder name="system" modelXPath="../.." />
        </Placeholders>
        <SectionModelBinding section="keyAttribute" modelXPath="columns/column[@primary='true']">   
            <Literals>
                <!-- Replace the literal VARCHAR(10) with the value of fulldatatype for the attribute from the model -->
                <Literal literal="VARCHAR(10)" modelXPath="./@fulldatatype"/>
            </Literals>
            <Placeholders>
                <Placeholder name="table" modelXPath="../.." />
            </Placeholders>
        </SectionModelBinding>
        <SectionModelBinding section="nonKeyAttribute" modelXPath="columns/column[not(@primary='true')]">
            <Literals>
                <Literal literal="VARCHAR(10)" modelXPath="./@fulldatatype"/>
            </Literals>
            <Placeholders>
                <Placeholder name="table" modelXPath="../.." />
            </Placeholders>
        </SectionModelBinding>
    </SectionModelBinding>
  </Binding>
  
</XGenConfig>
```

## system.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
    <Model>
        <ModelAttributeInjections>
            <!-- Create a new model attribute named packageId and populate it with the internalId, enclosed in brackets. -->
            <ModelAttributeInjection modelXPath="//*[@internalId]" targetAttribute="packageId" targetXPath="concat('{', @internalId, '}')"/>
        </ModelAttributeInjections>
    </Model>
    <XmlTemplate rootSectionName="system">
        <FileFormat currentAccessor="_" commentNodeXPath="@description" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="output_per_element" />
        <XmlSections>
            <XmlSection name="table" templateXPath="//Packages/Package[@Name='Staging_load_table_name.dtsx']" />
            <XmlSection name="table" templateXPath="//PackageMetaData[@Name='Staging_load_table_name.dtsx']" />
            <XmlSection name="table" templateXPath="//*[contains(@Include, 'table_')]" />      
        </XmlSections>
        <TemplatePlaceholderInjections>
            <!-- Inject a placeholder for the ID property for the packages. -->
            <TemplatePlaceholderInjection templateXPath="//PackageMetaData[@Name='Staging_load_table_name.dtsx']/Properties/Property[@Name='ID']" modelNode="packageId" scope="current" />             
        </TemplatePlaceholderInjections>
    </XmlTemplate>
    <Binding>
        <SectionModelBinding section="system" modelXPath="/database">
            <SectionModelBinding section="table" modelXPath="tables/table"/>
        </SectionModelBinding>
    </Binding>
</XGenConfig>
```

## Attachments
The entire tutorial, including all config-files and templates, can be found in the following zip file:

- [Tutorial.zip](CrossGenerate_Tutorial.zip)

Note: The config files can be found in the 'Config' folder in the attachment.