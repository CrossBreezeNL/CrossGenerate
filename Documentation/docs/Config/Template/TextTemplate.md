# TextTemplate

## TextTemplate

### Syntax
```xml
<TextTemplate rootSectionName="...">
  <FileFormat ... />
  <Output ... />
  <TextSections>
    <TextSection ... />
  </TextSections>
</TextTemplate>
```

### Parameters
| Parameter                        | Description | Default | Remark |
|:---                              |:--- |:--- |:--- |
| rootSectionName[^1]              | The name of the root section. | \_template\_ | |

### Child sections
| Section                          | Description |
|:---                              |:--- |
| FileFormat[^2]                   | See [FileFormat](#fileformat) |
| Output[^2]                       | See [Output](../GenericTemplate/#output) |
| TextSection                      | See [TextSection](#textsection) |

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
| ...                          | Click [here](../GenericTemplate/#fileformat) to see the generic FileFormat options. |||
| singleLineCommentPrefix[^1]  | The single line comment prefix. | | | 
| multiLineCommentPrefix       | The multi-line comment prefix. | | | 
| multiLineCommentSuffix       | The multi-line comment suffix. | | | 

## TextSection

The TextSection configuration defines a section in a text template. This can either be defined in the template part of the config or in the template directly. The syntax here is provided for in the configuration, but the same parameters are also available when specifying the section inline in a template using the [TextSection](../../../Template/Annotation/TextSection) annotation.

### Syntax
``` xml
<TextSection
  name="..."
  [
    optional="..."
  ]
  [
    placeholderName="..."
  ]
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
  [
    lineSeparator="..."
  ]
/>
```
### Parameters

| Parameter                  | Description | Default | Remark |
|:---                        |:--- |:--- |:--- |
| name[^1]                   | The name of the section. | | This can only contain a-z, A-Z, 0-9, _ and - characters. |
| optional[^4]               | Indicates if the section is optional (true/false). | false | Can be used to suppress warnings in case of a reused config, where sections defined in the config do not occur in each template the config is used with. |
| placeholderName            | An optional override for the placeholderName coming from the SectionModelBinding. | | Can be used to provide a specific placeholder name to use within the section. |
| begin[^3]                  | Character sequence which defines the beginning of the section. | | If begin is not specified, it will automatically start on the first line after the annotation. This can thus only be done with an inline annotation. |
| includeBegin               | Whether to include the characters specified in `begin` in the output | true |
| end[^3]                    | Character sequence which defines the end of the section. | | Cannot be used in conjunction with `nrOfLines`. |
| includeEnd                 | Whether to include the characters specified in `end` in the output | true |
| literalOnFirstLine[^3]     | Literal which exists on the first line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `begin`. |
| literalOnLastLine[^3]      | Literal which exists on the last line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `end` or `nrOfLines`. |
| nrOfLines[^3]              | Defines the number of lines of the section starting after the line containing the `@XGenSection` annotation. Any lines containing another (section) annotation should also be included. | 1 | Cannot be used in conjunction with `end` or `literalOnLastLine`. |
| prefix                     | The prefix to prepend using the `prefixStyle`. |
| prefixStyle                | The style of the prefix, see [Prefix & suffix styles](#prefix-suffix-styles) | allButFirst | Only interpreted when `prefix` is given. |
| prefixAction               | The action to be performed with the prefix, either add or remove. | add | Only interpreted when `prefix` is given. _Only add is supported at the moment._ |
| suffix                     | The suffix to append using the `suffxStyle`. |
| suffixStyle                | The style of the suffix, see [Prefix & suffix styles](#prefix-suffix-styles) | allButLast | Only interpreted when `suffix` is given. |
| suffixAction               | The action to be performed with the suffix, either add or remove. | add | Only interpreted when `suffix` is given. _Only add is supported at the moment._ |
| lineSeparator              | The string literal which represents a line separator within this section. | \r?\n | By default this is the line separator as specified in the [FileFormatConfig](../GenericTemplate/#fileformat) which is applicable for the template part this annotation is in. For a text template it will be the main TextTemplate configuration element. For a XML template, it will be the respective TextTemplate element in the TextTemplates collection. |

### Prefix & suffix styles

| Style              | Description |
|:---                |:---         |
| firstOnly          | Only apply the suffix or prefix on the first repetition of the section. |
| lastOnly           | Only apply the suffix or prefix on the last repetition of the section. |
| allButFirst        | Apply the suffix or prefix on all repetition of the section, except the first. |
| allButLast         | Apply the suffix or prefix on all repetition of the section, except the last. |
| allButFirstAndLast | Apply the suffix or prefix on all repetition of the section, except the first and last. |
| all                | Apply the suffix or prefix on all repetition of the section. |


[comment]: Footnotes
[^1]: required parameter
[^2]: required child section
[^3]: At least one of these parameters must be specified when using this annotation in the template section of the config file. When using this annotation inline in a template and none of these parameters are specified, the default value for nrOfLines will be used and the section will start at the first line after the annotation.
[^4]: An option which can only be specified in the config.
