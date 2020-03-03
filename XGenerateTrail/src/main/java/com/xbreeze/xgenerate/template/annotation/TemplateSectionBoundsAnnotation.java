package com.xbreeze.xgenerate.template.annotation;

public class TemplateSectionBoundsAnnotation extends TemplateAnnotation {
	private TemplateSectionAnnotation _templateSectionAnnotation;
	
	public TemplateSectionBoundsAnnotation(TemplateSectionAnnotation templateSectionAnnotation, int annotationBeginIndex, int annotationEndIndex) {
		this._templateSectionAnnotation = templateSectionAnnotation;
		setAnnotationBeginIndex(annotationBeginIndex);
		setAnnotationEndIndex(annotationEndIndex);
	}
	
	public String getName() {
		return _templateSectionAnnotation.getName();
	}

	/**
	 * @return the templateSectionAnnotation
	 */
	public TemplateSectionAnnotation getTemplateSectionAnnotation() {
		return _templateSectionAnnotation;
	}

	/**
	 * @param templateSectionAnnotation the templateSectionAnnotation to set
	 */
	public void setTemplateSectionAnnotation(TemplateSectionAnnotation templateSectionAnnotation) {
		this._templateSectionAnnotation = templateSectionAnnotation;
	}
}
