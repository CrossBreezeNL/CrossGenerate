@Unit
Feature: Unit_Config_Model_ModelAttributeInjection
  In this feature we will describe the ModelAttributeInjection feature in the model config.

  Background: 
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="A"/>
        <entity name="B"/>
        <entity name="C"/>
      </entities>
      """
    And the following template named "Unit_Config_Model_ModelAttributeInjection.txt":
      """
      table_name -> table_type

      """

  Scenario Outline: Single <Scenario> attribute injection
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="<modelXPath>" targetAttribute="type" target<targetType>="<targetValue>" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      A -> <expectedResultA>
      B -> <expectedResultB>
      C -> <expectedResultC>

      """

    Examples: 
      | Scenario      | modelXPath          | targetType | targetValue                | expectedResultA | expectedResultB | expectedResultC |
      | simple value  | //entity            | Value      | simple                     | simple          | simple          | simple          |
      | filter value  | //entity[@name='B'] | Value      | simple                     |                 | simple          |                 |
      | simple XPath  | //entity            | XPath      | ./@name                    | A               | B               | C               |
      | filter XPath  | //entity[@name='B'] | XPath      | ./@name                    |                 | B               |                 |
      | lower-case    | //entity            | XPath      | lower-case(@name)          | a               | b               | c               |

  @KnownIssue
  # KnownIssue: replace function is not implemented in vtd-xml
  Scenario Outline: Single <Scenario> attribute injection @KnownIssue
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="<modelXPath>" targetAttribute="type" target<targetType>="<targetValue>" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      A -> <expectedResultA>
      B -> <expectedResultB>
      C -> <expectedResultC>

      """

    Examples: 
      | Scenario      | modelXPath          | targetType | targetValue                | expectedResultA | expectedResultB | expectedResultC |
      | replace XPath | //entity[@name='B'] | XPath      | replace(./@name, 'B', 'b') |                 | B               |                 |

  Scenario Outline: Single <Scenario> quoted attribute injection
    Given I have the following model:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <entities>
        <entity name="&quot;A&quot;"/>
        <entity name="&quot;B&quot;"/>
        <entity name="&quot;C&quot;"/>
      </entities>
      """
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="<modelXPath>" targetAttribute="type" target<targetType>="<targetValue>" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      "A" -> <expectedResultA>
      "B" -> <expectedResultB>
      "C" -> <expectedResultC>

      """

    Examples: 
      | Scenario      | modelXPath          | targetType | targetValue                | expectedResultA | expectedResultB | expectedResultC |
      | lower-case    | //entity            | XPath      | lower-case(@name)          | "a"             | "b"             | "c"             |

  Scenario: Multiple attribute injection
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="//entity[@name='A']" targetAttribute="type" targetValue="The entity is A" />
            <ModelAttributeInjection modelXPath="//entity[@name='B']" targetAttribute="type" targetValue="The entity is B" />
            <ModelAttributeInjection modelXPath="//entity[@name='C']" targetAttribute="type" targetValue="The entity is C" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      A -> The entity is A
      B -> The entity is B
      C -> The entity is C

      """

  Scenario: Inject an already existing attribute
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="//entity[@name='A']" targetAttribute="type" targetValue="The entity was A" />
            <ModelAttributeInjection modelXPath="//entity[@name='B']" targetAttribute="type" targetValue="The entity was B" />
            <ModelAttributeInjection modelXPath="//entity[@name='C']" targetAttribute="type" targetValue="The entity was C" />
            <ModelAttributeInjection modelXPath="//entity[@name='A']" targetAttribute="name" targetValue="NewA" />
            <ModelAttributeInjection modelXPath="//entity[@name='B']" targetAttribute="name" targetValue="NewB" />
            <ModelAttributeInjection modelXPath="//entity[@name='C']" targetAttribute="name" targetValue="NewC" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      NewA -> The entity was A
      NewB -> The entity was B
      NewC -> The entity was C

      """

  Scenario: Inject using target xpath
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="//entity" targetAttribute="type" targetXPath="concat('The entity was ', @name)" />
            <ModelAttributeInjection modelXPath="//entity" targetAttribute="name" targetXPath="concat('New', @name)" />
            
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Config_Model_ModelAttributeInjection.txt" with content:
      """
      NewA -> The entity was A
      NewB -> The entity was B
      NewC -> The entity was C

      """

  Scenario: Inject using incorrect modelXPath
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="concats('test','test')" targetAttribute="type" targetXPath="concat('The entity was ', @name)" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect the following error message:
      """
      com.xbreeze.xgenerate.model.ModelPreprocessorException: Error while processing model attribute injection for model XPath ´concats('test','test')´: Syntax error after or around the end of ´concats´
      """

  Scenario: Inject using incorrect targetXPath
    Given the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>
      <XGenConfig>
        <Model>
          <ModelAttributeInjections>
            <ModelAttributeInjection modelXPath="//entity" targetAttribute="type" targetXPath="concats('The entity was ', @name)" />
          </ModelAttributeInjections>
        </Model>
        <TextTemplate rootSectionName="Template">
          <Output type="single_output" />
        </TextTemplate>
        <Binding>
          <SectionModelBinding section="Template" modelXPath="/entities/entity" placeholderName="table" />
        </Binding>
      </XGenConfig>
      """
    When I run the generator
    Then I expect the following error message:
      """
      com.xbreeze.xgenerate.model.ModelPreprocessorException: Error while processing model attribute injection for target XPath ´concats('The entity was ', @name)´: Syntax error after or around the end of ´concats´
      """
