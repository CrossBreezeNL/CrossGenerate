#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SSIS

Feature: SSIS
  This feature file contains the features when using SSIS in CrossGenerate templates
  
  Scenario: Simple staging package
  
  Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <modeldefinition>
        <system name="ExampleSource">
          <mappableObjects>
            <entity name="Order">
              <attributes>
                <attribute name="Id" datatype="int" fulldatatype="int" ordinal="1" primary="true" required="true" />
                <attribute name="OrderDate" datatype="datetime" fulldatatype="datetime" ordinal="2" />
                <attribute name="OrderNumber" datatype="varchar" length="50" fulldatatype="varchar(50)" ordinal="3" />
                <attribute name="CustomerId" datatype="int" fulldatatype="int" ordinal="4" />
                <attribute name="TotalAmount" datatype="decimal" precision="12" scale="2" fulldatatype="decimal(12,2)" ordinal="5" />
              </attributes>  
              <keyAttributes>
                <keyAttribute attributeName="Id" />
              </keyAttributes>
              <references>
                <reference name="Order_Customer" parentObjectName="Customer" parentOwner="ExampleSchema" parentReferenceName="Customer" childObjectName="Order" childReferenceName="Order">
                  <referencedAttributes>
                    <referencedAttribute parentObjectAttributeName="Id" childObjectAttributeName="CustomerId"  />
                  </referencedAttributes>
                </reference>
              </references>
            </entity>
            <entity name="Customer" owner="ExampleSchema" stereotype="ExampleTableType">
              <attributes>
                <attribute name="Id" datatype="int" fulldatatype="int" ordinal="1" primary="true" required="true" />
                <attribute name="FirstName" datatype="varchar" length="50" fulldatatype="varchar(50)" ordinal="2" />
                <attribute name="LastName" datatype="varchar" length="100" fulldatatype="varchar(100)" ordinal="3" />
                <attribute name="City" datatype="varchar" length="50" fulldatatype="varchar(50)" ordinal="4" />
                <attribute name="Country" datatype="varchar" length="3" fulldatatype="varchar(3)" ordinal="5" />
                <attribute name="Phone" datatype="varchar" length="20" fulldatatype="varchar(20)" ordinal="6" />
              </attributes>  
              <keyAttributes>
                <keyAttribute attributeName="Id" />
              </keyAttributes>
            </entity>
          </mappableObjects>
        </system>
      </modeldefinition>
      """

	  And the following template named stg_load_system_name_entity_name.dtsx:
	  """﻿﻿<?xml version="1.0"?>
