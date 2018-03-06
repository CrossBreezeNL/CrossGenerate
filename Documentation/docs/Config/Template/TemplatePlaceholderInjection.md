# TemplatePlaceholderInjection

## Syntax
``` xml
<TemplatePlaceholderInjection
  templateXPath="<template-xpath>"
  modelNode="<model-node>"
  scope="<scope>"
/>
```

## Parameters

| Parameter              | Description | Default | Remark |
|:---                    |:--- |:--- |:--- |
| templateXPath[^1]      | The XPath to evaluate on the template document to find the node on which to inject the placeholder. | | |
| modelNode[^1]          | What node in the model needs to be selected in the placeholder (it will be the right side of the placeholder). | | |
| scope                  | The template placeholder scope. This is either current or child. | current | |

[comment]: Footnotes
[^1]: Parameter is required