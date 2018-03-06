# Annotation

CrossGenerate supports creating annotations in a text template.

## Annotations
The following annotations are supported inside a template:

| Annotation           | Description | Documentation |
|:---                  |:---         |:---           |
| [Section](./Section) | Define a section inside a template | [Click here](./Section) |
| [Comment](./Comment) | Create a comment in a template which isn't in the output. | [Click here](./Comment) |

## Syntax
An annotation is always prefixed with prefix defined per FileFormat of a template. For example when the template is a SQL statement the annotations are prefixed with `-- @XGen`. See [FileFormat](../../../Config/Template/FileFormat) for configuration of specific annotation syntax in a template.