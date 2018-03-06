# Template configuration

## Syntax

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

## Parameters

| Parameter                        | Description | Default | Remark |
|:---                              |:--- |:--- |:--- |
| rootSectionName[^1]              | The name of the root section. | | |

## Child sections

| Section                          | Description |
|:---                              |:--- |
| FileFormat[^2]                   | See [FileFormat](/CrossGenerate/Components/Config/Template/FileFormat) |
| Output[^2]                       | See [Output](/CrossGenerate/Components/Config/Template/Output) |
| Section                          | See [Section](/CrossGenerate/Components/Config/Template/Section) |
| TemplateAttributeInjection       | See [TemplateAttributeInjection](/CrossGenerate/Components/Config/Template/TemplateAttributeInjection) |
| TemplatePlaceholderInjection     | See [TemplatePlaceholderInjection](/CrossGenerate/Components/Config/Template/TemplatePlaceholderInjection) |

[comment]: Footnotes
[^1]: required parameter
[^2]: required child section