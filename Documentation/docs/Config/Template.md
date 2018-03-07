# Template configuration

## Template
### Syntax

```xml
<Template rootSectionName="<root-section-name>">
  <FileFormat ... />
  <Output ... />
  <Sections>
    <Section ... />
  </Sections>
  <TemplateAttributeInjections>
    <TemplateAttributeInjection .../>
  </TemplateAttributeInjections>
  <TemplatePlaceholderInjections>
    <TemplatePlaceholderInjection ... />
  </TemplatePlaceholderInjections>
</Template>
```

### Parameters

| Parameter                        | Description | Default | Remark |
|:---                              |:--- |:--- |:--- |
| rootSectionName[^1]              | The name of the root section. | | |

### Child sections

| Section                          | Description |
|:---                              |:--- |
| FileFormat[^2]                   | See [FileFormat](#fileformat) |
| Output[^2]                       | See [Output](#output) |
| Section                          | See [Section](#section) |
| TemplateAttributeInjection       | See [TemplateAttributeInjection](#templateattributeinjection) |
| TemplatePlaceholderInjection     | See [TemplatePlaceholderInjection](#templateplaceholderinjection) |


## FileFormat

### Syntax
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

### Parameters

| Parameter                    | Description | Default | Remark |
|:---                          |:--- |:--- |:--- |
| type                         | The file format type, see [File format types](#file-format-types). | | |
| version                      | The version of the file format  type. | | |
| templateType[^1]             | The template type, this can be either text or xml. | | | 
| currentAccessor              | The placeholder part for accessing a current attribute. | _ | | 
| childAccessor                | The placeholder part for accessing a child element. | | | 
| singleLineCommentPrefix[^3]  | The single line comment prefix. | | | 
| multiLineCommentPrefix       | The multi-line comment prefix. | | | 
| multiLineCommentSuffix       | The multi-line comment suffix. | | | 
| commentNodeXPath[^4]         | The template comment node XPath, this will be executed on the template and the resulting elements or attributes are inspected for annotations.| | This is only relevant for xml templates. | 
| annotationPrefix             | The prefix for a annotation. | @XGen | | 
| annotationArgsPrefix         | The prefix for the annotation arguments. | ( | | 
| annotationArgsSuffix         | The suffix for the annotation arguments. | ) | | 

### File format types
The supported file format types are:

- ANSI_SQL
- IBM_DataStage
- Informatica_PowerCenter
- Microsoft_SQL
- Microsoft_SSIS

## Output

### Syntax
``` xml
<Output
  type="<type>"
/>
```

### Parameters

| Parameter                            | Description | Default | Remark |
|:---                                  |:--- |:--- |:--- |
| type[^1]                             | The output type, see [Output types](#output-types) | | |

### Output types
The supported output types are described in the following table.

| Type               | Description |
|:---                |:----        |
| single_output      | Will results in 1 output for each element mapped to the root section of the template. |
| output_per_element | Will results in 1 output in total, combining all translations from elements into the template into 1 output. |

## Section

The Section configuration defines a section in a template. This can either be defined in the template part of the config or in the template directly. The syntax here is provided for in the configuration, but  the same parameters are also available when specifying the section inline in a template using the [Section](../Template/Annotation/Section) annotation.

### Syntax
``` xml
<Section
  name='<name-of-section>'
  [
    begin='<begin-characters>'
    includeBegin='<include-begin>'
    end='<end-characters>'
    includeEnd='<include-begin>'
    placeholderOnFirstLine='<placeholder-on-first-line>'
    placeholderOnLastLine='<placeholder-on-last-line'
    nrOfLines='<nr-of-lines>'
  ]
  [
    prefix='<prefix>'
    prefixStyle='<prefix-style>'
    prefixAction='<prefix-action>'
  ]
  [
    suffix='<suffix>'
    suffixStyle='<suffix-style>'
    prefixAction='<prefix-action>'
  ]
/>
```
### Parameters

| Parameter                  | Description | Default | Remark |
|:---                        |:--- |:--- |:--- |
| name[^1]                      | The name of the section. Must be unique within one template. | | This can only contain a-z, A-Z, 0-9, _ and - characters. |
| begin[^5]                  | Character sequence which defines the beginning of the section. | | If begin is not specified, it will automatically start on the first line after the annotation. This can thus only be done with an inline annotation. |
| includeBegin               | Whether to include the characters specified in `begin` in the output | true |
| end[^5]                    | Character sequence which defines the beginning of the section. | | Cannot be used in conjunction with `nrOfLines`. |
| includeEnd                 | Whether to include the characters specified in `end` in the output | true |
| placeholderOnFirstLine[^5] | Placeholder which exists on the first line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `begin`. |
| placeholderOnLastLine[^5]  | Placeholder which exists on the last line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `end` or `nrOfLines`. |
| nrOfLines[^5]              | Defined the number of lines of the section after the `@XGenSection` annotation. | 1 | Cannot be used in conjunction with `end` or `placeholderOnLastLine`. |
| prefix                     | The prefix to prepend using the `prefixStyle`. |
| prefixStyle                | The style of the prefix, see [Prefix & suffix styles](#prefix-suffix-styles) | allButFirst | Only interpreted when `prefix` is given. |
| prefixAction               | The action to be performed with the prefix, either add or remove. | add | Only interpreted when `prefix` is given. _Only add is supported at the moment._ |
| suffix                     | The suffix to append using the `suffxStyle`. |
| suffixStyle                | The style of the suffix, see [Prefix & suffix styles](#prefix-suffix-styles) | allButLast | Only interpreted when `suffix` is given. |
| suffixAction               | The action to be performed with the suffix, either add or remove. | add | Only interpreted when `suffix` is given. _Only add is supported at the moment._ |

### Prefix & suffix styles

| Style              | Description |
|:---                |:---         |
| firstOnly          | Only apply the suffix or prefix on the first repetition of the section. |
| lastOnly           | Only apply the suffix or prefix on the last repetition of the section. |
| allButFirst        | Apply the suffix or prefix on all repetition of the section, except the first. |
| allButLast         | Apply the suffix or prefix on all repetition of the section, except the last. |
| allButFirstAndLast | Apply the suffix or prefix on all repetition of the section, except the first and last. |

## TemplateAttributeInjection

### Syntax
``` xml
<TemplateAttributeInjection
  parentNodeXPath="<parent-node-xpath>"
  attributeName="<attribute-name>"
  defaultValue="<default-value>"
/>
```

### Parameters

| Parameter                   | Description | Default | Remark |
|:---                         |:--- |:--- |:--- |
| parentNodeXPath[^1]         | The XPath for parent node where attribute needs to be injected. | | |
| attributeName[^1]           | The name of the attribute to inject. | | |
| defaultValue                | Default value for the newly injected attribute. | | |

## TemplatePlaceholderInjection

### Syntax
``` xml
<TemplatePlaceholderInjection
  templateXPath="<template-xpath>"
  modelNode="<model-node>"
  scope="<scope>"
/>
```

### Parameters

| Parameter              | Description | Default | Remark |
|:---                    |:--- |:--- |:--- |
| templateXPath[^1]      | The XPath to evaluate on the template document to find the node on which to inject the placeholder. | | |
| modelNode[^1]          | What node in the model needs to be selected in the placeholder (it will be the right side of the placeholder). | | |
| scope                  | The template placeholder scope. This is either current or child. | current | |

[comment]: Footnotes
[^1]: required parameter
[^2]: required child section
[^3]: when templateType is 'text' this parameter is  required
[^4]: when templateType is 'xml' this parameter is  required
[^5]: At least one of these parameters must be specified when using this annotation in the template section of the config file. When using this annotation inline in a template and none of these parameters are specified, the default value for nrOfLines will be used and the section will start at the first line after the annotation.