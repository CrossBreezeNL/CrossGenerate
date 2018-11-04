# Binding configuration

## Binding

### Syntax
``` xml
<Binding>
  <SectionModelBinding ... >
    ...
  </SectionModelBinding>
</Binding>
```

### Child sections
| Section                                      | Description |
|:---                                              |:--- |
| SectionModelBinding               | See [SectionModelBinding](#sectionmodelbinding) |


## SectionModelBinding
A SectionModelBinding defines how the model should be mapped to the template.
### Syntax
``` xml
<SectionModelBinding
  section="..."
  modelXPath="..."
  placeholderName="..."
  variableName="..."
>
  <Placeholders>
    <Placeholder ... />
  </Placeholders>
  <Literals>
    <Literal ... />
  </Literals>
  <SectionModelBinding ... >
    ...
  </SectionModelBinding>
</SectionModelBinding>
```
### Parameters
| Parameter                          | Description | Default | Remark |
|:---                                |:--- |:--- |:--- |
| section[^1]                        | The name of the section as defined in the template or in the config. For each section name there can only be one binding specified. | | |
| modelXPath[^1]                     | The XPath expression which can be applied on the model to get to the XML element to bind to the section. | | See [XPath](./XPath). |
| placeholderName                    | The name of the placeholder of the current element. | The same as `section` | |
| variableName                       | The variable name which is bound to the current element within a section. | | This variable can be used in any XPath expression in a child element of this SectionModelBinding. When using the varibale in a XPath it must be prefixed with a $, for example the variable with name 'someVariable' can be accessed in a XPath expression using $someVariable. |

### Child sections
| Section                            | Description |
|:---                                |:--- |
| Placeholder                        | See [Placeholder](#placeholder) |
| Literal                            | See [Literal](#literal) |
| SectionModelBinding                | A SectionModelBinding configuration in itself can contain a SectionModelBinding configuration. So it can be defined recursively. |

## Placeholder

### Syntax
``` xml
<Placeholder
  name="..."
  modelXPath="..."
  variableName="..."
/>
```

### Parameters

| Parameter                          | Description | Default | Remark |
|:---                                |:--- |:--- |:--- |
| name[^1]                           | The name of the placeholder. | | |
| modelXPath[^1]                     | The XPath expression which can be applied on the current element to get the element for the placeholder. | | See [XPath](./XPath). |
| variableName                       | The variable name which is bound to the node the modelXPath points to. | | This variable can be used in any XPath expression in a following element of the SectionModelBinding the placeholder is defined in. When using the varibale in a XPath it must be prefixed with a $, for example the variable with name 'someVariable' can be accessed in a XPath expression using $someVariable. |

## Literal
A literal configuration within a SectionModelBinding can be used to replace a literal piece of text in the template with a value from the model using a XPath expression.

### Syntax
``` xml
<Literal
  literal="..."
  modelXPath="..."
/>
```

### Parameters

| Parameter                          | Description | Default | Remark |
|:---                                |:--- |:--- |:--- |
| literal[^1]                        | The literal to find in the template. | | |
| modelXPath[^1]                     | The XPath expression which can be applied on the current element to get the value for the literal. | | See [XPath](./XPath). |


[comment]: Footnotes
[^1]: required parameter