@Unit

Feature: Unit_TextTemplate_Section
  In this feature we will describe the section feature in text templates.

  Scenario: implicit root section only
    Given I have the following model:
			"""
			<?xml version="1.0" encoding="UTF-8"?>
			<modeldefinition>
			  <system name="ExampleSource">
			  </system>
			</modeldefinition>
			"""
		
		And the following template named "CreateSchema.sql":
			"""			
			CREATE SCHEMA system_name;
			GO;
			"""
			
		And the following config:
			"""
			<?xml version="1.0" encoding="UTF-8"?>
			<XGenConfig>
			  <Model/>
			  <Template rootSectionName="System">
			    <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
			    <Output type="single_output" />
			  </Template>
			  <Binding>
			    <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
			    <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system" />
			  </Binding>
			</XGenConfig>
			"""
			
		When I run the generator
		
		Then I expect 1 generation result
		And an output named "CreateSchema.sql" with content:
			"""
			CREATE SCHEMA ExampleSource;
			GO;
			"""
			
  Scenario: Single line section
    Given I have the following model file: "general/model.xml"
	  And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity")
    DROP TABLE entity_name;
    GO;
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    DROP TABLE Order;
    DROP TABLE Customer;
    GO;
    """
   
  Scenario: Multiline section with placeholderOnLastLine, including end
   Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;" includeEnd="true")
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
  And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    DROP TABLE Order;
    GO;
    DROP TABLE Customer;
    GO;
    -- End of template
    """  
    
  Scenario: Multiline section with placeholderOnLastLine, excluding end
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;" includeEnd="false")
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    DROP TABLE Order;   
    DROP TABLE Customer
    GO;
    -- End of template
    """  
    
  Scenario: Multiline section with nrOfLines
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" nrOfLines=2)
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    DROP TABLE Order;   
    GO;
    DROP TABLE Customer
    GO;
    -- End of template
    """  
     
  Scenario: Multiline section with end character sequence, including end
   Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" end="GO; " includeEnd="true")
    DROP TABLE entity_name;
    GO; 
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    DROP TABLE Order;
    GO; DROP TABLE Customer;
    GO; 
    -- End of template
    """
           
  Scenario: Multiline section with end character sequence, excluding end
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" end="GO;" includeEnd="false")
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    DROP TABLE Order;
    DROP TABLE Customer;
    GO;
    -- End of template
    """       
    
	Scenario: Section with placeholderOnFirstLine, including begin
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnFirstLine="DROP" includeBegin="true")
    -- section starts on the next line
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
     -- section starts on the next line
    DROP TABLE Order;
    DROP TABLE Customer;
    GO;
    -- End of template
    """     
    
	Scenario: Section with placeholderOnFirstLine, excluding begin
	 Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnFirstLine="-- start of section" includeBegin="false")
    -- section starts on the next line
    -- start of section
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
     -- section starts on the next line
     -- start of section
    DROP TABLE Order;
    DROP TABLE Customer;
    GO;
    -- End of template
    """  
    
	Scenario: Section with begin character sequence, including begin
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" begin="DROP TABLE" includeBegin="true")
    -- section starts on the next line
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    -- section starts on the next line
    DROP TABLE Order;
    DROP TABLE Customer;
    GO;
    -- End of template
    """  
    
	Scenario: Section with begin character sequence, excluding begin
    Given I have the following model file: "general/model.xml"
    And the following template named "DropTables.sql":
    """     
    -- @XGenSection(name="Entity" begin="-- begin drop statement" includeBegin="false" placeholderOnLastLine="GO;")
    -- section starts on the next line
    -- begin drop statement
    DROP TABLE entity_name;
    GO;
    -- End of template
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity"/>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "DropTables.sql" with content:
    """
    -- section starts on the next line
    -- begin drop statement
    DROP TABLE Order;
    GO;
 
    DROP TABLE Customer;
    GO;
    -- End of template
    """  
	Scenario: Section with prefix on firstOnly
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS 
      -- @XGenSection(name="Attribute" prefix="(" prefixStyle="firstOnly")
      attribute_name attribute_fulldatatype,
      recordCreatedOn datetime2(3))    
    GO;
      """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
		CREATE TABLE Order AS 
		(  Id int,
			 OrderDate datetime,
			 OrderNumber varchar(50),
			 CustomerId int,
			 TotalAmount decimal(12,2),
			 Id int,
			 FirstName varchar(50),
			 LastName varchar(100),
			 City varchar(50),
			 Country varchar(3),
			 Phone varchar(20),
			 recordCreatedOn datetime2(3))    
		GO;
		CREATE TABLE Customer AS 
		(  Id int,
			 OrderDate datetime,
			 OrderNumber varchar(50),
			 CustomerId int,
			 TotalAmount decimal(12,2),
			 Id int,
			 FirstName varchar(50),
			 LastName varchar(100),
			 City varchar(50),
			 Country varchar(3),
			 Phone varchar(20),
			 recordCreatedOn datetime2(3))    
		GO;
    """  
    
	Scenario: Section wih prefix on lastOnly
	Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS (
      -- @XGenSection(name="Attribute" prefix="/* last */" prefixStyle="lastOnly")
      attribute_name attribute_fulldatatype,
      recordCreatedOn datetime2(3)
      )    
    GO;
      """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
    CREATE TABLE Order AS (
  Id int,
  OrderDate datetime,
  OrderNumber varchar(50),
  CustomerId int,
  TotalAmount decimal(12,2),
  Id int,
  FirstName varchar(50),
  LastName varchar(100),
  City varchar(50),
  Country varchar(3),
/* last */  Phone varchar(20),
  recordCreatedOn datetime2(3)
  )    
GO;CREATE TABLE Customer AS (
  Id int,
  OrderDate datetime,
  OrderNumber varchar(50),
  CustomerId int,
  TotalAmount decimal(12,2),
  Id int,
  FirstName varchar(50),
  LastName varchar(100),
  City varchar(50),
  Country varchar(3),
/* last */  Phone varchar(20),
  recordCreatedOn datetime2(3)
  )    
GO;
"""  

	Scenario: Section with prefix on allButFirst
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS (
      -- @XGenSection(name="Attribute" prefix="," prefixStyle="allButFirst")
      attribute_name attribute_fulldatatype
      ,recordCreatedOn datetime2(3)
      )    
    GO;
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
    CREATE TABLE Order AS (
  Id int
,  OrderDate datetime
,  OrderNumber varchar(50)
,  CustomerId int
,  TotalAmount decimal(12,2)
,  Id int
,  FirstName varchar(50)
,  LastName varchar(100)
,  City varchar(50)
,  Country varchar(3)
,  Phone varchar(20)
  ,recordCreatedOn datetime2(3)
  )    
