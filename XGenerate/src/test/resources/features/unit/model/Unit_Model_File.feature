@Unit
Feature: Unit_Model_Xml_Include
  In this feature we will describe the feature to use Xml Include instructions in the model file.

  Scenario: Xml include in model file
    Given I have the following model file: "example-model.xml"
    And the following config:
      """
      <?xml version="1.0" encoding="UTF-8"?>            
      <XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
        <TextTemplate rootSectionName="Template">
          <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
          <Output type="single_output" />
        </TextTemplate>              
        <Binding>        
					<SectionModelBinding section="Template" modelXPath ="/system" placeholderName="system">
						<SectionModelBinding section="Tables" modelXPath="./entities/entity" placeholderName="table">
							<Placeholders>
								<Placeholder name="system" modelXPath="../.." />
							</Placeholders>
						</SectionModelBinding>
					</SectionModelBinding>
        </Binding>          
      </XGenConfig>
      """
    And the following template named "Unit_Model_File.txt":
      """
      -- @XGenTextSection(name="Tables")
      table_name
      
      """
    When I run the generator
    Then I expect 1 generation result
    And an output named "Unit_Model_File.txt" with content:
      """
      A
      
      """