# Informatica PowerCenter - Simple staging example

## Model
Once again, for this example the [Source model](../Model/Source_model) is used as a model input.

## Template
As a template a simple mapping is created that copies the data from source to stage table, adding only a stagedatetime field populated with the session start time.

### Source definition
First a source definition is defined. Note the section annotations in the table's and column's description field, this serves as a section marker for CrossGenerate. The config file (see further down in this example) is used to specify the desired processing of this section.

_Source definition (1/2)_

![Source1.PNG](img/Source1-0f2fba68-fe43-4da6-b7b5-e029588adee9.PNG)

_Source definition (2/2)_

![Source2.PNG](img/Source2-589d1a1b-5377-441b-8f33-7adb69593021.PNG)

### Target definition
Similar to the source, a target is defined

_Target definition (1/2)_

![target1.PNG](img/target1-04f745d3-6afd-48db-baa9-f265760cafbc.PNG)
_Target definition (2/2)_

![Target2.PNG](img/Target2-43dd0b3a-1f3f-4f7f-b5a5-f6274199f3e8.PNG)

### With source and target defined, a mapping can be constructed:
_Mapping (1/4)_

![mapping2.PNG](img/mapping2-37e31f63-8978-4d07-b1ba-0531facc75a4.PNG)

_Mapping (2/4)_

![mapping1.PNG](img/mapping1-710db52f-fd50-4e51-852e-ceece537be02.PNG)

_Mapping (3/4)_

![mapping_sq.PNG](img/mapping_sq-da64996a-a7cc-4a89-a06c-af0d67033576.PNG)

_Mapping (4/4)_

![mapping_exp.PNG](img/mapping_exp-f7deab39-a9f9-4d27-a8ed-6a1df36dbc75.PNG)

