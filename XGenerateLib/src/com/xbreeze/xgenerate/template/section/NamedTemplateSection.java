package com.xbreeze.xgenerate.template.section;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.binding.PlaceholderConfig;
import com.xbreeze.xgenerate.config.binding.SectionModelBindingConfig;
import com.xbreeze.xgenerate.template.PreprocessedTemplate;

public class NamedTemplateSection extends TemplateSection {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(NamedTemplateSection.class.getName());
	
	/**
	 * The name of the section.
	 */
	private String _sectionName;
	
	/**
	 * The list containing all sections of the template section in the right order.
	 */
	private ArrayList<TemplateSection> _templateSections;
	
	/**
	 * Constructor.
	 * @param sectionName The name of the section.
	 * @param sectionBeginIndex The section begin index.
	 * @param sectionEndIndex The section end index.
	 */
	public NamedTemplateSection(String sectionName, int sectionBeginIndex) {
		super(sectionBeginIndex, -1);
		this._sectionName = sectionName;
		// Initialize the _templateSections list.
		this._templateSections = new ArrayList<TemplateSection>();
	}
	
	/**
	 * @return the section name
	 */
	public String getSectionName() {
		return _sectionName;
	}

	/**
	 * @param sectionName the section name to set
	 */
	public void setSectionName(String sectionName) {
		this._sectionName = sectionName;
	}
	
	/**
	 * Add a template section to the list of sections.
	 * @param templateSection The template section to add.
	 */
	public void addTemplateSection(TemplateSection templateSection) {
		_templateSections.add(templateSection);
	}
	
	/**
	 * Get the list of template sections of this section.
	 * @return The list of template sections.
	 */
	public ArrayList<TemplateSection> getTemplateSections() {
		return this._templateSections;
	}
	
