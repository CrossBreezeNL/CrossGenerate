package com.xbreeze.xgenerate.template.section;

import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation;

/**
 * The sectioned template contains an ordered collection of all sections residing in the template.
 * A template can contain 3 types of sections, namely:
 *  - NamedTemplateSection
 *     This type of section is explicitly defined by the user to be used by a SectionModelBinding.
 *     It does not contain any content of the template, its only a section in the section hierarchy. The content always resides in a RawTemplateSection.
 *  - RawTemplateSection
 *     This type of section resides in the template, but is not used for binding. It needs to be in the resulting template one-on-one.
 *     There can be placeholder's in these type of sections!
 *  - CommentTemplateSection
 *     This type of section is created by using the Comment annotation in the template.
 *     There can be no placeholder's in these type of sections!
 * 
 * To clarify, structure of a template can be:
 *  - Template (NamedTemplateSection)
 *    - RawTemplateSection
 *    - NamedTemplateSection
 *      - RawTemplateSection
 *      - CommentTemplateSection
 *      - NamedTemplateSection
 *        - NamedTemplateSection
 *          - RawTemplateSection
 *        - RawTemplateSection
 *      - RawTemplateSection
 *      - NamedTemplateSection
 *        - RawTemplateSection
 *      - RawTemplateSection
 *    - RawTemplateSection
 * So NamedTemplateSection can be nested. Each section can in turn contain RawTemplateSection, CommentTemplateSection and NamedTemplateSection.
 *
 * @author Harmen
 */
public class SectionedTemplate extends NamedTemplateSection {

	/**
	 * Constructor.
	 * @param rootSectionName The name of the root section.
	 */
	public SectionedTemplate(TemplateSectionAnnotation rootTemplateSectionAnnotation) {
		// Create an implicit TemplateSectionAnnotation for the root of the template.
		super(rootTemplateSectionAnnotation, rootTemplateSectionAnnotation.getAnnotationBeginIndex(), rootTemplateSectionAnnotation.getAnnotationEndIndex());
	}
}