<DTS:Executable xmlns:DTS="www.microsoft.com/SqlServer/Dts"
  DTS:refId="Package"
  DTS:CreationDate="2/16/2018 10:15:16 PM"
  DTS:CreationName="Microsoft.Package"
  DTS:CreatorComputerName="DESKTOP-EC40127"
  DTS:CreatorName="DESKTOP-EC40127\Willem"
  DTS:Description="@XGenSection(name=&quot;stgPackage&quot;)"
  DTS:DTSID="{FEE0C51A-D375-41C3-8DFB-BAACEB3D0087}"
  DTS:ExecutableType="Microsoft.Package"
  DTS:LastModifiedProductVersion="13.0.4001.0"
  DTS:LocaleID="1033"
  DTS:ObjectName="stg_load_system_name_entity_name"
  DTS:PackageType="5"
  DTS:VersionBuild="10"
  DTS:VersionGUID="{245421C5-DFD7-4DC3-BAB4-E124415ED498}">
  <DTS:Property
    DTS:Name="PackageFormatVersion">8</DTS:Property>
  <DTS:Variables />
  <DTS:Executables>
    <DTS:Executable
      DTS:refId="Package\Load entity_name"
      DTS:CreationName="Microsoft.Pipeline"
      DTS:Description="Data Flow Task"
      DTS:DTSID="{13F2099E-7D26-4671-8964-537D60997A48}"
      DTS:ExecutableType="Microsoft.Pipeline"
      DTS:LocaleID="-1"
      DTS:ObjectName="Load entity_name">
      <DTS:Variables />
      <DTS:ObjectData>
        <pipeline
          version="1">
          <components>
            <component
              refId="Package\Load entity_name\Add StageDateTime"
              componentClassID="Microsoft.DerivedColumn"
              contactInfo="Derived Column;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;0"
              description="Creates new column values by applying expressions to transformation input columns. Create new columns or overwrite existing ones. For example, concatenate the values from the 'first name' and 'last name' column to make a 'full name' column."
              name="Add StageDateTime"
              usesDispositions="true">
              <inputs>
                <input
                  refId="Package\Load entity_name\Add StageDateTime.Inputs[Derived Column Input]"
                  description="Input to the Derived Column Transformation"
                  name="Derived Column Input">
                  <externalMetadataColumns />
                </input>
              </inputs>
              <outputs>
                <output
                  refId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Output]"
                  description="Default Output of the Derived Column Transformation"
                  exclusionGroup="1"
                  name="Derived Column Output"
                  synchronousInputId="Package\Load entity_name\Add StageDateTime.Inputs[Derived Column Input]">
                  <outputColumns>
                    <outputColumn
                      refId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
                      dataType="dbTimeStamp"
                      errorOrTruncationOperation="Computation"
                      errorRowDisposition="FailComponent"
                      lineageId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
                      name="StageDateTime"
                      truncationRowDisposition="FailComponent">
                      <properties>
                        <property
                          containsID="true"
                          dataType="System.String"
                          description="Derived Column Expression"
                          name="Expression">[GETDATE]()</property>
                        <property
                          containsID="true"
                          dataType="System.String"
                          description="Derived Column Friendly Expression"
                          expressionType="Notify"
                          name="FriendlyExpression">GETDATE()</property>
                      </properties>
                    </outputColumn>
                  </outputColumns>
                  <externalMetadataColumns />
                </output>
                <output
                  refId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Error Output]"
                  description="Error Output of the Derived Column Transformation"
                  exclusionGroup="1"
                  isErrorOut="true"
                  name="Derived Column Error Output"
                  synchronousInputId="Package\Load entity_name\Add StageDateTime.Inputs[Derived Column Input]">
                  <outputColumns>
                    <outputColumn
                      refId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorCode]"
                      dataType="i4"
                      lineageId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorCode]"
                      name="ErrorCode"
                      specialFlags="1" />
                    <outputColumn
                      refId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorColumn]"
                      dataType="i4"
                      lineageId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorColumn]"
                      name="ErrorColumn"
                      specialFlags="2" />
                  </outputColumns>
                  <externalMetadataColumns />
                </output>
              </outputs>
            </component>
            <component
              refId="Package\Load entity_name\Read entity_name"
              componentClassID="Microsoft.OLEDBSource"
              contactInfo="OLE DB Source;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;7"
              description="OLE DB Source"
              name="Read entity_name"
              usesDispositions="true"
              version="7">
              <properties>
                <property
                  dataType="System.Int32"
                  description="The number of seconds before a command times out.  A value of 0 indicates an infinite time-out."
                  name="CommandTimeout">0</property>
                <property
                  dataType="System.String"
                  description="Specifies the name of the database object used to open a rowset."
                  name="OpenRowset">[dbo].[entity_name]</property>
                <property
                  dataType="System.String"
                  description="Specifies the variable that contains the name of the database object used to open a rowset."
                  name="OpenRowsetVariable"></property>
                <property
                  dataType="System.String"
                  description="The SQL command to be executed."
                  name="SqlCommand"
                  UITypeEditor="Microsoft.DataTransformationServices.Controls.ModalMultilineStringEditor"></property>
                <property
                  dataType="System.String"
                  description="The variable that contains the SQL command to be executed."
                  name="SqlCommandVariable"></property>
                <property
                  dataType="System.Int32"
                  description="Specifies the column code page to use when code page information is unavailable from the data source."
                  name="DefaultCodePage">1252</property>
                <property
                  dataType="System.Boolean"
                  description="Forces the use of the DefaultCodePage property value when describing character data."
                  name="AlwaysUseDefaultCodePage">false</property>
                <property
                  dataType="System.Int32"
                  description="Specifies the mode used to access the database."
                  name="AccessMode"
                  typeConverter="AccessMode">0</property>
                <property
                  dataType="System.String"
                  description="The mappings between the parameters in the SQL command and variables."
                  name="ParameterMapping"></property>
              </properties>
              <connections>
                <connection
                  refId="Package\Load entity_name\Read entity_name.Connections[OleDbConnection]"
                  connectionManagerID="{F43489D1-61D9-4682-898D-5398C303A955}:external"
                  connectionManagerRefId="Project.ConnectionManagers[Source]"
                  description="The OLE DB runtime connection used to access the database."
                  name="OleDbConnection" />
              </connections>
              <outputs>
                <output
                  refId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Output]"
                  name="OLE DB Source Output">
                  <outputColumns>
                    <outputColumn
                      refId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Output].Columns[attribute_name]"
                      codePage="1252"
                      dataType="str"
                      description="@XGenSection(name=&quot;Attribute&quot;)"
                      errorOrTruncationOperation="Conversion"
                      errorRowDisposition="FailComponent"
                      externalMetadataColumnId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Output].ExternalColumns[attribute_name]"
                      length="10"
                      lineageId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Output].Columns[attribute_name]"
                      name="attribute_name"
                      truncationRowDisposition="FailComponent" />
                  </outputColumns>
                  <externalMetadataColumns
                    isUsed="True">
                    <externalMetadataColumn
                      refId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Output].ExternalColumns[attribute_name]"
                      codePage="1252"
                      dataType="str"
                      description="@XGenSection(name=&quot;Attribute&quot;)"
                      length="10"
                      name="attribute_name" />
                  </externalMetadataColumns>
                </output>
                <output
                  refId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Error Output]"
                  isErrorOut="true"
                  name="OLE DB Source Error Output">
                  <outputColumns>
                    <outputColumn
                      refId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Error Output].Columns[attribute_name]"
                      codePage="1252"
                      dataType="str"
                      description="@XGenSection(name=&quot;Attribute&quot;)"
                      length="10"
                      lineageId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Error Output].Columns[attribute_name]"
                      name="attribute_name" />
                    <outputColumn
                      refId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Error Output].Columns[ErrorCode]"
                      dataType="i4"
                      lineageId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Error Output].Columns[ErrorCode]"
                      name="ErrorCode"
                      specialFlags="1" />
                    <outputColumn
                      refId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Error Output].Columns[ErrorColumn]"
                      dataType="i4"
                      lineageId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Error Output].Columns[ErrorColumn]"
                      name="ErrorColumn"
                      specialFlags="2" />
                  </outputColumns>
                  <externalMetadataColumns />
                </output>
              </outputs>
            </component>
            <component
              refId="Package\Load entity_name\Write to system_name entity_name"
              componentClassID="Microsoft.OLEDBDestination"
              contactInfo="OLE DB Destination;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;4"
              description="OLE DB Destination"
              name="Write to system_name entity_name"
              usesDispositions="true"
              version="4">
              <properties>
                <property
                  dataType="System.Int32"
                  description="The number of seconds before a command times out.  A value of 0 indicates an infinite time-out."
                  name="CommandTimeout">0</property>
                <property
                  dataType="System.String"
                  description="Specifies the name of the database object used to open a rowset."
                  name="OpenRowset">[system_name].[entity_name]</property>
                <property
                  dataType="System.String"
                  description="Specifies the variable that contains the name of the database object used to open a rowset."
                  name="OpenRowsetVariable"></property>
                <property
                  dataType="System.String"
                  description="The SQL command to be executed."
                  name="SqlCommand"
                  UITypeEditor="Microsoft.DataTransformationServices.Controls.ModalMultilineStringEditor"></property>
                <property
                  dataType="System.Int32"
                  description="Specifies the column code page to use when code page information is unavailable from the data source."
                  name="DefaultCodePage">1252</property>
                <property
                  dataType="System.Boolean"
                  description="Forces the use of the DefaultCodePage property value when describing character data."
                  name="AlwaysUseDefaultCodePage">false</property>
                <property
                  dataType="System.Int32"
                  description="Specifies the mode used to access the database."
                  name="AccessMode"
                  typeConverter="AccessMode">3</property>
                <property
                  dataType="System.Boolean"
                  description="Indicates whether the values supplied for identity columns will be copied to the destination. If false, values for identity columns will be auto-generated at the destination. Applies only if fast load is turned on."
                  name="FastLoadKeepIdentity">false</property>
                <property
                  dataType="System.Boolean"
                  description="Indicates whether the columns containing null will have null inserted in the destination. If false, columns containing null will have their default values inserted at the destination. Applies only if fast load is turned on."
                  name="FastLoadKeepNulls">false</property>
                <property
                  dataType="System.String"
                  description="Specifies options to be used with fast load.  Applies only if fast load is turned on."
                  name="FastLoadOptions">TABLOCK,CHECK_CONSTRAINTS</property>
                <property
                  dataType="System.Int32"
                  description="Specifies when commits are issued during data insertion.  A value of 0 specifies that one commit will be issued at the end of data insertion.  Applies only if fast load is turned on."
                  name="FastLoadMaxInsertCommitSize">2147483647</property>
              </properties>
              <connections>
                <connection
                  refId="Package\Load entity_name\Write to system_name entity_name.Connections[OleDbConnection]"
                  connectionManagerID="{84C8FEB0-CB1D-443C-BF4D-DFE07E7C0215}:external"
                  connectionManagerRefId="Project.ConnectionManagers[Staging]"
                  description="The OLE DB runtime connection used to access the database."
                  name="OleDbConnection" />
              </connections>
              <inputs>
                <input
                  refId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input]"
                  errorOrTruncationOperation="Insert"
                  errorRowDisposition="FailComponent"
                  hasSideEffects="true"
                  name="OLE DB Destination Input">
                  <inputColumns>
                    <inputColumn
                      refId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input].Columns[attribute_name]"
                      cachedCodepage="1252"
                      cachedDataType="str"
                      cachedLength="10"
                      cachedName="attribute_name"
                      externalMetadataColumnId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input].ExternalColumns[attribute_name]"
                      lineageId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Output].Columns[attribute_name]" />
                    <inputColumn
                      refId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input].Columns[StageDateTime]"
                      cachedDataType="dbTimeStamp"
                      cachedName="StageDateTime"
                      externalMetadataColumnId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input].ExternalColumns[StageDateTime]"
                      lineageId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]" />
                  </inputColumns>
                  <externalMetadataColumns
                    isUsed="True">
                    <externalMetadataColumn
                      refId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input].ExternalColumns[attribute_name]"
                      codePage="1252"
                      dataType="str"
                      description="@XGenSection(name=&quot;Attribute&quot;)"
                      length="10"
                      name="attribute_name" />
                    <externalMetadataColumn
                      refId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input].ExternalColumns[StageDateTime]"
                      dataType="wstr"
                      length="22"
                      name="StageDateTime" />
                  </externalMetadataColumns>
                </input>
              </inputs>
              <outputs>
                <output
                  refId="Package\Load entity_name\Write to system_name entity_name.Outputs[OLE DB Destination Error Output]"
                  exclusionGroup="1"
                  isErrorOut="true"
                  name="OLE DB Destination Error Output"
                  synchronousInputId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input]">
                  <outputColumns>
                    <outputColumn
                      refId="Package\Load entity_name\Write to system_name entity_name.Outputs[OLE DB Destination Error Output].Columns[ErrorCode]"
                      dataType="i4"
                      lineageId="Package\Load entity_name\Write to system_name entity_name.Outputs[OLE DB Destination Error Output].Columns[ErrorCode]"
                      name="ErrorCode"
                      specialFlags="1" />
                    <outputColumn
                      refId="Package\Load entity_name\Write to system_name entity_name.Outputs[OLE DB Destination Error Output].Columns[ErrorColumn]"
                      dataType="i4"
                      lineageId="Package\Load entity_name\Write to system_name entity_name.Outputs[OLE DB Destination Error Output].Columns[ErrorColumn]"
                      name="ErrorColumn"
                      specialFlags="2" />
                  </outputColumns>
                  <externalMetadataColumns />
                </output>
              </outputs>
            </component>
          </components>
          <paths>
            <path
              refId="Package\Load entity_name.Paths[Derived Column Output]"
              endId="Package\Load entity_name\Write to system_name entity_name.Inputs[OLE DB Destination Input]"
              name="Derived Column Output"
              startId="Package\Load entity_name\Add StageDateTime.Outputs[Derived Column Output]" />
            <path
              refId="Package\Load entity_name.Paths[OLE DB Source Output]"
              endId="Package\Load entity_name\Add StageDateTime.Inputs[Derived Column Input]"
              name="OLE DB Source Output"
              startId="Package\Load entity_name\Read entity_name.Outputs[OLE DB Source Output]" />
          </paths>
        </pipeline>
      </DTS:ObjectData>
    </DTS:Executable>
  </DTS:Executables>
  <DTS:DesignTimeProperties><![CDATA[<?xml version="1.0"?>
<!--This CDATA section contains the layout information of the package. The section includes information such as (x,y) coordinates, width, and height.-->
<!--If you manually edit this section and make a mistake, you can delete it. -->
<!--The package will still be able to load normally but the previous layout information will be lost and the designer will automatically re-arrange the elements on the design surface.-->
<Objects
  Version="8">
  <!--Each node below will contain properties that do not affect runtime behavior.-->
  <Package
    design-time-name="Package">
    <LayoutInfo>
      <GraphLayout
        Capacity="4" xmlns="clr-namespace:Microsoft.SqlServer.IntegrationServices.Designer.Model.Serialization;assembly=Microsoft.SqlServer.IntegrationServices.Graph">
        <NodeLayout
          Size="163,42"
          Id="Package\Load entity_name"
          TopLeft="231.5,189.5" />
      </GraphLayout>
    </LayoutInfo>
  </Package>
  <TaskHost
    design-time-name="Package\Load entity_name">
    <LayoutInfo>
      <GraphLayout
        Capacity="8" xmlns="clr-namespace:Microsoft.SqlServer.IntegrationServices.Designer.Model.Serialization;assembly=Microsoft.SqlServer.IntegrationServices.Graph" xmlns:mssgle="clr-namespace:Microsoft.SqlServer.Graph.LayoutEngine;assembly=Microsoft.SqlServer.Graph" xmlns:assembly="http://schemas.microsoft.com/winfx/2006/xaml">
        <NodeLayout
          Size="164,42"
          Id="Package\Load entity_name\Read entity_name"
          TopLeft="196,56" />
        <NodeLayout
          Size="172,42"
          Id="Package\Load entity_name\Add StageDateTime"
          TopLeft="197,189" />
        <NodeLayout
          Size="247,42"
          Id="Package\Load entity_name\Write to system_name entity_name"
          TopLeft="162,301" />
        <EdgeLayout
          Id="Package\Load entity_name.Paths[OLE DB Source Output]"
          TopLeft="280.5,98">
          <EdgeLayout.Curve>
            <mssgle:Curve
              StartConnector="{assembly:Null}"
              EndConnector="0,91"
              Start="0,0"
              End="0,83.5">
              <mssgle:Curve.Segments>
                <mssgle:SegmentCollection
                  Capacity="5">
                  <mssgle:LineSegment
                    End="0,83.5" />
                </mssgle:SegmentCollection>
              </mssgle:Curve.Segments>
            </mssgle:Curve>
          </EdgeLayout.Curve>
          <EdgeLayout.Labels>
            <EdgeLabelCollection />
          </EdgeLayout.Labels>
        </EdgeLayout>
        <EdgeLayout
          Id="Package\Load entity_name.Paths[Derived Column Output]"
          TopLeft="284.25,231">
          <EdgeLayout.Curve>
            <mssgle:Curve
              StartConnector="{assembly:Null}"
              EndConnector="0,70"
              Start="0,0"
              End="0,62.5">
              <mssgle:Curve.Segments>
                <mssgle:SegmentCollection
                  Capacity="5">
                  <mssgle:LineSegment
                    End="0,62.5" />
                </mssgle:SegmentCollection>
              </mssgle:Curve.Segments>
            </mssgle:Curve>
          </EdgeLayout.Curve>
          <EdgeLayout.Labels>
            <EdgeLabelCollection />
          </EdgeLayout.Labels>
        </EdgeLayout>
      </GraphLayout>
    </LayoutInfo>
  </TaskHost>
  <PipelineComponentMetadata
    design-time-name="Package\Load entity_name\Read entity_name">
    <Properties>
      <Property>
        <Name>DataSourceViewID</Name>
      </Property>
      <Property>
        <Name>TableInfoObjectType</Name>
        <Value
          type="q2:string">Table</Value>
      </Property>
    </Properties>
  </PipelineComponentMetadata>
  <PipelineComponentMetadata
    design-time-name="Package\Load entity_name\Write to system_name entity_name">
    <Properties>
      <Property>
        <Name>DataSourceViewID</Name>
      </Property>
      <Property>
        <Name>TableInfoObjectType</Name>
        <Value
          type="q2:string">Table</Value>
      </Property>
    </Properties>
  </PipelineComponentMetadata>
</Objects>]]></DTS:DesignTimeProperties>
</DTS:Executable>
"""
  
  And the following config:
  """
  <?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model>    
    <ModelAttributeInjections>
      <!-- Translate the SQL data type into the SSIS data type and store it in the 'etldatatype' attribute on the attribute elements in the model. -->
      <ModelAttributeInjection modelXPath="//attribute[@datatype='varchar']" targetAttribute="etldatatype" targetValue="str"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='varchar']" targetAttribute="codePage" targetValue="1252"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='nvarchar']" targetAttribute="etldatatype" targetValue="wstr"/>
      <ModelAttributeInjection modelXPath="//attribute[@datatype='nvarchar']" targetAttribute="codePage" targetValue="1252"/>
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
  <Template rootSectionName="StgPackage">
    <!-- Define the FileFormat, here all attributes with the name 'description' are scanned for annotations. -->
    <FileFormat templateType="xml" currentAccessor="_" commentNodeXPath="@*[lower-case(local-name())='description']" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <!-- Output a SSIS package per element of the root section, so 1 package per entity. -->
    <Output type="output_per_element" />
    <Sections>
      <!-- The input columns of the 'OLE DB Destination Input' need to be repeated for every attribute. -->
      <!-- This element doesn't have a 'Description' attribute, so we need to create this section in this config file. -->
      <Section name="Attribute" templateXPath="//input[@name='OLE DB Destination Input']/inputColumns/inputColumn[@cachedName='attribute_name']"/>
    </Sections>
    <TemplateAttributeInjections>
      <!-- Inject an attribute for the scale, precision, cachedScale & cachedPrecision on elements where the name is 'attribute_name'. -->
      <TemplateAttributeInjection parentNodeXPath="//*[@name='attribute_name']" attributeName="scale" defaultValue=""/>
      <TemplateAttributeInjection parentNodeXPath="//*[@name='attribute_name']" attributeName="precision" defaultValue=""/>
      <TemplateAttributeInjection parentNodeXPath="//*[@name='attribute_name']" attributeName="cachedScale" defaultValue=""/>
      <TemplateAttributeInjection parentNodeXPath="//*[@name='attribute_name']" attributeName="cachedPrecision" defaultValue=""/>
    </TemplateAttributeInjections>
    <TemplatePlaceholderInjections>
      <!-- Inject a placeholder for the dataType, length, precision, scale, codePage attributes for the columns. -->
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@dataType"  modelNode="etldatatype" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@length"  modelNode="length" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@precision"  modelNode="precision" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@scale"  modelNode="scale" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@codePage"  modelNode="codePage" scope="current" />
      <!-- Inject a placeholder for the cachedDataType, cachedLength, cachedPrecision, cachedScale, cachedCodePage attributes for the columns. -->
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@cachedDataType" modelNode="etldatatype" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@cachedLength"  modelNode="length" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@cachedPrecision"  modelNode="precision" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@cachedScale"  modelNode="scale" scope="current" />
      <TemplatePlaceholderInjection templateXPath="//*[@name='attribute_name']/@cachedCodePage"  modelNode="codePage" scope="current" />
    </TemplatePlaceholderInjections>
  </Template>
 <Binding>
   <!-- Create a binding between the 'StgPackage' sections in the template and the 'entity' elements in the model. -->
   <SectionModelBinding section="StgPackage" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
     <Placeholders>
       <!-- Create the 'system' placeholder within the 'StgPackage' section. -->
       <Placeholder name="system" modelXPath="../.." />                 
     </Placeholders>
     <!-- Create a binding between the 'Attribute' sections in the template and the 'attribute' elements in the model. -->
     <SectionModelBinding section="Attribute" modelXPath="attributes/attribute" placeholderName="attribute">
       <Placeholders>
         <!-- Create the 'system' placeholder within the 'Attribute' section. -->
         <Placeholder name="system" modelXPath="../../../.." />
         <!-- Create the 'entity' placeholder within the 'Attribute' section. -->
         <Placeholder name="entity" modelXPath="../.." />                 
       </Placeholders>    
     </SectionModelBinding>        
   </SectionModelBinding>
 </Binding>