### Template exported to XML
The template exported to XML becomes:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE POWERMART SYSTEM "powrmart.dtd">
<POWERMART CREATION_DATE="02/16/2018 20:53:52" REPOSITORY_VERSION="186.95">
<REPOSITORY NAME="REP_REFEC_DEV_01" VERSION="186" CODEPAGE="UTF-8" DATABASETYPE="Oracle">
<FOLDER NAME="Sandbox" GROUP="" OWNER="ottenw" SHARED="NOTSHARED" DESCRIPTION="For testing/prototyping purposes" PERMISSIONS="rwx---r--" UUID="af3f5d20-55dd-47cd-aed9-b6523741ed17">
    <SOURCE BUSINESSNAME ="" DATABASETYPE ="Oracle" DBDNAME ="system_name" DESCRIPTION ="@XGenSection(name=&quot;Entity&quot;)" NAME ="entity_name" OBJECTVERSION ="1" OWNERNAME ="" VERSIONNUMBER ="1">
        <SOURCEFIELD BUSINESSNAME ="" DATATYPE ="varchar2" DESCRIPTION ="@XGenSection(name=&quot;Attribute&quot;)" FIELDNUMBER ="1" FIELDPROPERTY ="0" FIELDTYPE ="ELEMITEM" HIDDEN ="NO" KEYTYPE ="NOT A KEY" LENGTH ="0" LEVEL ="0" NAME ="attribute_name" NULLABLE ="NULL" OCCURS ="0" OFFSET ="0" PHYSICALLENGTH ="10" PHYSICALOFFSET ="0" PICTURETEXT ="" PRECISION ="10" SCALE ="0" USAGE_FLAGS =""/>
    </SOURCE>
    <TARGET BUSINESSNAME ="" CONSTRAINT ="" DATABASETYPE ="Oracle" DESCRIPTION ="@XGenSection(name=&quot;Entity&quot;)" NAME ="entity_name" OBJECTVERSION ="1" TABLEOPTIONS ="" VERSIONNUMBER ="1">
        <TARGETFIELD BUSINESSNAME ="" DATATYPE ="varchar2" DESCRIPTION ="@XGenSection(name=&quot;Attribute&quot;)" FIELDNUMBER ="1" KEYTYPE ="NOT A KEY" NAME ="attribute_name" NULLABLE ="NULL" PICTURETEXT ="" PRECISION ="10" SCALE ="0"/>
        <TARGETFIELD BUSINESSNAME ="" DATATYPE ="timestamp" DESCRIPTION ="" FIELDNUMBER ="2" KEYTYPE ="NOT A KEY" NAME ="StageDateTime" NULLABLE ="NULL" PICTURETEXT ="" PRECISION ="26" SCALE ="6"/>
    </TARGET>
    <MAPPING DESCRIPTION ="@XgenSection(name=&quot;stgMapping&quot;)" ISVALID ="YES" NAME ="stg_load_system_name_entity_name" OBJECTVERSION ="1" VERSIONNUMBER ="1">
        <TRANSFORMATION DESCRIPTION ="" NAME ="SQ_entity_name" OBJECTVERSION ="1" REUSABLE ="NO" TYPE ="Source Qualifier" VERSIONNUMBER ="1">
            <TRANSFORMFIELD DATATYPE ="string" DEFAULTVALUE ="" DESCRIPTION ="@XGenSection(name=&quot;Attribute&quot;)" NAME ="attribute_name" PICTURETEXT ="" PORTTYPE ="INPUT/OUTPUT" PRECISION ="10" SCALE ="0"/>
            <TABLEATTRIBUTE NAME ="Sql Query" VALUE =""/>
            <TABLEATTRIBUTE NAME ="User Defined Join" VALUE =""/>
            <TABLEATTRIBUTE NAME ="Source Filter" VALUE =""/>
            <TABLEATTRIBUTE NAME ="Number Of Sorted Ports" VALUE ="0"/>
            <TABLEATTRIBUTE NAME ="Tracing Level" VALUE ="Normal"/>
            <TABLEATTRIBUTE NAME ="Select Distinct" VALUE ="NO"/>
            <TABLEATTRIBUTE NAME ="Is Partitionable" VALUE ="NO"/>
            <TABLEATTRIBUTE NAME ="Pre SQL" VALUE =""/>
            <TABLEATTRIBUTE NAME ="Post SQL" VALUE =""/>
            <TABLEATTRIBUTE NAME ="Output is deterministic" VALUE ="NO"/>
            <TABLEATTRIBUTE NAME ="Output is repeatable" VALUE ="Never"/>
        </TRANSFORMATION>
        <TRANSFORMATION DESCRIPTION ="" NAME ="EXPTRANS" OBJECTVERSION ="1" REUSABLE ="NO" TYPE ="Expression" VERSIONNUMBER ="1">
            <TRANSFORMFIELD DATATYPE ="string" DEFAULTVALUE ="" DESCRIPTION ="@XGenSection(name=&quot;Attribute&quot;)" EXPRESSION ="attribute_name" EXPRESSIONTYPE ="GENERAL" NAME ="attribute_name" PICTURETEXT ="" PORTTYPE ="INPUT/OUTPUT" PRECISION ="10" SCALE ="0"/>
            <TRANSFORMFIELD DATATYPE ="date/time" DEFAULTVALUE ="ERROR(&apos;transformation error&apos;)" DESCRIPTION ="" EXPRESSION ="sessstarttime" EXPRESSIONTYPE ="GENERAL" NAME ="StageDateTime" PICTURETEXT ="" PORTTYPE ="OUTPUT" PRECISION ="29" SCALE ="9"/>
            <TABLEATTRIBUTE NAME ="Tracing Level" VALUE ="Normal"/>
        </TRANSFORMATION>
        <INSTANCE DESCRIPTION ="" NAME ="entity_name1" TRANSFORMATION_NAME ="entity_name" TRANSFORMATION_TYPE ="Target Definition" TYPE ="TARGET"/>
        <INSTANCE DBDNAME ="system_name" DESCRIPTION ="" NAME ="entity_name" TRANSFORMATION_NAME ="entity_name" TRANSFORMATION_TYPE ="Source Definition" TYPE ="SOURCE"/>
        <INSTANCE DESCRIPTION ="" NAME ="SQ_entity_name" REUSABLE ="NO" TRANSFORMATION_NAME ="SQ_entity_name" TRANSFORMATION_TYPE ="Source Qualifier" TYPE ="TRANSFORMATION">
            <ASSOCIATED_SOURCE_INSTANCE NAME ="entity_name"/>
        </INSTANCE>
        <INSTANCE DESCRIPTION ="" NAME ="EXPTRANS" REUSABLE ="NO" TRANSFORMATION_NAME ="EXPTRANS" TRANSFORMATION_TYPE ="Expression" TYPE ="TRANSFORMATION"/>
        <CONNECTOR FROMFIELD ="attribute_name" FROMINSTANCE ="EXPTRANS" FROMINSTANCETYPE ="Expression" TOFIELD ="attribute_name" TOINSTANCE ="entity_name1" TOINSTANCETYPE ="Target Definition"/>
        <CONNECTOR FROMFIELD ="StageDateTime" FROMINSTANCE ="EXPTRANS" FROMINSTANCETYPE ="Expression" TOFIELD ="StageDateTime" TOINSTANCE ="entity_name1" TOINSTANCETYPE ="Target Definition"/>
        <CONNECTOR FROMFIELD ="attribute_name" FROMINSTANCE ="entity_name" FROMINSTANCETYPE ="Source Definition" TOFIELD ="attribute_name" TOINSTANCE ="SQ_entity_name" TOINSTANCETYPE ="Source Qualifier"/>
        <CONNECTOR FROMFIELD ="attribute_name" FROMINSTANCE ="SQ_entity_name" FROMINSTANCETYPE ="Source Qualifier" TOFIELD ="attribute_name" TOINSTANCE ="EXPTRANS" TOINSTANCETYPE ="Expression"/>
        <TARGETLOADORDER ORDER ="1" TARGETINSTANCE ="entity_name1"/>
        <ERPINFO/>
    </MAPPING>