	public void appendTemplateXslt(PreprocessedTemplate preprocessedTemplate, XGenConfig config, SectionModelBindingConfig parentBindingConfig) {
		
		// Add a comment in the XSLT marking the section start.
		preprocessedTemplate.append("<!-- Section begin: %s -->", this._sectionName);
		
		// Loop through the template sections and add the needed parts to the pre-processed template (XSLT).
		for (TemplateSection templateSection : this.getTemplateSections()) {
			
			// CommentTemplateSection
			if (templateSection instanceof CommentTemplateSection) {
				CommentTemplateSection commentTemplateSection = (CommentTemplateSection) templateSection;
				// Transform CommentTemplateSection into XSLT comments.
				// '-- @XGenComment(Some comment written here)' => '<xsl:comment>Some comment written here</xsl:comment>'.
				preprocessedTemplate.append("<!-- Comment: %s -->", commentTemplateSection.getComment());
			}
			
			// NamedTemplateSection
			else if (templateSection instanceof NamedTemplateSection) {
				NamedTemplateSection namedTemplateSection = (NamedTemplateSection) templateSection;
				// Transform NamedTemplateSection into <xsl:foreach ...>
				// Using SectionModelBinding info: <SectionModelBinding name="SomeSection" modelXPath="/System/MappableObjects/Entity" placeholderName="Entity" />
				// '-- @XGenSection(name="SomeSection")\n Element_Name\n' => '<xsl:for-each select="/System/MappableObjects/Entity"> Element_Name\n</xsl:for-each>'
				// The placeholder replacement is done in the next phase.
				
				// Get the local section model bindings for the section.
				SectionModelBindingConfig[] sectionModelBindingConfigs = parentBindingConfig.getSectionModelBindingConfigs(namedTemplateSection.getSectionName());
				/**
				// Merge an array of parent and global model binding configs.
				SectionModelBindingConfig[] sectionModelBindingConfigs = ArrayUtils.addAll(
						// Get the parent model binding configs for this section.
						parentBindingConfig.getSectionModelBindingConfigs(namedTemplateSection.getSectionName())//,
						// Get the global model binding configs for this section.
						// TODO: Only look into the global config if not the parent, or create a function to recursively walk to the parents and check for section config.
						//config.getBindingConfig().getSectionModelBindingConfigs(namedTemplateSection.getSectionName())
				);*/
				
				// Repeat the template for each section binding.
				if (sectionModelBindingConfigs.length > 0) {
					// For each section model binding, repeat the content of the section.
					for (SectionModelBindingConfig sectionModelBindingConfig : sectionModelBindingConfigs) {
						// Append the start of the for-each.
						preprocessedTemplate.append("<xsl:for-each select=\"%s\">", sectionModelBindingConfig.getModelXPath());
						// Append the Xstl of the named template section.
						// The content of the template section needs to be resolved before adding the content here. Recursive call needed.
						namedTemplateSection.appendTemplateXslt(preprocessedTemplate, config, sectionModelBindingConfig);
						// Append the end of the for-each.
						preprocessedTemplate.append("</xsl:for-each>");
					}
				}
				
				// If there is no section model binding, log a warning message.
				else {
					// If there is no binding we log a warning and add the contents of the named template without a xsl:for-each.
					logger.warning(String.format("There is no section model binding configured for section '%s'", namedTemplateSection.getSectionName()));
					// Append the Xstl of the named template section with the parent binding config.
					namedTemplateSection.appendTemplateXslt(preprocessedTemplate, config, parentBindingConfig);		
				}
			}
			
			// RawTemplateSection
			else if (templateSection instanceof RawTemplateSection) {
				RawTemplateSection rawTemplateSection = (RawTemplateSection) templateSection;
				
				// Store the template content in a local variable.
				String placeholderProcessedTemplateContent = rawTemplateSection.getContent();
				// Get the child accessor from the config.
				String childAccessor = config.getTemplateConfig().getFileFormatConfig().getCurrentAccessor();
				
				// Process the placeholder of this section.
				//logger.info("Processing local placeholder...");
				placeholderProcessedTemplateContent = processPlaceholder(
						parentBindingConfig.getPlaceholderName(),
						// The path of the placeholder of the current section is always the local element (so '.').
						".",
						placeholderProcessedTemplateContent,
						childAccessor
				);
				
				// Loop through the placeholders defined in this section binding and process them.
				if (parentBindingConfig.getPlaceholderConfigs() != null) {
					for (PlaceholderConfig placeholder : parentBindingConfig.getPlaceholderConfigs()) {
						placeholderProcessedTemplateContent = processPlaceholder(
								placeholder.getName(),
								placeholder.getModelXPath(),
								placeholderProcessedTemplateContent,
								childAccessor
						);
					}
				}
				
				// Append the raw template section into the pre-processed template.
				preprocessedTemplate.append("<!-- Raw begin -->");
				preprocessedTemplate.append(placeholderProcessedTemplateContent);
				preprocessedTemplate.append("<!-- Raw end -->");
			}
			
			// If we get a TemplateSection we don't handle, throw an exception.
			// TODO: Throw the exception.
			else {
				// A TemplateSection is given which we can't handle yet. Log a severe error.
				logger.severe(String.format("A TemplateSection type (%s) was used which can't be translated to Xslt (yet)!", templateSection.getClass().getName()));
			}
		}
		
		// Add a comment in the XSLT marking the section end.
		preprocessedTemplate.append("<!-- Section end: %s -->", this._sectionName);
		
	}
	
	/**
	 * Function to process a placeholder.
	 * @param placeholderName The placeholder name.
	 * @param modelXPath The model XPath
	 * @param templatePartToProcess The template part to process
	 * @param currentAccessor The current accessor as defined in the config.
	 * @return The processed template, where placeholders are replaced with XSLT expressions.
	 */
	private String processPlaceholder(String placeholderName, String modelXPath, String templatePartToProcess, String currentAccessor) {
		/**
		 * The regex to find a placeholder:
		 * %s            - The placeholder name
		 * %s            - The current accessor
		 * ([a-zA-Z]+)   - The attribute name to select
		 */
		String placeholderRegex = String.format(
				"%s%s([a-zA-Z]+)",
				placeholderName,
				currentAccessor
		);
		
		/**
		 * The replacement value for the placeholder (the XSLT to select a value):
		 * <xsl:value-of select=\"<replacement-regex>\" />
		 * 
		 * replacement-regex:
		 * %s   - The model XPath expression for the placeholder.
		 * /    - A slash to select something from the element at the level of %s.
		 * @    - The XML attribute accessor.
		 * $1   - The value of group 1 (the attribute name to select from the find regex)
		 */
		// Group 1: The attribute name.
		String placeholderReplacement = String.format(
				"<xsl:value-of select=\"%s/@$1\" />"
				,modelXPath
		);

		// Perform the replacement for the placeholder.
		//logger.info(String.format("Processing placeholder '%s': '%s' -> %s", placeholderName, placeholderRegex, placeholderReplacement));
		return templatePartToProcess.replaceAll(placeholderRegex, placeholderReplacement);
	}
}