</XGenConfig>
""" 
  
    When I run the generator
    
    Then I expect 2 generation result(s)
        
    And an output named file:///C:/CrossGenerate/Output/stg_load_ExampleSource_Order.dtsx with content:
  """
<?xml version="1.0" encoding="UTF-8"?>
<DTS:Executable xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                DTS:CreationDate="2/16/2018 10:15:16 PM"
		                DTS:CreationName="Microsoft.Package"
		                DTS:CreatorComputerName="DESKTOP-EC40127"
		                DTS:CreatorName="DESKTOP-EC40127\Willem"
		                DTS:DTSID="{FEE0C51A-D375-41C3-8DFB-BAACEB3D0087}"
		                DTS:Description=""
		                DTS:ExecutableType="Microsoft.Package"
		                DTS:LastModifiedProductVersion="13.0.4001.0"
		                DTS:LocaleID="1033"
		                DTS:ObjectName="stg_load_ExampleSource_Order"
		                DTS:PackageType="5"
		                DTS:VersionBuild="10"
		                DTS:VersionGUID="{245421C5-DFD7-4DC3-BAB4-E124415ED498}"
		                DTS:refId="Package"><DTS:Property xmlns:DTS="www.microsoft.com/SqlServer/Dts" DTS:Name="PackageFormatVersion">8</DTS:Property><DTS:Variables xmlns:DTS="www.microsoft.com/SqlServer/Dts"/><DTS:Executables xmlns:DTS="www.microsoft.com/SqlServer/Dts"><DTS:Executable xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                DTS:CreationName="Microsoft.Pipeline"
		                DTS:DTSID="{13F2099E-7D26-4671-8964-537D60997A48}"
		                DTS:Description="Data Flow Task"
		                DTS:ExecutableType="Microsoft.Pipeline"
		                DTS:LocaleID="-1"
		                DTS:ObjectName="Load Order"
		                DTS:refId="Package\Load Order"><DTS:Variables xmlns:DTS="www.microsoft.com/SqlServer/Dts"/><DTS:ObjectData xmlns:DTS="www.microsoft.com/SqlServer/Dts"><pipeline version="1"><components><component componentClassID="Microsoft.DerivedColumn"
		           contactInfo="Derived Column;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;0"
		           description="Creates new column values by applying expressions to transformation input columns. Create new columns or overwrite existing ones. For example, concatenate the values from the 'first name' and 'last name' column to make a 'full name' column."
		           name="Add StageDateTime"
		           refId="Package\Load Order\Add StageDateTime"
		           usesDispositions="true"><inputs><input description="Input to the Derived Column Transformation"
		       name="Derived Column Input"
		       refId="Package\Load Order\Add StageDateTime.Inputs[Derived Column Input]"><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></input></inputs><outputs><output description="Default Output of the Derived Column Transformation"
		        exclusionGroup="1"
		        name="Derived Column Output"
		        refId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Output]"
		        synchronousInputId="Package\Load Order\Add StageDateTime.Inputs[Derived Column Input]"><outputColumns><outputColumn dataType="dbTimeStamp"
		              errorOrTruncationOperation="Computation"
		              errorRowDisposition="FailComponent"
		              lineageId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
		              name="StageDateTime"
		              refId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
		              truncationRowDisposition="FailComponent"><properties><property containsID="true"
		          dataType="System.String"
		          description="Derived Column Expression"
		          name="Expression">[GETDATE]()</property><property containsID="true"
		          dataType="System.String"
		          description="Derived Column Friendly Expression"
		          expressionType="Notify"
		          name="FriendlyExpression">GETDATE()</property></properties></outputColumn></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output><output description="Error Output of the Derived Column Transformation"
		        exclusionGroup="1"
		        isErrorOut="true"
		        name="Derived Column Error Output"
		        refId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Error Output]"
		        synchronousInputId="Package\Load Order\Add StageDateTime.Inputs[Derived Column Input]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorCode]"
		              name="ErrorCode"
		              refId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorCode]"
		              specialFlags="1"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorColumn]"
		              name="ErrorColumn"
		              refId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorColumn]"
		              specialFlags="2"/></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output></outputs></component><component componentClassID="Microsoft.OLEDBSource"
		           contactInfo="OLE DB Source;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;7"
		           description="OLE DB Source"
		           name="Read Order"
		           refId="Package\Load Order\Read Order"
		           usesDispositions="true"
		           version="7"><properties><property dataType="System.Int32"
		          description="The number of seconds before a command times out.  A value of 0 indicates an infinite time-out."
		          name="CommandTimeout">0</property><property dataType="System.String"
		          description="Specifies the name of the database object used to open a rowset."
		          name="OpenRowset">[dbo].[Order]</property><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="Specifies the variable that contains the name of the database object used to open a rowset."
		          name="OpenRowsetVariable"/><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          UITypeEditor="Microsoft.DataTransformationServices.Controls.ModalMultilineStringEditor"
		          dataType="System.String"
		          description="The SQL command to be executed."
		          name="SqlCommand"/><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="The variable that contains the SQL command to be executed."
		          name="SqlCommandVariable"/><property dataType="System.Int32"
		          description="Specifies the column code page to use when code page information is unavailable from the data source."
		          name="DefaultCodePage">1252</property><property dataType="System.Boolean"
		          description="Forces the use of the DefaultCodePage property value when describing character data."
		          name="AlwaysUseDefaultCodePage">false</property><property dataType="System.Int32"
		          description="Specifies the mode used to access the database."
		          name="AccessMode"
		          typeConverter="AccessMode">0</property><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="The mappings between the parameters in the SQL command and variables."
		          name="ParameterMapping"/></properties><connections><connection xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		            connectionManagerID="{F43489D1-61D9-4682-898D-5398C303A955}:external"
		            connectionManagerRefId="Project.ConnectionManagers[Source]"
		            description="The OLE DB runtime connection used to access the database."
		            name="OleDbConnection"
		            refId="Package\Load Order\Read Order.Connections[OleDbConnection]"/></connections><outputs><output name="OLE DB Source Output"
		        refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[Id]"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[Id]"
		              name="Id"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[Id]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="dbTimeStamp"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[OrderDate]"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[OrderDate]"
		              name="OrderDate"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[OrderDate]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[OrderNumber]"
		              length="50"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[OrderNumber]"
		              name="OrderNumber"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[OrderNumber]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[CustomerId]"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[CustomerId]"
		              name="CustomerId"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[CustomerId]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              cachedPrecision="12"
		              cachedScale="2"
		              dataType="numeric"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[TotalAmount]"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[TotalAmount]"
		              name="TotalAmount"
		              precision="12"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[TotalAmount]"
		              scale="2"
		              truncationRowDisposition="FailComponent"/></outputColumns><externalMetadataColumns isUsed="True"><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="i4"
		                        description=""
		                        name="Id"
		                        refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[Id]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="dbTimeStamp"
		                        description=""
		                        name="OrderDate"
		                        refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[OrderDate]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="50"
		                        name="OrderNumber"
		                        refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[OrderNumber]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="i4"
		                        description=""
		                        name="CustomerId"
		                        refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[CustomerId]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        cachedPrecision="12"
		                        cachedScale="2"
		                        dataType="numeric"
		                        description=""
		                        name="TotalAmount"
		                        precision="12"
		                        refId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].ExternalColumns[TotalAmount]"
		                        scale="2"/></externalMetadataColumns></output><output isErrorOut="true"
		        name="OLE DB Source Error Output"
		        refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              description=""
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[Id]"
		              name="Id"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[Id]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="dbTimeStamp"
		              description=""
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[OrderDate]"
		              name="OrderDate"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[OrderDate]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              length="50"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[OrderNumber]"
		              name="OrderNumber"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[OrderNumber]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              description=""
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[CustomerId]"
		              name="CustomerId"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[CustomerId]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              cachedPrecision="12"
		              cachedScale="2"
		              dataType="numeric"
		              description=""
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[TotalAmount]"
		              name="TotalAmount"
		              precision="12"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[TotalAmount]"
		              scale="2"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[ErrorCode]"
		              name="ErrorCode"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[ErrorCode]"
		              specialFlags="1"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[ErrorColumn]"
		              name="ErrorColumn"
		              refId="Package\Load Order\Read Order.Outputs[OLE DB Source Error Output].Columns[ErrorColumn]"
		              specialFlags="2"/></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output></outputs></component><component componentClassID="Microsoft.OLEDBDestination"
		           contactInfo="OLE DB Destination;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;4"
		           description="OLE DB Destination"
		           name="Write to ExampleSource Order"
		           refId="Package\Load Order\Write to ExampleSource Order"
		           usesDispositions="true"
		           version="4"><properties><property dataType="System.Int32"
		          description="The number of seconds before a command times out.  A value of 0 indicates an infinite time-out."
		          name="CommandTimeout">0</property><property dataType="System.String"
		          description="Specifies the name of the database object used to open a rowset."
		          name="OpenRowset">[ExampleSource].[Order]</property><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="Specifies the variable that contains the name of the database object used to open a rowset."
		          name="OpenRowsetVariable"/><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          UITypeEditor="Microsoft.DataTransformationServices.Controls.ModalMultilineStringEditor"
		          dataType="System.String"
		          description="The SQL command to be executed."
		          name="SqlCommand"/><property dataType="System.Int32"
		          description="Specifies the column code page to use when code page information is unavailable from the data source."
		          name="DefaultCodePage">1252</property><property dataType="System.Boolean"
		          description="Forces the use of the DefaultCodePage property value when describing character data."
		          name="AlwaysUseDefaultCodePage">false</property><property dataType="System.Int32"
		          description="Specifies the mode used to access the database."
		          name="AccessMode"
		          typeConverter="AccessMode">3</property><property dataType="System.Boolean"
		          description="Indicates whether the values supplied for identity columns will be copied to the destination. If false, values for identity columns will be auto-generated at the destination. Applies only if fast load is turned on."
		          name="FastLoadKeepIdentity">false</property><property dataType="System.Boolean"
		          description="Indicates whether the columns containing null will have null inserted in the destination. If false, columns containing null will have their default values inserted at the destination. Applies only if fast load is turned on."
		          name="FastLoadKeepNulls">false</property><property dataType="System.String"
		          description="Specifies options to be used with fast load.  Applies only if fast load is turned on."
		          name="FastLoadOptions">TABLOCK,CHECK_CONSTRAINTS</property><property dataType="System.Int32"
		          description="Specifies when commits are issued during data insertion.  A value of 0 specifies that one commit will be issued at the end of data insertion.  Applies only if fast load is turned on."
		          name="FastLoadMaxInsertCommitSize">2147483647</property></properties><connections><connection xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		            connectionManagerID="{84C8FEB0-CB1D-443C-BF4D-DFE07E7C0215}:external"
		            connectionManagerRefId="Project.ConnectionManagers[Staging]"
		            description="The OLE DB runtime connection used to access the database."
		            name="OleDbConnection"
		            refId="Package\Load Order\Write to ExampleSource Order.Connections[OleDbConnection]"/></connections><inputs><input errorOrTruncationOperation="Insert"
		       errorRowDisposition="FailComponent"
		       hasSideEffects="true"
		       name="OLE DB Destination Input"
		       refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input]"><inputColumns><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="Id"
		             externalMetadataColumnId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[Id]"
		             lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[Id]"
		             refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].Columns[Id]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="OrderDate"
		             externalMetadataColumnId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[OrderDate]"
		             lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[OrderDate]"
		             refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].Columns[OrderDate]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="OrderNumber"
		             externalMetadataColumnId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[OrderNumber]"
		             lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[OrderNumber]"
		             refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].Columns[OrderNumber]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="CustomerId"
		             externalMetadataColumnId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[CustomerId]"
		             lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[CustomerId]"
		             refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].Columns[CustomerId]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="TotalAmount"
		             externalMetadataColumnId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[TotalAmount]"
		             lineageId="Package\Load Order\Read Order.Outputs[OLE DB Source Output].Columns[TotalAmount]"
		             refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].Columns[TotalAmount]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedDataType="dbTimeStamp"
		             cachedName="StageDateTime"
		             externalMetadataColumnId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[StageDateTime]"
		             lineageId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
		             refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].Columns[StageDateTime]"/></inputColumns><externalMetadataColumns isUsed="True"><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="i4"
		                        description=""
		                        name="Id"
		                        refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[Id]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="dbTimeStamp"
		                        description=""
		                        name="OrderDate"
		                        refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[OrderDate]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="50"
		                        name="OrderNumber"
		                        refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[OrderNumber]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="i4"
		                        description=""
		                        name="CustomerId"
		                        refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[CustomerId]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        cachedPrecision="12"
		                        cachedScale="2"
		                        dataType="numeric"
		                        description=""
		                        name="TotalAmount"
		                        precision="12"
		                        refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[TotalAmount]"
		                        scale="2"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="wstr"
		                        length="22"
		                        name="StageDateTime"
		                        refId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input].ExternalColumns[StageDateTime]"/></externalMetadataColumns></input></inputs><outputs><output exclusionGroup="1"
		        isErrorOut="true"
		        name="OLE DB Destination Error Output"
		        refId="Package\Load Order\Write to ExampleSource Order.Outputs[OLE DB Destination Error Output]"
		        synchronousInputId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Order\Write to ExampleSource Order.Outputs[OLE DB Destination Error Output].Columns[ErrorCode]"
		              name="ErrorCode"
		              refId="Package\Load Order\Write to ExampleSource Order.Outputs[OLE DB Destination Error Output].Columns[ErrorCode]"
		              specialFlags="1"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Order\Write to ExampleSource Order.Outputs[OLE DB Destination Error Output].Columns[ErrorColumn]"
		              name="ErrorColumn"
		              refId="Package\Load Order\Write to ExampleSource Order.Outputs[OLE DB Destination Error Output].Columns[ErrorColumn]"
		              specialFlags="2"/></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output></outputs></component></components><paths><path xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		      endId="Package\Load Order\Write to ExampleSource Order.Inputs[OLE DB Destination Input]"
		      name="Derived Column Output"
		      refId="Package\Load Order.Paths[Derived Column Output]"
		      startId="Package\Load Order\Add StageDateTime.Outputs[Derived Column Output]"/><path xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		      endId="Package\Load Order\Add StageDateTime.Inputs[Derived Column Input]"
		      name="OLE DB Source Output"
		      refId="Package\Load Order.Paths[OLE DB Source Output]"
		      startId="Package\Load Order\Read Order.Outputs[OLE DB Source Output]"/></paths></pipeline></DTS:ObjectData></DTS:Executable></DTS:Executables><DTS:DesignTimeProperties xmlns:DTS="www.microsoft.com/SqlServer/Dts"><![CDATA[<?xml version="1.0"?>
		<!--This CDATA section contains the layout information of the package. The section includes information such as (x,y) coordinates, width, and height.-->
		<!--If you manually edit this section and make a mistake, you can delete it. -->
		<!--The package will still be able to load normally but the previous layout information will be lost and the designer will automatically re-arrange the elements on the design surface.-->
		<Objects
  Version="8">
<!--Each node below will contain properties that do not affect runtime behavior.-->
<Package
  design-time-name="Package">
  <LayoutInfo>
    <GraphLayout
      Capacity="4" xmlns="clr-namespace:Microsoft.SqlServer.IntegrationServices.Designer.Model.Serialization;assembly=Microsoft.SqlServer.IntegrationServices.Graph">
      <NodeLayout
        Size="163,42"
        Id="Package\Load Order"
        TopLeft="231.5,189.5" />
    </GraphLayout>
  </LayoutInfo>
</Package>
<TaskHost
  design-time-name="Package\Load Order">
  <LayoutInfo>
    <GraphLayout
      Capacity="8" xmlns="clr-namespace:Microsoft.SqlServer.IntegrationServices.Designer.Model.Serialization;assembly=Microsoft.SqlServer.IntegrationServices.Graph" xmlns:mssgle="clr-namespace:Microsoft.SqlServer.Graph.LayoutEngine;assembly=Microsoft.SqlServer.Graph" xmlns:assembly="http://schemas.microsoft.com/winfx/2006/xaml">
      <NodeLayout
        Size="164,42"
        Id="Package\Load Order\Read Order"
        TopLeft="196,56" />
      <NodeLayout
        Size="172,42"
        Id="Package\Load Order\Add StageDateTime"
        TopLeft="197,189" />
      <NodeLayout
        Size="247,42"
        Id="Package\Load Order\Write to ExampleSource Order"
        TopLeft="162,301" />
      <EdgeLayout
        Id="Package\Load Order.Paths[OLE DB Source Output]"
        TopLeft="280.5,98">
        <EdgeLayout.Curve>
          <mssgle:Curve
            StartConnector="{assembly:Null}"
            EndConnector="0,91"
            Start="0,0"
            End="0,83.5">
            <mssgle:Curve.Segments>
              <mssgle:SegmentCollection
                Capacity="5">
                <mssgle:LineSegment
                  End="0,83.5" />
              </mssgle:SegmentCollection>
            </mssgle:Curve.Segments>
          </mssgle:Curve>
        </EdgeLayout.Curve>
        <EdgeLayout.Labels>
          <EdgeLabelCollection />
        </EdgeLayout.Labels>
      </EdgeLayout>
      <EdgeLayout
        Id="Package\Load Order.Paths[Derived Column Output]"
        TopLeft="284.25,231">
        <EdgeLayout.Curve>
          <mssgle:Curve
            StartConnector="{assembly:Null}"
            EndConnector="0,70"
            Start="0,0"
            End="0,62.5">
            <mssgle:Curve.Segments>
              <mssgle:SegmentCollection
                Capacity="5">
                <mssgle:LineSegment
                  End="0,62.5" />
              </mssgle:SegmentCollection>
            </mssgle:Curve.Segments>
          </mssgle:Curve>
        </EdgeLayout.Curve>
        <EdgeLayout.Labels>
          <EdgeLabelCollection />
        </EdgeLayout.Labels>
      </EdgeLayout>
    </GraphLayout>
  </LayoutInfo>
</TaskHost>
<PipelineComponentMetadata
  design-time-name="Package\Load Order\Read Order">
  <Properties>
    <Property>
      <Name>DataSourceViewID</Name>
    </Property>
    <Property>
      <Name>TableInfoObjectType</Name>
      <Value
        type="q2:string">Table</Value>
    </Property>
  </Properties>
</PipelineComponentMetadata>
<PipelineComponentMetadata
  design-time-name="Package\Load Order\Write to ExampleSource Order">
  <Properties>
    <Property>
      <Name>DataSourceViewID</Name>
    </Property>
    <Property>
      <Name>TableInfoObjectType</Name>
      <Value
        type="q2:string">Table</Value>
    </Property>
  </Properties>
</PipelineComponentMetadata>
</Objects>]]></DTS:DesignTimeProperties></DTS:Executable>
"""
  
  And an output named file:///C:/CrossGenerate/Output/stg_load_ExampleSource_Customer.dtsx with content:
  """
  <?xml version="1.0" encoding="UTF-8"?>
