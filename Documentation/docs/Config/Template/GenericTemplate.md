# Generic template options
Here the generic template options are described.

## FileFormat

### Syntax
``` xml
<FileFormat
  type="..."
  version="..."
  currentAccessor="..."
  childAccessor="..."
  annotationPrefix="..."
  annotationArgsPrefix="..."
  annotationArgsSuffix="..."
  ...
/>
```

### Parameters
| Parameter                    | Description | Default | Remark |
|:---                          |:--- |:--- |:--- |
| type                         | The file format type, see [File format types](#file-format-types). | | |
| version                      | The version of the file format  type. | | |
| currentAccessor              | The placeholder part for accessing a current attribute. | _ | | 
| childAccessor                | The placeholder part for accessing a child element. | | | 
| annotationPrefix             | The prefix for a annotation. | @XGen | | 
| annotationArgsPrefix         | The prefix for the annotation arguments. | ( | | 
| annotationArgsSuffix         | The suffix for the annotation arguments. | ) | |
| ...                          | The template type (Xml or Text) defines the other options, see [XmlTemplate](XmlTemplate/#fileformat) or [TextTemplate](TextTemplate/#fileformat). |||

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
  type="..."
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


[comment]: Footnotes
[^1]: required parameter
