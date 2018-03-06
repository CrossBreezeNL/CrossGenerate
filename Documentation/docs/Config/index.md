# Configuration

The configuration for CrossGenerate is designed to be the glue between model and template. It contains information about the template, the model and how they are to be combined in generation (binding).

## Syntax

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model>
    ...
  </Model>
  <Template ...>
    ...
  </Template>
  <Binding>
    ...
  </Binding>
</XGenConfig>
```

## Child sections
| Section                             | Description |
|:---                                 |:--- |
| Model                               | See [Model](./Model) |
| Template[^1]                        | See [Template](./Template) |
| Binding[^1]                         | See [Binding](./Binding) |


[comment]: Footnotes
[^1]: required child section