# Configuration

The configuration for CrossGenerate is designed to be the glue between model and template. It contains information about the template, the model and how they are to be combined in generation (binding).

## Syntax

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model>
    ...
  </Model>
  <!--
    The following section is the template,
    this can either be XmlTemplate or TextTemplate.
  -->
  <...Template>
    ...
  </...Template>
  <Binding>
    ...
  </Binding>
</XGenConfig>
```

## Child sections
| Section                             | Description |
|:---                                 |:--- |
| Model                               | See [Model](./Model) |
| XmlTemplate or TextTemplate[^1]     | See [XmlTemplate](./Template/XmlTemplate) or [TextTemplate](./Template/TextTemplate) |
| Binding[^1]                         | See [Binding](./Binding) |


[comment]: Footnotes
[^1]: required child section