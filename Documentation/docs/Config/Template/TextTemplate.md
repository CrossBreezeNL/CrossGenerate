# TextTemplate

## TextTemplate

### Syntax
```xml
<TextTemplate rootSectionName="...">
  <FileFormat ... />
  <Output ... />
  <Sections>
    <Section ... />
  </Sections>
</TextTemplate>
```

### Parameters
| Parameter                        | Description | Default | Remark |
|:---                              |:--- |:--- |:--- |
| rootSectionName[^1]              | The name of the root section. | \_root\_ | |

### Child sections
| Section                          | Description |
|:---                              |:--- |
| FileFormat[^2]                   | See [FileFormat](#fileformat) |
| Output[^2]                       | See [Output](GenericTemplate/#output) |
| Section                          | See [Section](#section) |

## FileFormat

### Syntax
``` xml
<FileFormat
  ...
  singleLineCommentPrefix="..."
  multiLineCommentPrefix="..."
  multiLineCommentSuffix="..."
/>
```

### Parameters

| Parameter                    | Description | Default | Remark |
|:---                          |:--- |:--- |:--- |
| ...                          | Click [here](GenericTemplate/#fileformat) to see the generic FileFormat options. |||
| singleLineCommentPrefix[^1]  | The single line comment prefix. | | | 
| multiLineCommentPrefix       | The multi-line comment prefix. | | | 
| multiLineCommentSuffix       | The multi-line comment suffix. | | | 

## Section

The Section configuration defines a section in a template. This can either be defined in the template part of the config or in the template directly. The syntax here is provided for in the configuration, but the same parameters are also available when specifying the section inline in a template using the [Section](../Template/Annotation/Section) annotation.

### Syntax
``` xml
<Section
  name="..."
  [
    begin="..."
    includeBegin="..."
    end="..."
    includeEnd="..."
    literalOnFirstLine="..."
    literalOnLastLine="..."
    nrOfLines="..."
  ]
  [
    prefix="..."
    prefixStyle="..."
    prefixAction="..."
  ]
  [
    suffix="..."
    suffixStyle="..."
    suffixAction="..."
  ]
/>
```
### Parameters

| Parameter                  | Description | Default | Remark |
|:---                        |:--- |:--- |:--- |
| name[^1]                   | The name of the section. Must be unique within one template. | | This can only contain a-z, A-Z, 0-9, _ and - characters. |
| begin[^3]                  | Character sequence which defines the beginning of the section. | | If begin is not specified, it will automatically start on the first line after the annotation. This can thus only be done with an inline annotation. |
| includeBegin               | Whether to include the characters specified in `begin` in the output | true |
| end[^3]                    | Character sequence which defines the beginning of the section. | | Cannot be used in conjunction with `nrOfLines`. |
| includeEnd                 | Whether to include the characters specified in `end` in the output | true |
| literalOnFirstLine[^3]     | Literal which exists on the first line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `begin`. |
| literalOnLastLine[^3]      | Literal which exists on the last line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `end` or `nrOfLines`. |
| nrOfLines[^3]              | Defined the number of lines of the section after the `@XGenSection` annotation. | 1 | Cannot be used in conjunction with `end` or `literalOnLastLine`. |
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


[comment]: Footnotes
[^1]: required parameter
[^2]: required child section
[^3]: At least one of these parameters must be specified when using this annotation in the template section of the config file. When using this annotation inline in a template and none of these parameters are specified, the default value for nrOfLines will be used and the section will start at the first line after the annotation.
