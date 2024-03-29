/*******************************************************************************
 *   Copyright (c) 2021 CrossBreeze
 *
 *   This file is part of CrossGenerate.
 *
 *      CrossGenerate is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      CrossGenerate is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with CrossGenerate.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *  Contributors:
 *      Willem Otten - CrossBreeze
 *      Harmen Wessels - CrossBreeze
 *      Jacob Siemaszko - CrossBreeze
 *  
 *******************************************************************************/
package com.xbreeze.xgenerate.template.section;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.xbreeze.xgenerate.config.XGenConfig;
import com.xbreeze.xgenerate.config.binding.PlaceholderConfig;
import com.xbreeze.xgenerate.config.binding.SectionModelBindingConfig;
import com.xbreeze.xgenerate.template.TemplatePreprocessorException;
import com.xbreeze.xgenerate.template.XsltTemplate;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionAction;
import com.xbreeze.xgenerate.template.annotation.TemplateTextSectionAnnotation.RepetitionStyle;

public class NamedTemplateSection extends TemplateSection {
	// The logger for this class.
	private static final Logger logger = Logger.getLogger(NamedTemplateSection.class.getName());
	
	public static final String PLACEHOLDER_PLACEHOLDER_NAME = "{{PLACEHOLDER_NAME}}";
	
	/**
	 * The template section annotation.
	 * Used to generate the right XSLT.
	 */
	private TemplateSectionAnnotation _templateSectionAnnotation;
	
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
	public NamedTemplateSection(TemplateSectionAnnotation templateSectionAnnotation, int sectionBeginIndex, int sectionEndIndex) {
		super(sectionBeginIndex, sectionEndIndex);
		// Set the template section annotation.
		this._templateSectionAnnotation = templateSectionAnnotation;
		// Initialize the _templateSections list.
		this._templateSections = new ArrayList<TemplateSection>();
	}
	
	/**
	 * @return the section name
	 */
	public String getSectionName() {
		return _templateSectionAnnotation.getName();
	}
	
