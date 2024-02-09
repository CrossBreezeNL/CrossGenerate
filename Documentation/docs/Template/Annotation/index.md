# Annotation

CrossGenerate supports creating annotations in a text and XML template.

## Annotations
The following annotations are supported inside a template:

| Annotation           | Description | Documentation |
|:---                  |:---         |:---           |
| [TextSection](./TextSection) | Define a section inside a text template | [Click here](./TextSection) |
| [XmlSection](./XmlSection) | Define a section inside a XML template | [Click here](./XmlSection) |
| [Comment](./Comment) | Create a comment in a template which isn't in the output. | [Click here](./Comment) |

## Syntax
An annotation is always prefixed with prefix defined per FileFormat of a template. For example when the template is a SQL statement the annotations are prefixed with `-- @XGen`. See [FileFormat](../../../../Config/Template/GenericTemplate#fileformat) for configuration of specific annotation syntax in a template.