</FOLDER>
</REPOSITORY>
</POWERMART>
```

## Config
```xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Includes>
    <Include src="../someotherconfig.xml"/>
    <Include src="../otherconfig.xml"/>
  </Includes>
  <App/>    
  <Model>    
    <AttributeInjections>
      <AttributeInjection modelXPath="//Attribute[@datatype='varchar']" targetAttribute="etldatatype" targetValue="string"/>
      <AttributeInjection modelXPath="//Attribute[@datatype='nvarchar']" targetAttribute="etldatatype" targetValue="nstring"/>
    </AttributeInjections>
  </Model>
  <Template rootSectionName="system"> 
    <FileFormat type="Informatica_PowerCenter" currentAccessor="_" childAccessor="$" templateType="xml" singleLineCommentPrefix="--" _multiLineCommentPrefix="/*" multiLineCommentSuffix="*/" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Sections>
      <Section elementXpath="CONNECTOR[@FROMFIELD='attribute_name']" name="connector"/> 
    </Sections>
    <PlaceholderInjections>
      <!--  attribute injection can either be related to other attribute in the same element that already contains a placeholder or unrelated  -->
      <!--  targetValue should contain the accessor, scope is determined by the containing section -->
      <!--  scope defines at what level placeholder will be resolved, can be either current or child so on a section that is on attribute level
      datatype with curent scope will result in attribute_datatype -->
      <PlaceholderInjection templateXPath="//SOURCEFIELD/@DATATYPE" modelNode="datatype" scope="current" />
      <PlaceholderInjection templateXPath="//TRANSFORMFIELD/@DATATYPE"  modelNode="etldatatype" scope="current" />
    </PlaceholderInjections>
  </Template>
  <Binding>
    <SectionModelBinding section="system" modelXPath = "/System" placeholderName="system">
      <SectionModelBinding section="Entity" modelXPath="System/Entities/Entity[@generate='true']" placeholderName="Entity">
        <Placeholders>
          <!-- For the placeholder the modelXPath is relative to its section model xpath. -->
          <Placeholder name="System" modelXPath="../.." />                 
        </Placeholders>
        <SectionModelBinding section="Attribute" modelXPath="System/Entities/Entity[@generate='true']/Attribute" placeholderName="Attribute">
          <Placeholders>
            <!-- For the placeholder the modelXPath is relative to its section model xpath. -->
            <Placeholder name="System" modelXPath="../../../../" />
            <Placeholder name="Entity" modelXPath="../../" />                 
          </Placeholders>    
        </SectionModelBinding>        
       </SectionModelBinding>             
       <SectionModelBinding section="stgMapping" modelXPath="System/Entities/Entity[@generate='true']" placeholderName="Entity">
        <Placeholders>
          <!-- For the placeholder the modelXPath is relative to its section model xpath. -->
          <Placeholder name="System" modelXPath="../.." />                 
        </Placeholders>
        <SectionModelBinding section="Attribute" modelXPath="System/Entities/Entity[@generate='true']/Attribute" placeholderName="Attribute">
          <Placeholders>
            <!-- For the placeholder the modelXPath is relative to its section model xpath. -->
            <Placeholder name="System" modelXPath="../../../../" />
            <Placeholder name="Entity" modelXPath="../../" />                 
          </Placeholders>      
        </SectionModelBinding>
        <SectionModelBinding section="connector" modelXPath="System/Entities/Entity[@generate='true']/Attribute" placeholderName="Attribute">
          <Placeholders>
            <!-- For the placeholder the modelXPath is relative to its section model xpath. -->
            <Placeholder name="System" modelXPath="../../../../" />
            <Placeholder name="Entity" modelXPath="../../" />                 
          </Placeholders>      
        </SectionModelBinding>
      </SectionModelBinding>  
    </SectionModelBinding>    
  </Binding>
</XGenConfig>
```

## Output

!!! todo
    Describe the output.
