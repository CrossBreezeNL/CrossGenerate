# Source model

This model describes an example source database that is used in the examples.
The model contains two table definitions.
The tables are wrapped in a collection named mappableObjects.
For each table the column definitions are provided including datatype, nullability and primary key specification.

![Source model diagram](img/source_model.png)

!!! note
    Note that this is a model that only describes structure. All the information in this model could for example be obtained from querying a database's metadata tables.

## Model file
``` xml
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
```