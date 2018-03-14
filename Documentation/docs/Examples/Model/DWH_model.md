# DWH model

Below a DWH model example is shown. This model describes not only structure, but also the mapping from source to target.

!!! todo
    Add a graphical data model for information purposes.

## Model file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<modeldefinition>
  <system id="DWH_Training" internalId="BBB3BBCB-7113-4F37-833E-8C47058DBD7F" name="DWH_Training">
    <mappableObjects>
      <entity id="Customer" internalId="D7CA93F3-6DBF-4384-82B6-B7A93939D9E7" name="Customer" stereotype="entity">
        <attributes>
          <attribute id="CustomerId" internalId="60A543F2-3F96-4B30-AC88-A66E4A48D238" name="CustomerId"  datatype="int" fulldatatype="int" required="true" isKey="true"  />
          <attribute id="FirstName" internalId="1175E21E-F469-4501-AF3D-AB4D99F41F00" name="FirstName"  datatype="varchar" length="50" fulldatatype="varchar(50)" isKey="false"  />
          <attribute id="LastName" internalId="F8CDD275-A84B-4BA1-8AB3-E1F7F04EE3F9" name="LastName"  datatype="varchar" length="100" fulldatatype="varchar(100)" isKey="false"  />
          <attribute id="City" internalId="9E6E0AE6-A1B4-49B3-A89D-4AD04EF92DE1" name="City"  datatype="varchar" length="50" fulldatatype="varchar(50)" isKey="false"  />
          <attribute id="Country" internalId="A8EFD84F-00A9-40A3-8EFF-54EF89BDC6F9" name="Country"  datatype="varchar" length="3" fulldatatype="varchar(3)" isKey="false"  />
          <attribute id="DateOfLastSalesOrder" internalId="299B4B2F-3390-4AB5-9375-7138CFE12496" name="DateOfLastSalesOrder"  datatype="datetime" fulldatatype="datetime" isKey="false"  />
        </attributes>
        <keyAttributes>
          <keyAttribute attributeId="CustomerId" attributeName="CustomerId" />
        </keyAttributes>
        <mappableObjectMappings>
          <mappableObjectMapping id="ExampleSource" internalId="E1908229-00C4-4FB5-A138-E2B8CA8E3B1D" name="ExampleSource" mappingType="source" mappedObjectJoiner="ExampleSource.Customer as Customer
          LEFT JOIN DWH.LastSalesOrderPerCustomer as LastSalesOrderPerCustomer
            ON Customer.Id = LastSalesOrderPerCustomer.CustomerId">
            <mappedObjects>
              <mappedObject mappedObjectId="Customer" mappedObjectSystemName="ExampleSource" mappedObjectName="Customer" />
              <mappedObject mappedObjectId="LastSalesOrderPerCustomer" mappedObjectSystemName="DWH_Training" mappedObjectName="LastSalesOrderPerCustomer" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="Id" sourceAttributeSystem="ExampleSource" sourceAttributeName="Id" expression="ExampleSource_Customer.Id" targetAttributeId="CustomerId" targetAttributeName="CustomerId"  isKey="true"  datatype="int" fulldatatype="int"/>
              <attributeMapping sourceAttributeId="FirstName" sourceAttributeSystem="ExampleSource" sourceAttributeName="FirstName" expression="ExampleSource_Customer.FirstName" targetAttributeId="FirstName" targetAttributeName="FirstName"  isKey="false"   datatype="varchar" length="50" fulldatatype="varchar(50)"/>
              <attributeMapping sourceAttributeId="LastName" sourceAttributeSystem="ExampleSource" sourceAttributeName="LastName" expression="ExampleSource_Customer.LastName" targetAttributeId="LastName" targetAttributeName="LastName"  isKey="false"   datatype="varchar" length="100" fulldatatype="varchar(100)"/>
              <attributeMapping sourceAttributeId="City" sourceAttributeSystem="ExampleSource" sourceAttributeName="City" expression="ExampleSource_Customer.City" targetAttributeId="City" targetAttributeName="City"  isKey="false"   datatype="varchar" length="50" fulldatatype="varchar(50)"/>
              <attributeMapping sourceAttributeId="Country" sourceAttributeSystem="ExampleSource" sourceAttributeName="Country" expression="ExampleSource_Customer.Country" targetAttributeId="Country" targetAttributeName="Country"  isKey="false"   datatype="varchar" length="3" fulldatatype="varchar(3)"/>
              <attributeMapping sourceAttributeId="OrderDate" sourceAttributeSystem="DWH_Training" sourceAttributeName="OrderDate" expression="LastSalesOrderPerCustomer.OrderDate" targetAttributeId="DateOfLastSalesOrder" targetAttributeName="DateOfLastSalesOrder"  isKey="false"   datatype="datetime" fulldatatype="datetime"/>
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>  
      </entity>
      <entity id="SalesOrder" internalId="27EAF4DD-7A51-4335-B641-0BD7713693FB" name="SalesOrder" stereotype="entity">
        <attributes>
          <attribute id="OrderId" internalId="192A337A-BE2B-4ABD-A602-1635EA65A942" name="OrderId"  datatype="int" fulldatatype="int" isKey="false"  />
          <attribute id="OrderDate" internalId="CFD849BA-151B-4BA3-AC69-3D044F56E4AB" name="OrderDate"  datatype="datetime" fulldatatype="datetime" isKey="false"  />
          <attribute id="OrderNumber" internalId="A3EA129A-313D-4CAD-8C63-190A45213B79" name="OrderNumber"  datatype="varchar" length="50" fulldatatype="varchar(50)" isKey="false"  />
          <attribute id="CustomerId" internalId="569CCAB7-1F57-465C-B3B8-2FABE565FC9C" name="CustomerId"  datatype="int" fulldatatype="int" isKey="false"  />
          <attribute id="TotalAmount" internalId="2E800DA1-31F7-4494-9F2C-95EC1C01FF85" name="TotalAmount"  datatype="decimal" precision="12" scale="2" fulldatatype="decimal(12,2)" isKey="false"  />
        </attributes>
        <mappableObjectMappings>
          <mappableObjectMapping id="ExampleSource" internalId="3A1AA385-108E-4FF7-9168-7B78CC56AF38" name="ExampleSource" mappingType="source">
            <mappedObjects>
              <mappedObject mappedObjectId="Order" mappedObjectSystemName="ExampleSource" mappedObjectName="Order" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="Id" sourceAttributeSystem="ExampleSource" sourceAttributeName="Id" expression=""Order".Id" targetAttributeId="OrderId" targetAttributeName="OrderId"  isKey="false"   datatype="int" fulldatatype="int"/>
              <attributeMapping sourceAttributeId="OrderDate" sourceAttributeSystem="ExampleSource" sourceAttributeName="OrderDate" expression=""Order".OrderDate" targetAttributeId="OrderDate" targetAttributeName="OrderDate"  isKey="false"   datatype="datetime" fulldatatype="datetime"/>
              <attributeMapping sourceAttributeId="OrderNumber" sourceAttributeSystem="ExampleSource" sourceAttributeName="OrderNumber" expression=""Order".OrderNumber" targetAttributeId="OrderNumber" targetAttributeName="OrderNumber"  isKey="false"   datatype="varchar" length="50" fulldatatype="varchar(50)"/>
              <attributeMapping sourceAttributeId="CustomerId" sourceAttributeSystem="ExampleSource" sourceAttributeName="CustomerId" expression=""Order".CustomerId" targetAttributeId="CustomerId" targetAttributeName="CustomerId"  isKey="false"   datatype="int" fulldatatype="int"/>
              <attributeMapping sourceAttributeId="TotalAmount" sourceAttributeSystem="ExampleSource" sourceAttributeName="TotalAmount" expression=""Order".TotalAmount" targetAttributeId="TotalAmount" targetAttributeName="TotalAmount"  isKey="false"   datatype="decimal" precision="12" scale="2" fulldatatype="decimal(12,2)"/>
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>  
        <references>
          <reference id="SalesOrder_Customer" internalId="CFE7DFEA-D22C-4EB6-91FB-D517E4B93E30" name="SalesOrder_Customer" parentObjectId="Customer" parentReferenceName="Customer" childObjectId="SalesOrder" childReferenceName="SalesOrder">
            <referencedAttributes>
              <referencedAttribute parentObjectAttributeId="CustomerId" childObjectAttributeId="CustomerId"  />
            </referencedAttributes>
          </reference>
        </references>
      </entity>
      <businessRule id="LastSalesOrderPerCustomer" internalId="FE6C9734-B0DA-4140-9681-AB0DF9771F4C" name="LastSalesOrderPerCustomer" stereotype="aggregate">
        <attributes>
          <parameter id="CustomerId" internalId="C23CF4A9-7B24-4837-8C87-69DB5F6663FC" name="CustomerId"  datatype="int" fulldatatype="int" stereotype="parameter" required="true" gentype="parameter" />
          <attribute id="OrderDate" internalId="74904FF7-A76A-47A9-A4AF-1861E03418BC" name="OrderDate"  datatype="datetime" fulldatatype="datetime" stereotype="attribute" required="true" gentype="attribute" />
        </attributes>
        <keyAttributes>
          <keyAttribute attributeId="CustomerId" attributeName="CustomerId" />
        </keyAttributes>
        <mappableObjectMappings>
          <mappableObjectMapping id="ExampleSource" internalId="AF163E21-ACCF-42C4-B421-A10210AEA9E0" name="ExampleSource" mappingType="source">
            <mappedObjects>
              <mappedObject mappedObjectId="Order" mappedObjectSystemName="ExampleSource" mappedObjectName="Order" />
            </mappedObjects>
            <attributeMappings>
              <attributeMapping sourceAttributeId="OrderDate" sourceAttributeSystem="ExampleSource" sourceAttributeName="OrderDate" expression="MAX("Order".OrderDate)" targetAttributeId="OrderDate" targetAttributeName="OrderDate"  isKey="false"   datatype="datetime" fulldatatype="datetime"/>
              <attributeMapping sourceAttributeId="CustomerId" sourceAttributeSystem="ExampleSource" sourceAttributeName="CustomerId" expression=""Order".CustomerId" targetAttributeId="CustomerId" targetAttributeName="CustomerId"  isKey="true"  datatype="int" fulldatatype="int"/>
            </attributeMappings>
          </mappableObjectMapping>
        </mappableObjectMappings>  
      </businessRule>
    </mappableObjects>
  </system>
</modeldefinition>
```