GO;CREATE TABLE Customer AS (
    Id int
,  OrderDate datetime
,  OrderNumber varchar(50)
,  CustomerId int
,  TotalAmount decimal(12,2)
,  Id int
,  FirstName varchar(50)
,  LastName varchar(100)
,  City varchar(50)
,  Country varchar(3)
,  Phone varchar(20)
  ,recordCreatedOn datetime2(3)
  )    
GO;
"""  

	Scenario: Section with prefix on allButLast
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS (
      -- @XGenSection(name="Attribute" prefix="/* not last */" prefixStyle="allButLast")
      attribute_name attribute_fulldatatype
      ,recordCreatedOn datetime2(3)
      )    
    GO;
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
    CREATE TABLE Order AS (
/* not last */  Id int
/* not last */  OrderDate datetime
/* not last */  OrderNumber varchar(50)
/* not last */  CustomerId int
/* not last */  TotalAmount decimal(12,2)
/* not last */  Id int
/* not last */  FirstName varchar(50)
/* not last */  LastName varchar(100)
/* not last */  City varchar(50)
/* not last */  Country varchar(3)
  Phone varchar(20)
  ,recordCreatedOn datetime2(3)
  )    
GO;CREATE TABLE Customer AS (
/* not last */  Id int
/* not last */  OrderDate datetime
/* not last */  OrderNumber varchar(50)
/* not last */  CustomerId int
/* not last */  TotalAmount decimal(12,2)
/* not last */  Id int
/* not last */  FirstName varchar(50)
/* not last */  LastName varchar(100)
/* not last */  City varchar(50)
/* not last */  Country varchar(3)
  Phone varchar(20)
  ,recordCreatedOn datetime2(3)
  )    
GO;
    """ 
    
	Scenario: Section with suffix on firstOnly
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS (
      -- @XGenSection(name="Attribute" end="," suffix="/* only on first */" suffixStyle="firstOnly")
      attribute_name attribute_fulldatatype,
      ,recordCreatedOn datetime2(3)
      )    
    GO;
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
    CREATE TABLE Order AS (
      Id int,/* only on first */  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20),
      ,recordCreatedOn datetime2(3)
)    
GO;CREATE TABLE Customer AS (
      Id int,/* only on first */  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20),
      ,recordCreatedOn datetime2(3)
)    
GO;
    """
	Scenario: Section wih suffix on lastOnly
	  Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS (
      -- @XGenSection(name="Attribute" end="," suffix="/* only on last */" suffixStyle="lastOnly")
      attribute_name attribute_fulldatatype,
      ,recordCreatedOn datetime2(3)
      )    
    GO;
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
    CREATE TABLE Order AS (
      Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20),/* only on last */
      ,recordCreatedOn datetime2(3)
)    
GO;CREATE TABLE Customer AS (
      Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20),/* only on last */
      ,recordCreatedOn datetime2(3)
)    
GO;
    """
    
	Scenario: Section with suffix on allButFirst
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS (
      -- @XGenSection(name="Attribute" end="," suffix="/*not on first */" suffixStyle="allButFirst")
      attribute_name attribute_fulldatatype,
      ,recordCreatedOn datetime2(3)
      )    
    GO;
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
    CREATE TABLE Order AS (
				  Id int,  OrderDate datetime,/*not on first */  OrderNumber varchar(50),/*not on first */  CustomerId int,/*not on first */  TotalAmount decimal(12,2),/*not on first */  Id int,/*not on first */  FirstName varchar(50),/*not on first */  LastName varchar(100),/*not on first */  City varchar(50),/*not on first */  Country varchar(3),/*not on first */  Phone varchar(20),/*not on first */
				  ,recordCreatedOn datetime2(3)
				  )    
