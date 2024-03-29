# DWH model

Below a simple DWH model example is shown. The model shows two entities, customer and order, the relationship between them and several reference objects and business rules.

[![DWH model diagram](img/dwh_model.png)](img/dwh_model.png)

Next to structure, the model also contains information about the mapping from source to target including the usage of business rules. Below is an example of the mapping for the customer entity.

![DWH model diagram](img/dwh_mapping.png)

All information in the model translates to the XML file below, containing target structures (entity and relation), business rule definitions and source to target mappings.

## Model file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<modeldefinition>
  <system id="ExampleDWH" internalId="B1C935E0-9F47-41C0-BCED-4D5EA0C91A65" name="ExampleDWH" pdObjectType="Model">
    <sourceSystems>
      <system name="ExampleSource" internalId="380932B1-4152-4A1E-B649-93677B501977" />
      <system name="Reference" internalId="3A8B3BF1-B56B-48A8-A566-0C01515D48AC" />
    </sourceSystems>
    <mappableObjects>
      <businessRule id="Lookup_Country" internalId="DA13F4A5-65CA-4F63-A427-3B25FB328DC3" name="Lookup_Country" pdObjectType="Table" owner="BusinessRule" stereotype="lookup">
        <attributes>
          <parameter id="Country_Code" internalId="DDB72F63-3F24-4557-8380-E717D4E2A75C" name="Country_Code" pdObjectType="Column" datatype="varchar" length="3" fulldatatype="varchar(3)" stereotype="parameter" ordinal="1" />
          <attribute id="Country_Description" internalId="EB20F71A-BDC7-4079-A0CF-429906A8CDD6" name="Country_Description" pdObjectType="Column" datatype="varchar" length="100" fulldatatype="varchar(100)" stereotype="attribute" ordinal="2" />
          <attribute id="Country_Continent" internalId="5FE9B15F-C927-461C-BC78-4C72C1AA551B" name="Country_Continent" pdObjectType="Column" datatype="varchar" length="100" fulldatatype="varchar(100)" stereotype="attribute" ordinal="3" />
        </attributes>  
        <mappableObjectMappings>
          <mappableObjectMapping id="Mapping_Lookup_Country_Reference_Country" internalId="4214BBCB-0566-47A6-9B93-F5A301668C38" name="Mapping_Lookup_Country_Reference_Country" pdObjectType="TableMapping" mappingType="source"   mainMappedObject= "Reference_Country"  mappedObjectJoiner="[$(DWH_Staging_Reference)].[dbo].[Country] AS [Reference_Country]">
            <mappedObjects>
              <mappedObject mappedObjectId="Country" mappedObjectSystemName="Reference" mappedObjectName="Country" expression="Reference_Country" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="Country_Code" sourceAttributeSystem="Reference" sourceAttributeName="Country_Code" expression="Reference_Country.Country_Code" targetAttributeId="Country_Code" targetAttributeName="Country_Code" targetAttributeStereotype="parameter" />
              <attributeMapping sourceAttributeId="Country_Description" sourceAttributeSystem="Reference" sourceAttributeName="Country_Description" expression="Reference_Country.Country_Description" targetAttributeId="Country_Description" targetAttributeName="Country_Description" targetAttributeStereotype="attribute" />
              <attributeMapping sourceAttributeId="Country_Continent" sourceAttributeSystem="Reference" sourceAttributeName="Country_Continent" expression="Reference_Country.Country_Continent" targetAttributeId="Country_Continent" targetAttributeName="Country_Continent" targetAttributeStereotype="attribute" />
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>
      </businessRule>
      <entity id="Customer" internalId="39C5194D-382C-4746-B146-1E62AAE7114C" name="Customer" pdObjectType="Table" owner="BusinessVault" stereotype="entity">
        <attributes>
          <attribute id="Id" internalId="B2E3091A-0B2D-45CD-916F-A8A4E6C804C9" name="Id" pdObjectType="Column" datatype="int" fulldatatype="int" stereotype="attribute" ordinal="1" primary="true" required="true" userDefinedType="9029C1F9-19C6-42BF-8D88-1E3BC808BC26" />
          <attribute id="FirstName" internalId="A18FE8DD-A057-4FCB-AF3E-6129E4ED1880" name="FirstName" pdObjectType="Column" datatype="varchar" length="50" fulldatatype="varchar(50)" stereotype="attribute" ordinal="2" userDefinedType="BA314752-B151-4E29-A53E-94B1FD771CD2" />
          <attribute id="LastName" internalId="3C205946-C8EC-48DA-9E97-26C2D9E3DD9B" name="LastName" pdObjectType="Column" datatype="varchar" length="100" fulldatatype="varchar(100)" stereotype="attribute" ordinal="3" userDefinedType="4A1E500A-5916-4D6B-A42A-51D440834127" />
          <attribute id="City" internalId="7F94DA0F-C700-4497-ABA8-1F0DE7548831" name="City" pdObjectType="Column" datatype="varchar" length="50" fulldatatype="varchar(50)" stereotype="attribute" ordinal="4" userDefinedType="BA314752-B151-4E29-A53E-94B1FD771CD2" />
          <attribute id="Country_Code" internalId="0AF3C69F-1AD4-4263-A91C-10FC45918824" name="Country_Code" pdObjectType="Column" datatype="varchar" length="3" fulldatatype="varchar(3)" stereotype="attribute" ordinal="5" userDefinedType="3C1FF716-ED44-410D-80E2-59DB496135C6" />
          <attribute id="Country_Description" internalId="008CE9FC-F1B1-441D-B6EC-1598217073F5" name="Country_Description" pdObjectType="Column" datatype="nvarchar" length="100" fulldatatype="nvarchar(100)" stereotype="attribute" ordinal="6" userDefinedType="4A1E500A-5916-4D6B-A42A-51D440834127" />
          <attribute id="Country_Continent" internalId="BED50CAC-8E7D-4F50-B1F6-1ABE7B5A618A" name="Country_Continent" pdObjectType="Column" datatype="nvarchar" length="100" fulldatatype="nvarchar(100)" stereotype="attribute" ordinal="7" userDefinedType="4A1E500A-5916-4D6B-A42A-51D440834127" />
          <attribute id="Phone" internalId="BEFD68BE-6FEA-4FFB-A7A2-845B2479371D" name="Phone" pdObjectType="Column" datatype="varchar" length="20" fulldatatype="varchar(20)" stereotype="attribute" ordinal="8" userDefinedType="C21D6B7B-B763-406A-B775-CD7D3D65CF85" />
          <attribute id="FullName" internalId="8743FD52-17E2-4708-9D96-7B9041B89FD1" name="FullName" pdObjectType="Column" datatype="varchar" length="151" fulldatatype="varchar(151)" stereotype="attribute" ordinal="9" />
        </attributes>  
        <keyAttributes>
          <keyAttribute attributeId="Id" attributeName="Id" />
        </keyAttributes>
        <mappableObjectMappings>
          <mappableObjectMapping id="Mapping_Customer_ExampleSource_Customer_WithEnrichment" internalId="6E7D220F-3B7C-4EE2-A689-F09B7ED44973" name="Mapping_Customer_ExampleSource_Customer_WithEnrichment" pdObjectType="TableMapping" mappingType="source" mappedObjectJoiner="[DWH_Staging_ExampleSource].[dbo].[Customer] AS [ExampleSource_Customer]
          OUTER APPLY [BusinessRule].[udf_BR_Lookup_Country]([ExampleSource_Customer].[Country]) AS [BusinessRule_Lookup_Country]
          OUTER APPLY [BusinessRule].[udf_BR_Derive_Customer_FullName]([ExampleSource_Customer].[FirstName], [ExampleSource_Customer].[LastName]) AS [BusinessRule_Derive_Customer_FullName]"   mainMappedObject= "ExampleSource_Customer">
            <mappedObjects>
              <mappedObject mappedObjectId="Customer" mappedObjectSystemName="ExampleSource" mappedObjectOwner="dbo" mappedObjectName="Customer" expression="ExampleSource_Customer" stereotype="ExampleTableType" />
              <mappedObject mappedObjectId="Lookup_Country" mappedObjectSystemName="ExampleDWH" mappedObjectOwner="BusinessRule" mappedObjectName="Lookup_Country" expression="ExampleDWH_BusinessRule_Lookup_Country" stereotype="lookup" />
              <mappedObject mappedObjectId="Derive_Customer_FullName" mappedObjectSystemName="ExampleDWH" mappedObjectOwner="BusinessRule" mappedObjectName="Derive_Customer_FullName" expression="ExampleDWH_BusinessRule_Derive_Customer_FullName" stereotype="derive" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="Id" sourceAttributeSystem="ExampleSource" sourceAttributeOwner="dbo" sourceAttributeName="Id" expression="ExampleSource_Customer.Id" targetAttributeId="Id" targetAttributeName="Id" targetAttributePrimary="true" />
              <attributeMapping sourceAttributeId="FirstName" sourceAttributeSystem="ExampleSource" sourceAttributeOwner="dbo" sourceAttributeName="FirstName" expression="ExampleSource_Customer.FirstName" targetAttributeId="FirstName" targetAttributeName="FirstName" />
              <attributeMapping sourceAttributeId="LastName" sourceAttributeSystem="ExampleSource" sourceAttributeOwner="dbo" sourceAttributeName="LastName" expression="ExampleSource_Customer.LastName" targetAttributeId="LastName" targetAttributeName="LastName" />
              <attributeMapping sourceAttributeId="City" sourceAttributeSystem="ExampleSource" sourceAttributeOwner="dbo" sourceAttributeName="City" expression="ExampleSource_Customer.City" targetAttributeId="City" targetAttributeName="City" />
              <attributeMapping sourceAttributeId="Country" sourceAttributeSystem="ExampleSource" sourceAttributeOwner="dbo" sourceAttributeName="Country" expression="ExampleSource_Customer.Country" targetAttributeId="Country_Code" targetAttributeName="Country_Code" />
              <attributeMapping sourceAttributeId="Country_Description" sourceAttributeSystem="ExampleDWH" sourceAttributeOwner="BusinessRule" sourceAttributeName="Country_Description" expression="BusinessRule_Lookup_Country.Country_Description" targetAttributeId="Country_Description" targetAttributeName="Country_Description" />
              <attributeMapping sourceAttributeId="Country_Continent" sourceAttributeSystem="ExampleDWH" sourceAttributeOwner="BusinessRule" sourceAttributeName="Country_Continent" expression="BusinessRule_Lookup_Country.Country_Continent" targetAttributeId="Country_Continent" targetAttributeName="Country_Continent" />
              <attributeMapping sourceAttributeId="Phone" sourceAttributeSystem="ExampleSource" sourceAttributeOwner="dbo" sourceAttributeName="Phone" expression="ExampleSource_Customer.Phone" targetAttributeId="Phone" targetAttributeName="Phone" />
              <attributeMapping sourceAttributeId="FullName" sourceAttributeSystem="ExampleDWH" sourceAttributeOwner="BusinessRule" sourceAttributeName="FullName" expression="BusinessRule_Derive_Customer_FullName.FullName" targetAttributeId="FullName" targetAttributeName="FullName" />
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>
      </entity>
      <entity id="Order" internalId="E98E63CE-4662-45D9-BA48-7B56A5A83D1E" name="Order" pdObjectType="Table" owner="BusinessVault" stereotype="entity">
        <attributes>
          <attribute id="Id" internalId="31DAFCF0-A33A-4CE4-8985-E1A371B1E798" name="Id" pdObjectType="Column" datatype="int" fulldatatype="int" stereotype="attribute" ordinal="1" primary="true" required="true" userDefinedType="9029C1F9-19C6-42BF-8D88-1E3BC808BC26" />
          <attribute id="OrderDate" internalId="3AF09AB3-9076-4410-BAB6-28378223987D" name="OrderDate" pdObjectType="Column" datatype="datetime" fulldatatype="datetime" stereotype="attribute" ordinal="2" userDefinedType="536FDCA6-C809-48CF-BA15-CC3FE6AB2D9F" />
          <attribute id="OrderNumber" internalId="512F0304-5023-4B6A-B854-6B195D244B43" name="OrderNumber" pdObjectType="Column" datatype="varchar" length="50" fulldatatype="varchar(50)" stereotype="attribute" ordinal="3" userDefinedType="BA314752-B151-4E29-A53E-94B1FD771CD2" />
          <attribute id="CustomerId" internalId="D4AA200C-16C2-4626-89AC-C25840C13174" name="CustomerId" pdObjectType="Column" datatype="int" fulldatatype="int" stereotype="attribute" ordinal="4" userDefinedType="9029C1F9-19C6-42BF-8D88-1E3BC808BC26" />
          <attribute id="TotalAmount" internalId="5D85AC0E-A189-49F9-8691-BFC1799C661C" name="TotalAmount" pdObjectType="Column" datatype="decimal" precision="12" scale="2" fulldatatype="decimal(12,2)" stereotype="attribute" ordinal="5" userDefinedType="73AC10D5-A7EA-4B95-80BF-5EBB22CDFE30" />
        </attributes>  
        <keyAttributes>
          <keyAttribute attributeId="Id" attributeName="Id" />
        </keyAttributes>
        <mappableObjectMappings>
          <mappableObjectMapping id="Mapping_Order_ExampleSource_Order" internalId="A92B7B45-F0C5-416E-9DC2-6CEBC71D900B" name="Mapping_Order_ExampleSource_Order" pdObjectType="TableMapping" mappingType="source"   mainMappedObject= "ExampleSource_Order"  mappedObjectJoiner="[DWH_Staging_ExampleSource].[dbo].[Order] AS [ExampleSource_Order]">
            <mappedObjects>
              <mappedObject mappedObjectId="Order" mappedObjectSystemName="ExampleSource" mappedObjectName="Order" expression="ExampleSource_Order" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="Id" sourceAttributeSystem="ExampleSource" sourceAttributeName="Id" expression="ExampleSource_Order.Id" targetAttributeId="Id" targetAttributeName="Id" targetAttributePrimary="true" />
              <attributeMapping sourceAttributeId="OrderDate" sourceAttributeSystem="ExampleSource" sourceAttributeName="OrderDate" expression="ExampleSource_Order.OrderDate" targetAttributeId="OrderDate" targetAttributeName="OrderDate" />
              <attributeMapping sourceAttributeId="OrderNumber" sourceAttributeSystem="ExampleSource" sourceAttributeName="OrderNumber" expression="ExampleSource_Order.OrderNumber" targetAttributeId="OrderNumber" targetAttributeName="OrderNumber" />
              <attributeMapping sourceAttributeId="CustomerId" sourceAttributeSystem="ExampleSource" sourceAttributeName="CustomerId" expression="ExampleSource_Order.CustomerId" targetAttributeId="CustomerId" targetAttributeName="CustomerId" />
              <attributeMapping sourceAttributeId="TotalAmount" sourceAttributeSystem="ExampleSource" sourceAttributeName="TotalAmount" expression="ExampleSource_Order.TotalAmount" targetAttributeId="TotalAmount" targetAttributeName="TotalAmount" />
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>
      </entity>
      <relation id="Order_Customer" internalId="14895D76-B1A0-4A51-B733-76BEE730D37B" name="Order_Customer" pdObjectType="Table" owner="BusinessVault" stereotype="relation">
        <attributes>
          <attribute id="OrderId" internalId="B7289703-0C35-492D-958D-625C249103C4" name="OrderId" pdObjectType="Column" datatype="int" fulldatatype="int" stereotype="attribute" ordinal="1" primary="true" required="true" userDefinedType="9029C1F9-19C6-42BF-8D88-1E3BC808BC26" />
          <attribute id="CustomerId" internalId="4D44591D-46CC-44F7-8D73-029297BB4E77" name="CustomerId" pdObjectType="Column" datatype="int" fulldatatype="int" stereotype="attribute" ordinal="2" primary="true" required="true" userDefinedType="9029C1F9-19C6-42BF-8D88-1E3BC808BC26" />
        </attributes>  
        <keyAttributes>
          <keyAttribute attributeId="OrderId" attributeName="OrderId" />
          <keyAttribute attributeId="CustomerId" attributeName="CustomerId" />
        </keyAttributes>
        <mappableObjectMappings>
          <mappableObjectMapping id="Mapping_Order_Customer_ExampleSource_Order" internalId="B46BF85F-D0C1-440F-BBC9-3FA5A5892F72" name="Mapping_Order_Customer_ExampleSource_Order" pdObjectType="TableMapping" mappingType="source"   mainMappedObject= "ExampleSource_Order" mappedObjectJoiner="[DWH_Staging_ExampleSource].[dbo].[Order] AS [ExampleSource_Order]">
            <mappedObjects>
              <mappedObject mappedObjectId="Order" mappedObjectSystemName="ExampleSource" mappedObjectName="Order" expression="ExampleSource_Order" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="Id" sourceAttributeSystem="ExampleSource" sourceAttributeName="Id" expression="ExampleSource_Order.Id" targetAttributeId="OrderId" targetAttributeName="OrderId" targetAttributePrimary="true" />
              <attributeMapping sourceAttributeId="CustomerId" sourceAttributeSystem="ExampleSource" sourceAttributeName="CustomerId" expression="ExampleSource_Order.CustomerId" targetAttributeId="CustomerId" targetAttributeName="CustomerId" targetAttributePrimary="true" />
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>
        <references>
          <reference id="Order_Customer_Customer" internalId="7D8B6FD7-4C52-44D5-A9D3-66AD97891432" name="Order_Customer_Customer" pdObjectType="Reference" parentObjectId="Customer" parentOwner="BusinessVault" parentReferenceName="Customer" childObjectId="Order_Customer" childOwner="BusinessVault" childReferenceName="Order_Customer">
            <referencedAttributes>
              <referencedAttribute parentObjectAttributeId="Id" childObjectAttributeId="CustomerId" name="CustomerId"  />
            </referencedAttributes>
          </reference>
          <reference id="Order_Customer_Order" internalId="4C1C6518-1F8B-4EC8-9178-0004B6612B1E" name="Order_Customer_Order" pdObjectType="Reference" parentObjectId="Order" parentOwner="BusinessVault" parentReferenceName="Order" childObjectId="Order_Customer" childOwner="BusinessVault" childReferenceName="Order_Customer">
            <referencedAttributes>
              <referencedAttribute parentObjectAttributeId="Id" childObjectAttributeId="OrderId" name="OrderId" />
            </referencedAttributes>
          </reference>
        </references>
      </relation>
      <entity id="Country" internalId="0AF86A6C-5018-47A4-A58F-84E61AA4CDD9" name="Country" pdObjectType="Table" owner="BusinessVault" stereotype="entity">
        <attributes>
          <attribute id="Code" internalId="1F8205E3-7EB9-4C3C-A8AB-CBCABDA3DCA8" name="Code" pdObjectType="Column" datatype="nvarchar" length="3" fulldatatype="nvarchar(3)" stereotype="attribute" ordinal="1" primary="true" required="true" />
          <attribute id="Description" internalId="4564FFF0-C768-4298-A5EB-C56C8F94674A" name="Description" pdObjectType="Column" datatype="nvarchar" length="100" fulldatatype="nvarchar(100)" stereotype="attribute" ordinal="2" />
          <attribute id="Continent" internalId="31311140-9F68-4CFE-8564-7F461C0DE37E" name="Continent" pdObjectType="Column" datatype="nvarchar" length="100" fulldatatype="nvarchar(100)" stereotype="attribute" ordinal="3" />
        </attributes>  
        <keyAttributes>
          <keyAttribute attributeId="Code" attributeName="Code" />
        </keyAttributes>
        <mappableObjectMappings>
          <mappableObjectMapping id="Mapping_Country_Reference_Country" internalId="3E3AB9A2-22B7-4008-ABCD-C6657AE69392" name="Mapping_Country_Reference_Country" pdObjectType="TableMapping" mappingType="source"   mainMappedObject= "Reference_Country" mappedObjectJoiner="[$(DWH_Staging_Reference)].[dbo].[Country] AS [Reference_Country]">
            <mappedObjects>
              <mappedObject mappedObjectId="Country" mappedObjectSystemName="Reference" mappedObjectOwner="dbo" mappedObjectName="Country" expression="Reference_Country" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="Country_Code" sourceAttributeSystem="Reference" sourceAttributeName="Country_Code" expression="Reference_Country.Country_Code" targetAttributeId="Code" targetAttributeName="Code" targetAttributePrimary="true" />
              <attributeMapping sourceAttributeId="Country_Description" sourceAttributeSystem="Reference" sourceAttributeName="Country_Description" expression="Reference_Country.Country_Description" targetAttributeId="Description" targetAttributeName="Description" />
              <attributeMapping sourceAttributeId="Country_Continent" sourceAttributeSystem="Reference" sourceAttributeName="Country_Continent" expression="Reference_Country.Country_Continent" targetAttributeId="Continent" targetAttributeName="Continent" />
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>
      </entity>
      <businessRule id="Derive_Customer_FullName" internalId="88A1C5A2-0A9A-43BA-8CBB-DD6BAD143549" name="Derive_Customer_FullName" pdObjectType="Table" owner="BusinessRule" stereotype="derive">
        <expression>(SELECT @FirstName + &#39; &#39; + @LastName AS [FullName])</expression>
        <attributes>
          <parameter id="FirstName" internalId="CAEF6965-0AA1-4BE5-A7BD-48188C70BC41" name="FirstName" pdObjectType="Column" datatype="varchar" length="50" fulldatatype="varchar(50)" stereotype="parameter" ordinal="1" />
          <parameter id="LastName" internalId="8590808F-282F-410A-B48C-01FE3E30AC2D" name="LastName" pdObjectType="Column" datatype="varchar" length="100" fulldatatype="varchar(100)" stereotype="parameter" ordinal="2" />
          <attribute id="FullName" internalId="DE0D6D17-9850-4797-9C7C-081A4A00D82B" name="FullName" pdObjectType="Column" datatype="varchar" length="151" fulldatatype="varchar(151)" stereotype="attribute" ordinal="3" />
        </attributes>  
      </businessRule>
    </mappableObjects>
    <userDefinedTypes>
      <userDefinedType id="Amount" internalId="73AC10D5-A7EA-4B95-80BF-5EBB22CDFE30" name="Amount" pdObjectType="PhysicalDomain" datatype="decimal" precision="12" scale="2" fulldatatype="decimal(12,2)" />
      <userDefinedType id="Text_100" internalId="4A1E500A-5916-4D6B-A42A-51D440834127" name="Text_100" pdObjectType="PhysicalDomain" datatype="nvarchar" length="100" fulldatatype="nvarchar(100)" />
      <userDefinedType id="Text_020" internalId="C21D6B7B-B763-406A-B775-CD7D3D65CF85" name="Text_020" pdObjectType="PhysicalDomain" datatype="nvarchar" length="20" fulldatatype="nvarchar(20)" />
      <userDefinedType id="Text_003" internalId="3C1FF716-ED44-410D-80E2-59DB496135C6" name="Text_003" pdObjectType="PhysicalDomain" datatype="nvarchar" length="3" fulldatatype="nvarchar(3)" />
      <userDefinedType id="Text_050" internalId="BA314752-B151-4E29-A53E-94B1FD771CD2" name="Text_050" pdObjectType="PhysicalDomain" datatype="nvarchar" length="50" fulldatatype="nvarchar(50)" />
      <userDefinedType id="Integer" internalId="9029C1F9-19C6-42BF-8D88-1E3BC808BC26" name="Integer" pdObjectType="PhysicalDomain" datatype="int" fulldatatype="int" />
      <userDefinedType id="DateTime" internalId="536FDCA6-C809-48CF-BA15-CC3FE6AB2D9F" name="DateTime" pdObjectType="PhysicalDomain" datatype="datetime2" length="3" fulldatatype="datetime2(3)" />
    </userDefinedTypes>
  </system>
</modeldefinition>
```