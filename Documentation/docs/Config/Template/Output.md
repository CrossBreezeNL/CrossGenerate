# Output

## Syntax
``` xml
<Output
  type="<type>"
/>
```

## Parameters

| Parameter                            | Description | Default | Remark |
|:---                                  |:--- |:--- |:--- |
| type[^1]                             | The output type, see [Output types](#output-types) | | |

## Output types
The supported output types are described in the following table.

| Type               | Description |
|:---                |:----        |
| single_output      | Will results in 1 output for each element mapped to the root section of the template. |
| output_per_element | Will results in 1 output in total, combining all translations from elements into the template into 1 output. |

[comment]: Footnotes
[^1]: required parameter