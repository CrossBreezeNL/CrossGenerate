# XmlTemplate

## XmlTemplate
### Syntax

```xml
<XmlTemplate rootSectionName="...">
  <FileFormat/>
  <Output ... />
  <XmlSections>
    <XmlSection ... />
  </XmlSections>
  <TemplateAttributeInjections>
    <TemplateAttributeInjection ... />
  </TemplateAttributeInjections>
  <TemplatePlaceholderInjections>
    <TemplatePlaceholderInjection ... />
  </TemplatePlaceholderInjections>
  <TextTemplates>
    <TextTemplate ... />
  </TextTemplates>
</XmlTemplate>
```

### Parameters
| Parameter                        | Description | Default | Remark |
|:---                              |:--- |:--- |:--- |
| rootSectionName[^1]              | The name of the root section. | \_template\_ | |

### Child sections
| Section                          | Description |
|:---                              |:--- |
| FileFormat[^2]                   | See [FileFormat](#fileformat) |
| Output[^2]                       | See [Output](GenericTemplate/#output) |
| XmlSection                       | See [XmlSection](#xmlsection) |
| TemplateAttributeInjection       | See [TemplateAttributeInjection](#templateattributeinjection) |
| TemplatePlaceholderInjection     | See [TemplatePlaceholderInjection](#templateplaceholderinjection) |
| TextTemplate                     | See [TextTemplate](#texttemplate) |

## FileFormat

### Syntax
``` xml
<FileFormat
  ...
  commentNodeXPath="..."
/>
```

### Parameters
| Parameter                    | Description | Default | Remark |
|:---                          |:--- |:--- |:--- |
| ...                          | Click [here](GenericTemplate/#fileformat) to see the generic FileFormat options. |||
| commentNodeXPath[^1]         | The template comment node XPath, this will be executed on the template and the resulting elements or attributes are inspected for annotations.| | This is only relevant for xml templates. | 


## XmlSection
The XmlSection configuration defines a section in a XML template. This can either be defined in the template part of the config or in the template directly. The syntax here is provided for the configuration, but the same parameters are also available when specifying the section inline in a template using the [XmlSection](../Template/Annotation/XmlSection) annotation.

### Syntax
``` xml
<XmlSection
  name="..."
  templateXPath="..."
/>
```
### Parameters

| Parameter                  | Description | Default | Remark |
|:---                        |:--- |:--- |:--- |
| name[^1]                   | The name of the section. Must be unique within one template. | | This can only contain a-z, A-Z, 0-9, _ and - characters. |
| templateXPath[^1]          | The XPath which needs to be executed on the template document to get the element which resembles the section element. | | |


## TemplateAttributeInjection

### Syntax
``` xml
<TemplateAttributeInjection
  templateXPath="..."
  attributeName="..."
  attributeValue="..."
/>
```

### Parameters
| Parameter                   | Description | Default | Remark |
|:---                         |:--- |:--- |:--- |
| templateXPath[^1]           | The XPath for template node where attribute needs to be injected. | | |
| attributeName[^1]           | The name of the attribute to inject. | | |
| attributeValue              | Default value for the newly injected attribute. | | |


## TemplatePlaceholderInjection

### Syntax
``` xml
<TemplatePlaceholderInjection
  templateXPath="..."
  modelNode="..."
  scope="..."
/>
```

### Parameters
| Parameter              | Description | Default | Remark |
|:---                    |:--- |:--- |:--- |
| templateXPath[^1]      | The XPath to evaluate on the template document to find the node on which to inject the placeholder. | | |
| modelNode[^1]          | What node in the model needs to be selected in the placeholder (it will be the right side of the placeholder). | | |
| scope                  | The template placeholder scope. This is either current or child. | current | |


## TextTemplate
A XML template can contain elements or attribute which in itself contain a text template. For example when a ETL package (which is a XML template) contains a SQL SELECT statement to fetch data from a source system. To instruct CrossGenerate to interpret the element or attribute as a text template, the TextTemplate element can be specified in the configuration of a XML template.

### Syntax
```xml
<TextTemplate
  node="..."
  rootSectionName="..."
>
  <FileFormat ... />
  <TextSections>
    <TextSection ... />
  </TextSections>
</TextTemplate>
```

### Parameters
| Parameter                        | Description | Default | Remark |
|:---                              |:--- |:--- |:--- |
| node[^1]                         | The node which contains the text template | | |
| rootSectionName                  | The name of the root section. | | |

### Child sections
| Section                          | Description |
|:---                              |:--- |
| FileFormat[^2]                   | See [FileFormat](TextTemplate/#fileformat) |
| TextSection                      | See [TextSection](TextTemplate/#textsection) |

[comment]: Footnotes
[^1]: required parameter
[^2]: required child section