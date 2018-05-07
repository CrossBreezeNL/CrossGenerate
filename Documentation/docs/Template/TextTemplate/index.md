# Text template

A text template is a template which is made up of sections (sometimes called blocks).

Examples of template types which would be classified as a section based template:

- SQL
- C#
- Java
- CSS

## Annotation
CrossGenerate supports creating annotations in a text template.
For documentation on the specific annotation syntax, see [Annotation](../Annotation).

### Section example
An example of a section configuration annotation inside a SQL template would be:

``` sql
USE [SomeDatabase];
GO
-- @XGenTextSection(name="SomeExampleSection", nrOfLines=2)
SELECT *
FROM [SomeSchema].[SomeTable]
```

### Comment example
An example of a comment annotation inside a SQL template would be:

``` sql
-- @XGenComment(This is an example of writing comments in a template.)
CREATE VIEW [ExampleView]
AS
SELECT * FROM [ExampleTable];
```