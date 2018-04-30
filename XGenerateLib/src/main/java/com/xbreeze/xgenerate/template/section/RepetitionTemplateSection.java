package com.xbreeze.xgenerate.template.section;

import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation.RepetitionAction;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation.RepetitionStyle;
import com.xbreeze.xgenerate.template.annotation.TemplateSectionAnnotation.RepetitionType;

public class RepetitionTemplateSection extends RawTemplateSection {
	
	private RepetitionType _reptitionType;
	private RepetitionStyle _repetitionStyle;
	private RepetitionAction _repetitionAction;
	
	/**
	 * Constructor.
	 * @param content The content of the repetition section.
	 * @param sectionBeginIndex The section begin index.
	 * @param sectionEndIndex The section end index.
	 */
	public RepetitionTemplateSection(String repetitionContent, int sectionIndex, RepetitionType reptitionType, RepetitionStyle repetitionStyle, RepetitionAction repetitionAction) {
		super(repetitionContent, sectionIndex, sectionIndex);
		this._reptitionType = reptitionType;
		this._repetitionStyle = repetitionStyle;
		this._repetitionAction = repetitionAction;
	}
	
	public RepetitionType getReptitionType() {
		return _reptitionType;
	}

	public RepetitionStyle getRepetitionStyle() {
		return _repetitionStyle;
	}

	public RepetitionAction getRepetitionAction() {
		return _repetitionAction;
	}
}
