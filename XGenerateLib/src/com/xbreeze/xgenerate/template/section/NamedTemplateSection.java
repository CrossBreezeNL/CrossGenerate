package com.xbreeze.xgenerate.template.section;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.binding.SectionModelBindingConfig;
import com.xbreeze.xgenerate.template.PreprocessedTemplate;

public class NamedTemplateSection extends TemplateSection {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(NamedTemplateSection.class.getName());
	
	public static final String PLACEHOLDER_PLACEHOLDER_NAME = "{{PLACEHOLDER_NAME}}";
	
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
				
				// Process the placeholder-name placeholder.
				// This placeholder is injected during TemplatePlaceholderInjection in XML templates.
				// TODO Maybe handle this a bit smarter during injection of the placeholder somehow?
				// ^ This is bit of chicken - egg problem where you can have multiple placeholder names?
				// ^ Bit if placeholder names would differ the template would probably not work right?
				// ^ Cause the sections are repeated, but placeholder can't be different for same section.
				// ^ Maybe with this information we can check the unique placeholder names upfront and store
				// ^ Them in this object to be used in the XMLPreprocessor.
				if (placeholderProcessedTemplateContent.indexOf(PLACEHOLDER_PLACEHOLDER_NAME) != -1) {
					logger.info("Placeholder name placeholder found, replacing with right name");
					placeholderProcessedTemplateContent = placeholderProcessedTemplateContent.replaceAll(Pattern.quote(PLACEHOLDER_PLACEHOLDER_NAME), parentBindingConfig.getPlaceholderName());
				}
				
				// Process the placeholder of this section.
				placeholderProcessedTemplateContent = PreprocessedTemplate.processPlaceholders(placeholderProcessedTemplateContent, parentBindingConfig, config.getTemplateConfig().getFileFormatConfig());
				
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
	

}
