# FileFormat

## Syntax
``` xml
<FileFormat
  type="<file-format-type>"
  version="<file-format-version>"
  templateType="<template-type>"
  currentAccessor="<current-accessor>"
  childAccessor="<child-accessor>"
  singleLineCommentPrefix="<single-line-comment-prefix>"
  multiLineCommentPrefix="<multi-line-comment-prefix>"
  multiLineCommentSuffix="<multi-line-comment-suffix>"
  commentNodeXPath="<comment-node-xpath>"
  annotationPrefix="<annotation-prefix>"
  annotationArgsPrefix="<annotation-args-prefix>"
  annotationArgsSuffix="<annotation-args-suffix>"
/>
```

## Parameters

| Parameter                    | Description | Default | Remark |
|:---                          |:--- |:--- |:--- |
| type                         | The file format type, see [File format types](#file-format-types). | | |
| version                      | The version of the file format  type. | | |
| templateType[^1]             | The template type, this can be either text or xml. | | | 
| currentAccessor              | The placeholder part for accessing a current attribute. | _ | | 
| childAccessor                | The placeholder part for accessing a child element. | | | 
| singleLineCommentPrefix[^2]  | The single line comment prefix. | | | 
| multiLineCommentPrefix       | The multi-line comment prefix. | | | 
| multiLineCommentSuffix       | The multi-line comment suffix. | | | 
| commentNodeXPath[^3]         | The template comment node XPath, this will be executed on the template and the resulting elements or attributes are inspected for annotations.| | This is only relevant for xml templates. | 
| annotationPrefix             | The prefix for a annotation. | @XGen | | 
| annotationArgsPrefix         | The prefix for the annotation arguments. | ( | | 
| annotationArgsSuffix         | The suffix for the annotation arguments. | ) | | 

## File format types
The supported file format types are:

- ANSI_SQL
- IBM_DataStage
- Informatica_PowerCenter
- Microsoft_SQL
- Microsoft_SSIS

[comment]: Footnotes
[^1]: required parameter
[^2]: when templateType is 'text' this parameter is  required
[^3]: when templateType is 'xml' this parameter is  required