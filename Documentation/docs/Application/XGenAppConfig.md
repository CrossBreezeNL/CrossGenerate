# Application Config

The application configuration for CrossGenerate is to configure the folders to use.

## Syntax

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenAppConfig>
  <App>
    ...
  </App>
</XGenAppConfig>
```

## Child sections
| Section                             | Description |
|:---                                 |:--- |
| App[^1]                             | See [App](#app) |


## App
The App section contains all folders which will be used while generating. When relative paths are specified in the model-template-config combinations these paths are used as a base.

### Syntax
``` xml
<App>
  <TemplateFolder>...</TemplateFolder>
  <ModelFolder>...</ModelFolder>
  <OutputFolder>...</OutputFolder>
  <ConfigFolder>...</ConfigFolder>
</App>
```

### Parameters
| Parameter                           | Description | Default | Remark |
|:---                                 |:--- |:--- |:--- |
| TemplateFolder[^2]                  | The folder location for the template files. | | |
| ModelFolder[^2]                     | The folder location for the model files. | | |
| OutputFolder[^2]                    | The folder location for the output. | | |
| ConfigFolder[^2]                    | The folder location for the config files. | | |


[comment]: Footnotes
[^1]: required child section
[^2]: required parameter