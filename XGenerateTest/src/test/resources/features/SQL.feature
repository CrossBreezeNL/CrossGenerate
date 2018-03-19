#Author: info@x-breeze.com
#Keywords Summary : CrossGenerate SQL

Feature: SQL
  This feature file contains the features when using SQL in your template.


  Scenario: Simple staging example

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
		
		And the following template named Staging_Tables_system_name.sql:
			"""
			-- @XGenSection(name="CreateTable" placeholderOnLastLine="GO")
			CREATE TABLE [system_name].[entity_name] (
			  -- @XGenSection(name="TableColumn")
			  [attribute_name]           attribute_fulldatatype        NULL,
			  [StageDateTime]            datetime2(2)              NOT NULL  
			);
			GO
			"""
			
		And the following config:
			"""
			<?xml version="1.0" encoding="UTF-8"?>
			<XGenConfig>
			  <Model/>
			  <Template rootSectionName="System">
			    <FileFormat templateType="text" currentAccessor="_" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
			    <Output type="single_output" />
			  </Template>
			  <Binding>
			    <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
			    <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system">
			        <!-- Bind the 'CreateTable' template section on the mappableObjects/entity elements in the model. -->
			        <SectionModelBinding section="CreateTable" modelXPath="mappableObjects/entity" placeholderName="entity">
			          <Placeholders>
			            <!-- For the placeholder the modelXPath is relative to its section model XPath. -->
			            <Placeholder name="system" modelXPath="../.." />
			          </Placeholders>
			          <!-- Bind the 'TableColumn' template section on the attributes/attribute elements in the model. -->
			          <SectionModelBinding section="TableColumn" modelXPath="attributes/attribute" placeholderName="attribute" />
			        </SectionModelBinding>
			    </SectionModelBinding>
			  </Binding>
			</XGenConfig>
			"""
			
		When I run the generator
		
		Then I expect 1 generation result with the following content:
			"""
			CREATE TABLE [ExampleSource].[Order] (
			  [Id]           int        NULL,
			  [OrderDate]           datetime        NULL,
			  [OrderNumber]           varchar(50)        NULL,
			  [CustomerId]           int        NULL,
			  [TotalAmount]           decimal(12,2)        NULL,
			  [StageDateTime]            datetime2(2)              NOT NULL  
			);
			GOCREATE TABLE [ExampleSource].[Customer] (
			  [Id]           int        NULL,
			  [FirstName]           varchar(50)        NULL,
			  [LastName]           varchar(100)        NULL,
			  [City]           varchar(50)        NULL,
			  [Country]           varchar(3)        NULL,
			  [Phone]           varchar(20)        NULL,
			  [StageDateTime]            datetime2(2)              NOT NULL  
			);
			GO
			"""
