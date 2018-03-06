# Source model

This model describes an example source database that is used in the examples.
The model contains two table definitions.
The tables are wrapped in a collection named mappableObjects.
For each table the column definitions are provided including datatype, nullability and primary key specification.

!!! todo
    Add a graphical data model for information purposes.

!!! note
    Note that this is a model that only describes structure. All the information in this model could for example be obtained from querying a database's metadata tables.

## Model file
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<modeldefinition>
   <system id="ExampleSource" internalid="6B629DE1-5B1F-402A-B1E7-43DFC141EB57" name="ExampleSource">
     <mappableObjects>
       <entity id="Order" internalid="D5ADD5CF-510D-4F42-8CE0-9F850A78FB41" name="Order">
         <attributes>
           <attribute id="Id" internalid="49257E04-0334-4850-9F6B-423B4921A7A6" name="Id" datatype="int" fulldatatype="int" required="TRUE" isKey="TRUE" />
           <attribute id="OrderDate" internalid="56A92EE6-55C9-4243-A0E1-B24978B86E4D" name="OrderDate" datatype="datetime" fulldatatype="datetime" isKey="FALSE" />
           <attribute id="OrderNumber" internalid="A1249EC3-2A7A-4FBB-9FC4-DEC00F3D0598" name="OrderNumber" datatype="varchar" length="50" fulldatatype="varchar(50)" isKey="FALSE" />
           <attribute id="CustomerId" internalid="53C34CE9-C822-4DB3-B76B-821D7F9BB7EB" name="CustomerId" datatype="int" fulldatatype="int" isKey="FALSE" />
           <attribute id="TotalAmount" internalid="E5A91AEB-BE4A-45AF-AFCA-BC6E3F199BAC" name="TotalAmount" datatype="decimal" precision="12" scale="2" fulldatatype="decimal(12,2)" isKey="FALSE" />
         </attributes>
         <keyAttributes>
           <keyAttribute attributeId="Id" attributeName="Id" />
         </keyAttributes>
       </entity>
       <entity id="Customer" internalid="F64B5547-8E05-465A-AD15-AFD8C2732AE4" name="Customer">
         <attributes>
           <attribute id="Id" internalid="A1016A0D-D621-429B-BF65-24F5A5A60CAA" name="Id" datatype="int" fulldatatype="int" required="TRUE" isKey="TRUE" />
           <attribute id="FirstName" internalid="B75751AC-CF60-4D88-B346-A888F9B65422" name="FirstName" datatype="varchar" length="50" fulldatatype="varchar(50)" isKey="FALSE" />
           <attribute id="LastName" internalid="63918C4F-EA12-4FDC-97EE-DB23C42D888C" name="LastName" datatype="varchar" length="100" fulldatatype="varchar(100)" isKey="FALSE" />
           <attribute id="City" internalid="01D55B0A-481F-4FD4-BA69-6A1587E4F87C" name="City" datatype="varchar" length="50" fulldatatype="varchar(50)" isKey="FALSE" />
           <attribute id="Country" internalid="64D2F2D7-F997-47D2-8AAD-C191B1DE960A" name="Country" datatype="varchar" length="3" fulldatatype="varchar(3)" isKey="FALSE" />
           <attribute id="Phone" internalid="E8F86F4E-5C1D-4F0A-98CA-EE6DB630A025" name="Phone" datatype="varchar" length="20" fulldatatype="varchar(20)" isKey="FALSE" />
         </attributes>
         <keyAttributes>
           <keyAttribute attributeId="Id" attributeName="Id" />
         </keyAttributes>
       </entity>
     </mappableObjects>
   </system>
</modeldefinition>
```