<DTS:Executable xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                DTS:CreationDate="2/16/2018 10:15:16 PM"
		                DTS:CreationName="Microsoft.Package"
		                DTS:CreatorComputerName="DESKTOP-EC40127"
		                DTS:CreatorName="DESKTOP-EC40127\Willem"
		                DTS:DTSID="{FEE0C51A-D375-41C3-8DFB-BAACEB3D0087}"
		                DTS:Description=""
		                DTS:ExecutableType="Microsoft.Package"
		                DTS:LastModifiedProductVersion="13.0.4001.0"
		                DTS:LocaleID="1033"
		                DTS:ObjectName="stg_load_ExampleSource_Customer"
		                DTS:PackageType="5"
		                DTS:VersionBuild="10"
		                DTS:VersionGUID="{245421C5-DFD7-4DC3-BAB4-E124415ED498}"
		                DTS:refId="Package"><DTS:Property xmlns:DTS="www.microsoft.com/SqlServer/Dts" DTS:Name="PackageFormatVersion">8</DTS:Property><DTS:Variables xmlns:DTS="www.microsoft.com/SqlServer/Dts"/><DTS:Executables xmlns:DTS="www.microsoft.com/SqlServer/Dts"><DTS:Executable xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                DTS:CreationName="Microsoft.Pipeline"
		                DTS:DTSID="{13F2099E-7D26-4671-8964-537D60997A48}"
		                DTS:Description="Data Flow Task"
		                DTS:ExecutableType="Microsoft.Pipeline"
		                DTS:LocaleID="-1"
		                DTS:ObjectName="Load Customer"
		                DTS:refId="Package\Load Customer"><DTS:Variables xmlns:DTS="www.microsoft.com/SqlServer/Dts"/><DTS:ObjectData xmlns:DTS="www.microsoft.com/SqlServer/Dts"><pipeline version="1"><components><component componentClassID="Microsoft.DerivedColumn"
		           contactInfo="Derived Column;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;0"
		           description="Creates new column values by applying expressions to transformation input columns. Create new columns or overwrite existing ones. For example, concatenate the values from the 'first name' and 'last name' column to make a 'full name' column."
		           name="Add StageDateTime"
		           refId="Package\Load Customer\Add StageDateTime"
		           usesDispositions="true"><inputs><input description="Input to the Derived Column Transformation"
		       name="Derived Column Input"
		       refId="Package\Load Customer\Add StageDateTime.Inputs[Derived Column Input]"><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></input></inputs><outputs><output description="Default Output of the Derived Column Transformation"
		        exclusionGroup="1"
		        name="Derived Column Output"
		        refId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Output]"
		        synchronousInputId="Package\Load Customer\Add StageDateTime.Inputs[Derived Column Input]"><outputColumns><outputColumn dataType="dbTimeStamp"
		              errorOrTruncationOperation="Computation"
		              errorRowDisposition="FailComponent"
		              lineageId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
		              name="StageDateTime"
		              refId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
		              truncationRowDisposition="FailComponent"><properties><property containsID="true"
		          dataType="System.String"
		          description="Derived Column Expression"
		          name="Expression">[GETDATE]()</property><property containsID="true"
		          dataType="System.String"
		          description="Derived Column Friendly Expression"
		          expressionType="Notify"
		          name="FriendlyExpression">GETDATE()</property></properties></outputColumn></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output><output description="Error Output of the Derived Column Transformation"
		        exclusionGroup="1"
		        isErrorOut="true"
		        name="Derived Column Error Output"
		        refId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Error Output]"
		        synchronousInputId="Package\Load Customer\Add StageDateTime.Inputs[Derived Column Input]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorCode]"
		              name="ErrorCode"
		              refId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorCode]"
		              specialFlags="1"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorColumn]"
		              name="ErrorColumn"
		              refId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Error Output].Columns[ErrorColumn]"
		              specialFlags="2"/></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output></outputs></component><component componentClassID="Microsoft.OLEDBSource"
		           contactInfo="OLE DB Source;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;7"
		           description="OLE DB Source"
		           name="Read Customer"
		           refId="Package\Load Customer\Read Customer"
		           usesDispositions="true"
		           version="7"><properties><property dataType="System.Int32"
		          description="The number of seconds before a command times out.  A value of 0 indicates an infinite time-out."
		          name="CommandTimeout">0</property><property dataType="System.String"
		          description="Specifies the name of the database object used to open a rowset."
		          name="OpenRowset">[dbo].[Customer]</property><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="Specifies the variable that contains the name of the database object used to open a rowset."
		          name="OpenRowsetVariable"/><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          UITypeEditor="Microsoft.DataTransformationServices.Controls.ModalMultilineStringEditor"
		          dataType="System.String"
		          description="The SQL command to be executed."
		          name="SqlCommand"/><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="The variable that contains the SQL command to be executed."
		          name="SqlCommandVariable"/><property dataType="System.Int32"
		          description="Specifies the column code page to use when code page information is unavailable from the data source."
		          name="DefaultCodePage">1252</property><property dataType="System.Boolean"
		          description="Forces the use of the DefaultCodePage property value when describing character data."
		          name="AlwaysUseDefaultCodePage">false</property><property dataType="System.Int32"
		          description="Specifies the mode used to access the database."
		          name="AccessMode"
		          typeConverter="AccessMode">0</property><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="The mappings between the parameters in the SQL command and variables."
		          name="ParameterMapping"/></properties><connections><connection xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		            connectionManagerID="{F43489D1-61D9-4682-898D-5398C303A955}:external"
		            connectionManagerRefId="Project.ConnectionManagers[Source]"
		            description="The OLE DB runtime connection used to access the database."
		            name="OleDbConnection"
		            refId="Package\Load Customer\Read Customer.Connections[OleDbConnection]"/></connections><outputs><output name="OLE DB Source Output"
		        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[Id]"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Id]"
		              name="Id"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Id]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[FirstName]"
		              length="50"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[FirstName]"
		              name="FirstName"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[FirstName]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[LastName]"
		              length="100"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[LastName]"
		              name="LastName"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[LastName]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[City]"
		              length="50"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[City]"
		              name="City"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[City]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[Country]"
		              length="3"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Country]"
		              name="Country"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Country]"
		              truncationRowDisposition="FailComponent"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              errorOrTruncationOperation="Conversion"
		              errorRowDisposition="FailComponent"
		              externalMetadataColumnId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[Phone]"
		              length="20"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Phone]"
		              name="Phone"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Phone]"
		              truncationRowDisposition="FailComponent"/></outputColumns><externalMetadataColumns isUsed="True"><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="i4"
		                        description=""
		                        name="Id"
		                        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[Id]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="50"
		                        name="FirstName"
		                        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[FirstName]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="100"
		                        name="LastName"
		                        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[LastName]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="50"
		                        name="City"
		                        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[City]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="3"
		                        name="Country"
		                        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[Country]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="20"
		                        name="Phone"
		                        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].ExternalColumns[Phone]"/></externalMetadataColumns></output><output isErrorOut="true"
		        name="OLE DB Source Error Output"
		        refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              description=""
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[Id]"
		              name="Id"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[Id]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              length="50"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[FirstName]"
		              name="FirstName"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[FirstName]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              length="100"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[LastName]"
		              name="LastName"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[LastName]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              length="50"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[City]"
		              name="City"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[City]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              length="3"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[Country]"
		              name="Country"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[Country]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              codePage="1252"
		              dataType="str"
		              description=""
		              length="20"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[Phone]"
		              name="Phone"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[Phone]"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[ErrorCode]"
		              name="ErrorCode"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[ErrorCode]"
		              specialFlags="1"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[ErrorColumn]"
		              name="ErrorColumn"
		              refId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Error Output].Columns[ErrorColumn]"
		              specialFlags="2"/></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output></outputs></component><component componentClassID="Microsoft.OLEDBDestination"
		           contactInfo="OLE DB Destination;Microsoft Corporation; Microsoft SQL Server; (C) Microsoft Corporation; All Rights Reserved; http://www.microsoft.com/sql/support;4"
		           description="OLE DB Destination"
		           name="Write to ExampleSource Customer"
		           refId="Package\Load Customer\Write to ExampleSource Customer"
		           usesDispositions="true"
		           version="4"><properties><property dataType="System.Int32"
		          description="The number of seconds before a command times out.  A value of 0 indicates an infinite time-out."
		          name="CommandTimeout">0</property><property dataType="System.String"
		          description="Specifies the name of the database object used to open a rowset."
		          name="OpenRowset">[ExampleSource].[Customer]</property><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          dataType="System.String"
		          description="Specifies the variable that contains the name of the database object used to open a rowset."
		          name="OpenRowsetVariable"/><property xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		          UITypeEditor="Microsoft.DataTransformationServices.Controls.ModalMultilineStringEditor"
		          dataType="System.String"
		          description="The SQL command to be executed."
		          name="SqlCommand"/><property dataType="System.Int32"
		          description="Specifies the column code page to use when code page information is unavailable from the data source."
		          name="DefaultCodePage">1252</property><property dataType="System.Boolean"
		          description="Forces the use of the DefaultCodePage property value when describing character data."
		          name="AlwaysUseDefaultCodePage">false</property><property dataType="System.Int32"
		          description="Specifies the mode used to access the database."
		          name="AccessMode"
		          typeConverter="AccessMode">3</property><property dataType="System.Boolean"
		          description="Indicates whether the values supplied for identity columns will be copied to the destination. If false, values for identity columns will be auto-generated at the destination. Applies only if fast load is turned on."
		          name="FastLoadKeepIdentity">false</property><property dataType="System.Boolean"
		          description="Indicates whether the columns containing null will have null inserted in the destination. If false, columns containing null will have their default values inserted at the destination. Applies only if fast load is turned on."
		          name="FastLoadKeepNulls">false</property><property dataType="System.String"
		          description="Specifies options to be used with fast load.  Applies only if fast load is turned on."
		          name="FastLoadOptions">TABLOCK,CHECK_CONSTRAINTS</property><property dataType="System.Int32"
		          description="Specifies when commits are issued during data insertion.  A value of 0 specifies that one commit will be issued at the end of data insertion.  Applies only if fast load is turned on."
		          name="FastLoadMaxInsertCommitSize">2147483647</property></properties><connections><connection xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		            connectionManagerID="{84C8FEB0-CB1D-443C-BF4D-DFE07E7C0215}:external"
		            connectionManagerRefId="Project.ConnectionManagers[Staging]"
		            description="The OLE DB runtime connection used to access the database."
		            name="OleDbConnection"
		            refId="Package\Load Customer\Write to ExampleSource Customer.Connections[OleDbConnection]"/></connections><inputs><input errorOrTruncationOperation="Insert"
		       errorRowDisposition="FailComponent"
		       hasSideEffects="true"
		       name="OLE DB Destination Input"
		       refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input]"><inputColumns><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="Id"
		             externalMetadataColumnId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[Id]"
		             lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Id]"
		             refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].Columns[Id]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="FirstName"
		             externalMetadataColumnId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[FirstName]"
		             lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[FirstName]"
		             refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].Columns[FirstName]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="LastName"
		             externalMetadataColumnId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[LastName]"
		             lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[LastName]"
		             refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].Columns[LastName]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="City"
		             externalMetadataColumnId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[City]"
		             lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[City]"
		             refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].Columns[City]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="Country"
		             externalMetadataColumnId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[Country]"
		             lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Country]"
		             refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].Columns[Country]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedCodepage="1252"
		             cachedDataType="str"
		             cachedLength="10"
		             cachedName="Phone"
		             externalMetadataColumnId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[Phone]"
		             lineageId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output].Columns[Phone]"
		             refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].Columns[Phone]"/><inputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		             cachedDataType="dbTimeStamp"
		             cachedName="StageDateTime"
		             externalMetadataColumnId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[StageDateTime]"
		             lineageId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Output].Columns[StageDateTime]"
		             refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].Columns[StageDateTime]"/></inputColumns><externalMetadataColumns isUsed="True"><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="i4"
		                        description=""
		                        name="Id"
		                        refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[Id]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="50"
		                        name="FirstName"
		                        refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[FirstName]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="100"
		                        name="LastName"
		                        refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[LastName]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="50"
		                        name="City"
		                        refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[City]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="3"
		                        name="Country"
		                        refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[Country]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        codePage="1252"
		                        dataType="str"
		                        description=""
		                        length="20"
		                        name="Phone"
		                        refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[Phone]"/><externalMetadataColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		                        dataType="wstr"
		                        length="22"
		                        name="StageDateTime"
		                        refId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input].ExternalColumns[StageDateTime]"/></externalMetadataColumns></input></inputs><outputs><output exclusionGroup="1"
		        isErrorOut="true"
		        name="OLE DB Destination Error Output"
		        refId="Package\Load Customer\Write to ExampleSource Customer.Outputs[OLE DB Destination Error Output]"
		        synchronousInputId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input]"><outputColumns><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Customer\Write to ExampleSource Customer.Outputs[OLE DB Destination Error Output].Columns[ErrorCode]"
		              name="ErrorCode"
		              refId="Package\Load Customer\Write to ExampleSource Customer.Outputs[OLE DB Destination Error Output].Columns[ErrorCode]"
		              specialFlags="1"/><outputColumn xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		              dataType="i4"
		              lineageId="Package\Load Customer\Write to ExampleSource Customer.Outputs[OLE DB Destination Error Output].Columns[ErrorColumn]"
		              name="ErrorColumn"
		              refId="Package\Load Customer\Write to ExampleSource Customer.Outputs[OLE DB Destination Error Output].Columns[ErrorColumn]"
		              specialFlags="2"/></outputColumns><externalMetadataColumns xmlns:DTS="www.microsoft.com/SqlServer/Dts"/></output></outputs></component></components><paths><path xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		      endId="Package\Load Customer\Write to ExampleSource Customer.Inputs[OLE DB Destination Input]"
		      name="Derived Column Output"
		      refId="Package\Load Customer.Paths[Derived Column Output]"
		      startId="Package\Load Customer\Add StageDateTime.Outputs[Derived Column Output]"/><path xmlns:DTS="www.microsoft.com/SqlServer/Dts"
		      endId="Package\Load Customer\Add StageDateTime.Inputs[Derived Column Input]"
		      name="OLE DB Source Output"
		      refId="Package\Load Customer.Paths[OLE DB Source Output]"
		      startId="Package\Load Customer\Read Customer.Outputs[OLE DB Source Output]"/></paths></pipeline></DTS:ObjectData></DTS:Executable></DTS:Executables><DTS:DesignTimeProperties xmlns:DTS="www.microsoft.com/SqlServer/Dts"><![CDATA[<?xml version="1.0"?>
		<!--This CDATA section contains the layout information of the package. The section includes information such as (x,y) coordinates, width, and height.-->
		<!--If you manually edit this section and make a mistake, you can delete it. -->
		<!--The package will still be able to load normally but the previous layout information will be lost and the designer will automatically re-arrange the elements on the design surface.-->
		<Objects
Version="8">
<!--Each node below will contain properties that do not affect runtime behavior.-->
<Package
  design-time-name="Package">
  <LayoutInfo>
    <GraphLayout
      Capacity="4" xmlns="clr-namespace:Microsoft.SqlServer.IntegrationServices.Designer.Model.Serialization;assembly=Microsoft.SqlServer.IntegrationServices.Graph">
      <NodeLayout
        Size="163,42"
        Id="Package\Load Customer"
        TopLeft="231.5,189.5" />
    </GraphLayout>
  </LayoutInfo>
</Package>
<TaskHost
  design-time-name="Package\Load Customer">
  <LayoutInfo>
    <GraphLayout
      Capacity="8" xmlns="clr-namespace:Microsoft.SqlServer.IntegrationServices.Designer.Model.Serialization;assembly=Microsoft.SqlServer.IntegrationServices.Graph" xmlns:mssgle="clr-namespace:Microsoft.SqlServer.Graph.LayoutEngine;assembly=Microsoft.SqlServer.Graph" xmlns:assembly="http://schemas.microsoft.com/winfx/2006/xaml">
      <NodeLayout
        Size="164,42"
        Id="Package\Load Customer\Read Customer"
        TopLeft="196,56" />
      <NodeLayout
        Size="172,42"
        Id="Package\Load Customer\Add StageDateTime"
        TopLeft="197,189" />
      <NodeLayout
        Size="247,42"
        Id="Package\Load Customer\Write to ExampleSource Customer"
        TopLeft="162,301" />
      <EdgeLayout
        Id="Package\Load Customer.Paths[OLE DB Source Output]"
        TopLeft="280.5,98">
        <EdgeLayout.Curve>
          <mssgle:Curve
            StartConnector="{assembly:Null}"
            EndConnector="0,91"
            Start="0,0"
            End="0,83.5">
            <mssgle:Curve.Segments>
              <mssgle:SegmentCollection
                Capacity="5">
                <mssgle:LineSegment
                  End="0,83.5" />
              </mssgle:SegmentCollection>
            </mssgle:Curve.Segments>
          </mssgle:Curve>
        </EdgeLayout.Curve>
        <EdgeLayout.Labels>
          <EdgeLabelCollection />
        </EdgeLayout.Labels>
      </EdgeLayout>
      <EdgeLayout
        Id="Package\Load Customer.Paths[Derived Column Output]"
        TopLeft="284.25,231">
        <EdgeLayout.Curve>
          <mssgle:Curve
            StartConnector="{assembly:Null}"
            EndConnector="0,70"
            Start="0,0"
            End="0,62.5">
            <mssgle:Curve.Segments>
              <mssgle:SegmentCollection
                Capacity="5">
                <mssgle:LineSegment
                  End="0,62.5" />
              </mssgle:SegmentCollection>
            </mssgle:Curve.Segments>
          </mssgle:Curve>
        </EdgeLayout.Curve>
        <EdgeLayout.Labels>
          <EdgeLabelCollection />
        </EdgeLayout.Labels>
      </EdgeLayout>
    </GraphLayout>
  </LayoutInfo>
</TaskHost>
<PipelineComponentMetadata
  design-time-name="Package\Load Customer\Read Customer">
  <Properties>
    <Property>
      <Name>DataSourceViewID</Name>
    </Property>
    <Property>
      <Name>TableInfoObjectType</Name>
      <Value
        type="q2:string">Table</Value>
    </Property>
  </Properties>
</PipelineComponentMetadata>
<PipelineComponentMetadata
  design-time-name="Package\Load Customer\Write to ExampleSource Customer">
  <Properties>
    <Property>
      <Name>DataSourceViewID</Name>
    </Property>
    <Property>
      <Name>TableInfoObjectType</Name>
      <Value
        type="q2:string">Table</Value>
    </Property>
  </Properties>
</PipelineComponentMetadata>
</Objects>]]></DTS:DesignTimeProperties></DTS:Executable>
"""
  