	public boolean isUserDefinedSectionName() {
		return _templateSectionAnnotation.isUserDefinedSectionName();
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
	
	public void appendTemplateXslt(XsltTemplate xsltTemplate, XGenConfig config, SectionModelBindingConfig parentBindingConfig) throws TemplatePreprocessorException {
		
		// Add a comment in the XSLT marking the section start.
		xsltTemplate.append("<!-- Section begin: %s -->", this.getSectionName());
		
		// If the variable name is set for the section model binding, inject the xsl-variable part.
		if (parentBindingConfig.hasVariableName()) {
			logger.info(String.format("Creating variable '%s' for SectionModelBinding '%s'.", parentBindingConfig.getVariableName(), parentBindingConfig.getSectionName()));
			xsltTemplate.append("<xsl:variable name=\"%s\" select=\".\" />", parentBindingConfig.getVariableName());
		}
		
		// Add a variable for each placeholder which has a variableName defined.
		// If a variable is defined for a placeholder, we use the variable i.s.o. replacing the placeholders with the modelXPath in XsltTemplate.processPlaceholders().
		if (parentBindingConfig.getPlaceholderConfigs() != null) {
			for (PlaceholderConfig placeholderWithVariable : parentBindingConfig.getPlaceholderConfigs().stream().filter(placeholder -> placeholder.hasVariableName()).collect(Collectors.toList())) {
				logger.info(String.format("Creating variable '%s' for Placeholder '%s'.", placeholderWithVariable.getVariableName(), placeholderWithVariable.getName()));
				xsltTemplate.append("<xsl:variable name=\"%s\" select=\"%s\" />", placeholderWithVariable.getVariableName(), placeholderWithVariable.getModelXPath());
			}
		}
		
		// Loop through the template sections and add the needed parts to the pre-processed template (XSLT).
		for (TemplateSection templateSection : this.getTemplateSections()) {
			
			// CommentTemplateSection
			if (templateSection instanceof CommentTemplateSection) {
				CommentTemplateSection commentTemplateSection = (CommentTemplateSection) templateSection;
				// Transform CommentTemplateSection into XSLT comments.
				// '-- @XGenComment(Some comment written here)' => '<xsl:comment>Some comment written here</xsl:comment>'.
				xsltTemplate.append("<!-- Comment: %s -->", commentTemplateSection.getComment());
			}
			
			// NamedTemplateSection
			else if (templateSection instanceof NamedTemplateSection) {
				NamedTemplateSection namedTemplateSection = (NamedTemplateSection) templateSection;
				// Transform NamedTemplateSection into <xsl:foreach ...>
				// Using SectionModelBinding info: <SectionModelBinding name="SomeSection" modelXPath="/System/MappableObjects/Entity" placeholderName="Entity" />
				// '-- @XGenTextSection(name="SomeSection")\n Element_Name\n' => '<xsl:for-each select="/System/MappableObjects/Entity"> Element_Name\n</xsl:for-each>'
				// The placeholder replacement is done in the next phase.
				
				// Get the local section model bindings for the section.
				SectionModelBindingConfig[] sectionModelBindingConfigs = parentBindingConfig.getSectionModelBindingConfigs(namedTemplateSection.getSectionName());

				// Repeat the template for each section binding.
				if (sectionModelBindingConfigs != null && sectionModelBindingConfigs.length > 0) {
					// For each section model binding, repeat the content of the section.
					for (SectionModelBindingConfig sectionModelBindingConfig : sectionModelBindingConfigs) {
						
						// Append the start of the for-each.
						xsltTemplate.append("<xsl:for-each select=\"%s\">", sectionModelBindingConfig.getModelXPath());
						
						// Append the Xstl of the named template section.
						// The content of the template section needs to be resolved before adding the content here. Recursive call needed.
						namedTemplateSection.appendTemplateXslt(xsltTemplate, config, sectionModelBindingConfig);
						
						// Append the end of the for-each.
						xsltTemplate.append("</xsl:for-each>");
					}
				}
				
				// If there is no section model binding, log a warning message.
				else {
					// If the section name is set, but there is no binding, log a warning.
					// The section name can be empty for the root section of a text templates which resides in a XML template.
					// For these sections (where the section name is not defined by the user) we don't log a warning.
					if (namedTemplateSection.isUserDefinedSectionName()) {
						// If there is no binding we log a warning and add the contents of the named template without a xsl:for-each.
						logger.warning(String.format("There is no section model binding configured for section '%s'", namedTemplateSection.getSectionName()));
					}
					// Append the Xstl of the named template section with the parent binding config.
					namedTemplateSection.appendTemplateXslt(xsltTemplate, config, parentBindingConfig);		
				}
			}
			
			// RepetitionTemplateSection (Prefix or Suffix section)
			// It's important this section type is handled before the raw template section, since its a sub-type.
			else if (templateSection instanceof RepetitionTemplateSection) {
				RepetitionTemplateSection repetitionTemplateSection = (RepetitionTemplateSection)templateSection;
				// Append the repetition xslt for this section.
				xsltTemplate.append(getRepetitionXSLT(repetitionTemplateSection.getContent(), repetitionTemplateSection.getRepetitionStyle(), repetitionTemplateSection.getRepetitionAction()));
			}
			
			// RawTemplateSection
			else if (templateSection instanceof RawTemplateSection) {
				RawTemplateSection rawTemplateSection = (RawTemplateSection) templateSection;
				
				// Store the template content in a local variable.
				String processedRawTemplateContent = rawTemplateSection.getContent();
				
				// Replace the carriage return character with the XML encoded version so it's processed correctly in the XSLT engine.
				processedRawTemplateContent = processedRawTemplateContent.replace("\r", "&#13;");
				
				//Determine the appropriate placeholderName to use (from parent binding config or from section annotation if specified)
				String placeholderName = parentBindingConfig.getPlaceholderName();
				if (this._templateSectionAnnotation.getPlaceholderName() != null) {
					placeholderName = this._templateSectionAnnotation.getPlaceholderName();
				}
				// Process the placeholder-name placeholder.
				// This placeholder is injected during TemplatePlaceholderInjection in XML templates.
				// TODO Maybe handle this a bit smarter during injection of the placeholder somehow?
				// ^ This is bit of chicken - egg problem where you can have multiple placeholder names?
				// ^ But if placeholder names would differ the template would probably not work right?
				// ^ Cause the sections are repeated, but placeholder can't be different for same section.
				// ^ Maybe with this information we can check the unique placeholder names upfront and store
				// ^ Them in this object to be used in the XMLPreprocessor.
				if (processedRawTemplateContent.indexOf(PLACEHOLDER_PLACEHOLDER_NAME) != -1) {
					logger.fine("Placeholder name placeholder found, replacing with right name");
					/**
					 * TODO: This is a simple way to inject a if for placeholder injections. Should be designed properly later.
					 * In the template we find something like 'someAttribute="{{PLACEHOLDER_NAME}}_someModelNode"'
					 * These placeholders are there because of injection.
					 * We would like to add a <xsl:if> around this attribute so it is only there when there is a value.
					 */
					/**
					 * The regex to find a placeholder injected attribute:
					 * group 1:
					 *   [ \\t\\r\\n]* - Whitespace (space, tab or new-line)
					 *   
					 * group 2:
					 *   ([a-zA-Z]+:)? - The namespace prefix (group 3)
					 *   [a-zA-Z_-]+   - The attribute name
					 *   [ \\t]*       - Whitespace (space or tab)
					 *   =             - Assignment operator
					 *   [ \\t]*       - Whitespace (space or tab)
					 *   \"            - Opening quote
					 *  
					 * group 4:
					 *   %s            - The placeholder for placeholder name.
					 * 
					 * group 5:
					 *   %s            - Current accessor
					 *   
					 * group 6:
					 *   [a-zA-Z]+     - The attribute name to select
					 *   
					 * group 7:
					 *   \"            - The closing quote
					 */
					String placeholderRegex =  String.format(
							"([ \\t\\r\\n]*)(([a-zA-Z]+:)?[a-zA-Z_-]+[ \\t]*=[ \\t]*\")(%s)(%s)([a-zA-Z]+)(\")",
							Pattern.quote(PLACEHOLDER_PLACEHOLDER_NAME),
							Pattern.quote(config.getTemplateConfig().getFileFormatConfig().getCurrentAccessor())
					);
					processedRawTemplateContent = processedRawTemplateContent.replaceAll(placeholderRegex, String.format("</xsl:text><xsl:if test=\"./@$6\"><xsl:text>$1$2%s$5$6$7</xsl:text></xsl:if><xsl:text>", placeholderName));
					
					// When the placeholder wasn't replaced in the previous replacement, its due to it not being an attribute (probably).
					// So we replace only the placeholder now.
					processedRawTemplateContent = processedRawTemplateContent.replaceAll(Pattern.quote(PLACEHOLDER_PLACEHOLDER_NAME), placeholderName);
				}
				
				// Process the placeholder of this section.
				processedRawTemplateContent = XsltTemplate.processPlaceholders(processedRawTemplateContent, parentBindingConfig, config.getTemplateConfig(), placeholderName);
				
				// Process the literals defined for this section.
				processedRawTemplateContent = XsltTemplate.processLiterals(processedRawTemplateContent, parentBindingConfig, config.getTemplateConfig());
				
				// Append the raw template section into the pre-processed template.
				xsltTemplate.append("<!-- Raw begin -->");
				xsltTemplate.append("<xsl:text>");
				xsltTemplate.append(processedRawTemplateContent);
				xsltTemplate.append("</xsl:text>");
				xsltTemplate.append("<!-- Raw end -->");
			}
			
			// If we get a TemplateSection we don't handle, throw an exception.
			// TODO: Throw the exception.
			else {
				// A TemplateSection is given which we can't handle yet. Log a severe error.
				logger.severe(String.format("A TemplateSection type (%s) was used which can't be translated to Xslt (yet)!", templateSection.getClass().getName()));
			}
		}
		
		// Add a comment in the XSLT marking the section end.
		xsltTemplate.append("<!-- Section end: %s -->", this.getSectionName());
		
	}
	
	/**
	 * Get the XSLT for the repetion.
	 * @param prefixOrSuffix The prefix or suffix.
	 * @param style The repetition style.
	 * @param action The repetition action.
	 * @return The prefix or suffix XSLT.
	 * @throws TemplatePreprocessorException
	 */
	private String getRepetitionXSLT(String prefixOrSuffix, RepetitionStyle style, RepetitionAction action) throws TemplatePreprocessorException {
		String condition;
		switch (style) {
			case allButFirst: 
				condition = "position() != 1";
				break;
			case allButFirstAndLast:
				condition = "position() != 1 and position() != last()";
				break;
			case allButLast:
				condition = "position() != last()";
				break;
			case firstOnly:
				condition = "position() = 1";
				break;
			case lastOnly:
				condition = "position() = last()";
				break;
			case all:
				// When the style is 'all' we want the repetition section to always be included. So we have a condition which is always true.
				condition = "1 = 1";
				break;
			default:
				throw new TemplatePreprocessorException(String.format("Unrecognized repetition style specified: %s", style));
		}
		
		switch (action) {
			case add:
				// Return the XSLT for the prefix or suffix.
				return String.format("<xsl:if test=\"%s\"><text>%s</text></xsl:if>", condition, prefixOrSuffix);
			default:
				throw new TemplatePreprocessorException(String.format("Unrecognized repetition action specified: %s", action));
		}
	}

	/**
	 * @return the _templateSectionAnnotation
	 */
	public TemplateSectionAnnotation getTemplateSectionAnnotation() {
		return _templateSectionAnnotation;
	}	
}
