@Unit
Feature: Unit_TextTemplate_Section_Prefix
  In this feature we will describe the section prefix feature in text templates.
  
  Background:
    Given I have the following model:
	    """
				<?xml version="1.0" encoding="UTF-8"?>
				<modeldefinition>
				  <system name="ExampleSource">
				    <mappableObjects>
				      <entity name="ExampleTable">
				        <attributes>
				          <attribute name="FirstColumn" fulldatatype="int" ordinal="1" />
				          <attribute name="SecondColumn" fulldatatype="varchar(50)" />
				          <attribute name="ThirdColumn" fulldatatype="datetime" />
				        </attributes>
				      </entity>
				    </mappableObjects>
				  </system>
				</modeldefinition>
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

  Scenario: Section with prefix on firstOnly
    Given the following template named "Section_Prefix_firstOnly.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" prefix="/** first */" prefixStyle="firstOnly")
        attribute_name attribute_fulldatatype,
        recordCreatedOn datetime2(3)
      );
      GO;
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_firstOnly.sql" with content:
      """
      CREATE TABLE ExampleTable AS (
      /** first */  FirstColumn int,
        SecondColumn varchar(50),
        ThirdColumn datetime,
        recordCreatedOn datetime2(3)
      );
      GO;
      """

  Scenario: Section wih prefix on lastOnly
    Given the following template named "Section_Prefix_lastOnly.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" prefix="/** last */" prefixStyle="lastOnly")
        attribute_name attribute_fulldatatype,
        recordCreatedOn datetime2(3)
      );
      GO;
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_lastOnly.sql" with content:
      """
      CREATE TABLE ExampleTable AS (
        FirstColumn int,
        SecondColumn varchar(50),
      /** last */  ThirdColumn datetime,
        recordCreatedOn datetime2(3)
      );
      GO;
      """

  Scenario: Section with prefix on allButFirst
    Given the following template named "Section_Prefix_allButFirst.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" prefix="," prefixStyle="allButFirst")
        attribute_name attribute_fulldatatype
      );
      GO;
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_allButFirst.sql" with content:
      """
      CREATE TABLE ExampleTable AS (
        FirstColumn int
      ,  SecondColumn varchar(50)
      ,  ThirdColumn datetime
      );
      GO;
      """

  Scenario: Section with prefix on allButLast
    Given the following template named "Section_Prefix_allButLast.sql":
      """
      -- @XGenSection(name="Entity" placeholderOnLastLine="GO;")
      CREATE TABLE entity_name AS (
        -- @XGenSection(name="Attribute" prefix="/* not last */" prefixStyle="allButLast")
        attribute_name attribute_fulldatatype,
        recordCreatedOn datetime2(3)
      );
      GO;
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Section_Prefix_allButLast.sql" with content:
      """
      CREATE TABLE ExampleTable AS (
      /* not last */  FirstColumn int,
      /* not last */  SecondColumn varchar(50),
        ThirdColumn datetime,
        recordCreatedOn datetime2(3)
      );
      GO;
      """
