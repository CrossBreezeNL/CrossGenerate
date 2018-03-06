# Section

The Section configuration defines a section in a template. This can either be defined in the template part of the config or in the template directly. The syntax here is provided for in the configuration, but  the same parameters are also available when specifying the section inline in a template using the [Section](/CrossGenerate/Components/Template/Annotations/Section) annotation.

## Syntax
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
## Parameters

| Parameter                  | Description | Default | Remark |
|:---                        |:--- |:--- |:--- |
| name[^1]                      | The name of the section. Must be unique within one template. | | This can only contain a-z, A-Z, 0-9, _ and - characters. |
| begin[^2]                  | Character sequence which defines the beginning of the section. | | If begin is not specified, it will automatically start on the first line after the annotation. This can thus only be done with an inline annotation. |
| includeBegin               | Whether to include the characters specified in `begin` in the output | true |
| end[^2]                    | Character sequence which defines the beginning of the section. | | Cannot be used in conjunction with `nrOfLines`. |
| includeEnd                 | Whether to include the characters specified in `end` in the output | true |
| placeholderOnFirstLine[^2] | Placeholder which exists on the first line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `begin`. |
| placeholderOnLastLine[^2]  | Placeholder which exists on the last line of the section, the whole line will be taken into the section. | | Cannot be used in conjunction with `end` or `nrOfLines`. |
| nrOfLines[^2]              | Defined the number of lines of the section after the `@XGenSection` annotation. | 1 | Cannot be used in conjunction with `end` or `placeholderOnLastLine`. |
| prefix                     | The prefix to prepend using the `prefixStyle`. |
| prefixStyle                | The style of the prefix, see [Prefix & suffix styles](#prefix-suffix-styles) | allButFirst | Only interpreted when `prefix` is given. |
| prefixAction               | The action to be performed with the prefix, either add or remove. | add | Only interpreted when `prefix` is given. _Only add is supported at the moment._ |
| suffix                     | The suffix to append using the `suffxStyle`. |
| suffixStyle                | The style of the suffix, see [Prefix & suffix styles](#prefix-suffix-styles) | allButLast | Only interpreted when `suffix` is given. |
| suffixAction               | The action to be performed with the suffix, either add or remove. | add | Only interpreted when `suffix` is given. _Only add is supported at the moment._ |

## Prefix & suffix styles

| Style              | Description |
|:---                |:---         |
| firstOnly          | Only apply the suffix or prefix on the first repetition of the section. |
| lastOnly           | Only apply the suffix or prefix on the last repetition of the section. |
| allButFirst        | Apply the suffix or prefix on all repetition of the section, except the first. |
| allButLast         | Apply the suffix or prefix on all repetition of the section, except the last. |
| allButFirstAndLast | Apply the suffix or prefix on all repetition of the section, except the first and last. |


## Examples

### Section defined in config

#### Example in config to take in 1 line as a section.
Here the starting point of the section is defined by the `placeholderOnFirstLine` parameter and the `nrOfLines` gets the default value `1`. 
#####Template:
``` java
CREATE TABLE [ExampleTable] AS (
  [Attribute_Name] int not null,
  [SomeOtherColumn] int null
)
```
#####Config (in the Template part of the config):
``` xml
<Section name='ExampleColumnSection' placeholderOnFirstLine='Attribute_Name' />
```
#####ExampleColumnSection Section:
``` java
  [Attribute_Name] int not null,
```

### Section defined template

#### Example in SQL to take in 1 line as a section.
Here the starting point of the section by default is the first line after the annotation and the `nrOfLines` gets the default value `1`. 
#####Template:
``` java
CREATE TABLE [ExampleTable] AS (
  -- @XGenSection(name="ExampleColumnSection")
  [Attribute_Name] int not null,
  [SomeOtherColumn] int null
)
```
#####ExampleColumnSection Section:
``` java
  [Attribute_Name] int not null,
```

#### Example in SQL to take in 2 lines as a section.
Here the starting point of the section by default is the first line after the annotation. 
#####Template:
``` java
CREATE TABLE [ExampleTable] AS (
  -- @XGenSection(name="ExampleColumnSection", nrOfLines=2)
  [Attribute_Name]
    int not null,
  [SomeOtherColumn] int null
)
```
#####ExampleColumnSection Section:
``` java
  [Attribute_Name]
    int not null,
```
#### Example in SQL to take in 1 line as a section and apply the suffix on all section repetions but the last.
Here the `suffixAction` gets the default value `add` and the `suffixStyle` gets the default value `allButLast`. 
``` java
CREATE TABLE [ExampleTable] AS (
  -- @XGenSection(name="ExampleColumnSection", suffix=",")
  [Attribute_Name] int not null
)
```

[comment]: Footnotes
[^1]: Parameter is required
[^2]: At least one of these parameters must be specified when using this annotation in the template section of the config file. When using this annotation inline in a template and none of these parameters are specified, the default value for nrOfLines will be used and the section will start at the first line after the annotation.