GO;CREATE TABLE Customer AS (
				  Id int,  OrderDate datetime,/*not on first */  OrderNumber varchar(50),/*not on first */  CustomerId int,/*not on first */  TotalAmount decimal(12,2),/*not on first */  Id int,/*not on first */  FirstName varchar(50),/*not on first */  LastName varchar(100),/*not on first */  City varchar(50),/*not on first */  Country varchar(3),/*not on first */  Phone varchar(20),/*not on first */
				  ,recordCreatedOn datetime2(3)
				  )    
GO;
    """
    
	Scenario: Section with suffix on allButLast
    Given I have the following model file: "general/model.xml"
    And the following template named "CreateTables.sql":
    """     
    -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
    CREATE TABLE entity_name AS (
      -- @XGenSection(name="Attribute" end="fulldatatype" suffix="," suffixStyle="allButLast")
      attribute_name attribute_fulldatatype
      ,recordCreatedOn datetime2(3)
      )    
    GO;
    """
  
    And the following config:
    """
    <?xml version="1.0" encoding="UTF-8"?>
    <XGenConfig>
      <Model/>
      <Template rootSectionName="System">
        <FileFormat templateType="text" singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
        <Output type="single_output" />
      </Template>
      <Binding>
        <!-- Bind the 'System' template section on the /modeldefinition/system elements in the model. -->
        <SectionModelBinding section="System" modelXPath="/modeldefinition/system" placeholderName="system"> 
          <SectionModelBinding section="Entity" modelXPath="/modeldefinition/system/mappableObjects/entity" placeholderName="entity">
            <SectionModelBinding section="Attribute" modelXPath="/modeldefinition/system/mappableObjects/entity/attributes/attribute" placeholderName="attribute"/>
          </SectionModelBinding>
        </SectionModelBinding>
      </Binding>
    </XGenConfig>
    """
    
    When I run the generator
  
    Then I expect 1 generation result
    And an output named "CreateTables.sql" with content:
    """
CREATE TABLE Order AS (
				  Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20)
				  ,recordCreatedOn datetime2(3)
				  )    
GO;CREATE TABLE Customer AS (
				  Id int,  OrderDate datetime,  OrderNumber varchar(50),  CustomerId int,  TotalAmount decimal(12,2),  Id int,  FirstName varchar(50),  LastName varchar(100),  City varchar(50),  Country varchar(3),  Phone varchar(20)
				  ,recordCreatedOn datetime2(3)
				  )    
GO;
    """
	