# XML template

A XML template is a template which contains a clear hierarchy and every node in the hierarchy can be identifier using a path (in XML this can be done with XPath).

Examples of XML templates are:

- Microsoft SQL Server Intergration Services packages (DTSX)
- Informatica PowerCenter Mapping (XML Export)
- IBM DataStage Job (XML Export)
- XHTML

## Annotation
CrossGenerate supports creating annotations in a XML template.
For documentation on the specific annotation syntax, see [Annotation](../Annotation).

### Section example
An example of a section configuration annotation inside a XML template would be:

``` xml
<SomeElement>
  <SomeSubElement description="@XGenSection(name='SomeExampleSection')" />
</SomeElement>
```

### Comment example
An example of a comment annotation inside a XML template would be:

``` xml
<SomeElement>
  <SomeSubElement description="@XGenComment(Some comment text...)" />
</SomeElement>
```