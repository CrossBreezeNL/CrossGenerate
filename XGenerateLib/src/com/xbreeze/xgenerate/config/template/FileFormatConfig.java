package com.xbreeze.xgenerate.config.template;

import javax.xml.bind.annotation.XmlAttribute;

import com.xbreeze.xgenerate.template.TemplateType;

/**
 * The FileFormat represents the configuration of the template its file format.
 * One can specify the type and version of a file format.
 * 
 * @author Harmen
 */
public class FileFormatConfig {
	/**
	 * The file formats supported by CrossGenerate.
	 * For each file format in this enumeration there should be a configuration available in the fileformat folder.
	 */
	public enum FileFormatType {
		ANSI_SQL,
		Microsoft_SQL,
		Microsoft_SSIS,
		Informatica_PowerCenter,
		IBM_DataStage
	}
	
	/**
	 * The FileFormatType of the Template.
	 * @see FileFormatType
	 */
	private FileFormatType _type;
	
	/**
	 * The version of the FileFormat.
	 * For example when the FileFormatType is Microsoft_SSIS, one could specify 2016 here to point to Microsoft SQL Server 2016 here.
	 */
	private String _version;
	
	/**
	 * The type of the template.
	 * @see TemplateType
	 */
	private TemplateType templateType;
	
	/**
	 * The current accessor.
	 */
	private String _currentAccessor;
	
	/**
	 * The child accessor.
	 */
	private String _childAccessor;
	
	/**
	 * When the template type is text, this property can be used to specify the prefix in the template file which defines the start of a  single line comment.
	 * For example in SQL this is '--'.
	 */
	private String _singleLineCommentPrefix;
	
	/**
	 * When the template type is text, this property can be used to specify the prefix in the template file which defines the start of a multi-line comment.
	 * For example in SQL this is /*
	 */
	private String _multiLineCommentPrefix;
	
	/**
	 * When the template type is text, this property can be used to specify the prefix in the template file which defines the end of a multi-line comment.
	 * For example in SQL this is *&#47; (/) 
	 */
	private String multiLineCommentSuffix;
	
	/**
	 * When the template type is xml, the location of a comment relative to the current element can be specified here.
	 * For example, with the following xml:
	 *   <SomeElement>
	 *     <SomeValue>Bla</SomeValue>
	 *     <Description>Some description</Description>
	 *   </SomeElement>
	 * The 'Description' element could be used to store comments. In this case the XPath to the comment would be 'Description'
	 * 
	 * In another example, with the following xml:
	 *   <SomeElement someValue="Bla" description="Some description" />
	 * The 'description' attribute could be used to store comments. In this case the XPath to the comment would be '@description'.
	 */
	private String _commentNodeXPath;
	
	/**
	 * The prefix in the template file which defines the start of an annotation.
	 * For example in SQL we advise to use '@XGen'. So when specifying a section the annotation would be '@XGenSection'.
	 */
	private String _annotationPrefix;
	
	/**
	 * The prefix for specifying the annotation arguments.
	 * For example '@XGenComment(Some comment)' <- The '(' is the prefix.
	 */
	private String _annotationArgsPrefix;
	
	/**
	 * The prefix for specifying the annotation arguments.
	 * For example '@XGenComment(Some comment)' <- The ')' is the suffix.
	 */	
	private String _annotationArgsSuffix;

	/**
	 * @return the type
	 */
	@XmlAttribute
	public FileFormatType getType() {
		return _type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(FileFormatType type) {
		this._type = type;
	}

	/**
	 * @return the version
	 */
	@XmlAttribute
	public String getVersion() {
		return _version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this._version = version;
	}
	
	/**
	 * @return the templateType
	 */
	@XmlAttribute
	public TemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType the templateType to set
	 */
	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}
	
	/**
	 * @return the currentAccessor
	 */
	@XmlAttribute
	public String getCurrentAccessor() {
		return _currentAccessor;
	}

	/**
	 * @param currentAccessor the currentAccessor to set
	 */
	public void setCurrentAccessor(String currentAccessor) {
		this._currentAccessor = currentAccessor;
	}

	/**
	 * @return the childAccessor
	 */
	@XmlAttribute
	public String getChildAccessor() {
		return _childAccessor;
	}

	/**
	 * @param childAccessor the childAccessor to set
	 */
	public void setChildAccessor(String childAccessor) {
		this._childAccessor = childAccessor;
	}
	
	/**
	 * @return the singleLineCommentPrefix
	 */
	@XmlAttribute
	public String getSingleLineCommentPrefix() {
		return _singleLineCommentPrefix;
	}

	/**
	 * @param singleLineCommentPrefix the singleLineCommentPrefix to set
	 */
	public void setSingleLineCommentPrefix(String singleLineCommentPrefix) {
		this._singleLineCommentPrefix = singleLineCommentPrefix;
	}

	/**
	 * @return the multiLineCommentPrefix
	 */
	@XmlAttribute
	public String getMultiLineCommentPrefix() {
		return _multiLineCommentPrefix;
	}

	/**
	 * @param multiLineCommentPrefix the multiLineCommentPrefix to set
	 */
	public void setMultiLineCommentPrefix(String multiLineCommentPrefix) {
		this._multiLineCommentPrefix = multiLineCommentPrefix;
	}

	/**
	 * @return the multiLineCommentSuffix
	 */
	@XmlAttribute
	public String getMultiLineCommentSuffix() {
		return multiLineCommentSuffix;
	}

	/**
	 * @param multiLineCommentSuffix the multiLineCommentSuffix to set
	 */
	public void setMultiLineCommentSuffix(String multiLineCommentSuffix) {
		this.multiLineCommentSuffix = multiLineCommentSuffix;
	}
	
	/**
	 * @return the commentNodeXPath
	 */
	@XmlAttribute
	public String getCommentNodeXPath() {
		return _commentNodeXPath;
	}

	/**
	 * @param commentNodeXPath the commentNodeXPath to set
	 */
	public void setCommentNodeXPath(String commentNodeXPath) {
		this._commentNodeXPath = commentNodeXPath;
	}

	/**
	 * @return the annotationPrefix
	 */
	@XmlAttribute
	public String getAnnotationPrefix() {
		return _annotationPrefix;
	}

	/**
	 * @param annotationPrefix the annotationPrefix to set
	 */
	public void setAnnotationPrefix(String annotationPrefix) {
		this._annotationPrefix = annotationPrefix;
	}

	/**
	 * @return the annotationArgsPrefix
	 */
	@XmlAttribute
	public String getAnnotationArgsPrefix() {
		return _annotationArgsPrefix;
	}

	/**
	 * @param annotationArgsPrefix the annotationArgsPrefix to set
	 */
	public void setAnnotationArgsPrefix(String annotationArgsPrefix) {
		this._annotationArgsPrefix = annotationArgsPrefix;
	}

	/**
	 * @return the annotationArgsSuffix
	 */
	@XmlAttribute
	public String getAnnotationArgsSuffix() {
		return _annotationArgsSuffix;
	}

	/**
	 * @param annotationArgsSuffix the annotationArgsSuffix to set
	 */
	public void setAnnotationArgsSuffix(String annotationArgsSuffix) {
		this._annotationArgsSuffix = annotationArgsSuffix;
	}
}
