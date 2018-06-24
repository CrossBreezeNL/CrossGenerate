# Application Config

The application configuration for CrossGenerate is to configure the folders to use and specify the license information.

## Syntax

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenAppConfig>
  <App>
    ...
  </App>
  <License>
    ...
  </License>
</XGenAppConfig>
```

## Child sections
| Section                             | Description |
|:---                                 |:--- |
| App[^1]                             | See [App](#app) |
| License[^1]                         | See [License](#license) |


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


## License
In the license section the license information is specified. Most fields can be filled using the license information given by CrossBreeze.

### Syntax
``` xml
<License>
  <LicenseKey>...</LicenseKey>
  <ContractId>...</ContractId>
  <Tag>...</Tag>
  <Url>...</Url>
  <Version>...</Version>
</License>
```

### Parameters
| Parameter                           | Description | Default | Remark |
|:---                                 |:--- |:--- |:--- |
| LicenseKey[^2]                      | The license key. | | |
| ContractId[^2]                      | The contract key. | | |
| Tag                                 | An optional tag that can be supplied to track generation cycles. | | |
| Url[^2]                             | The url used to validate license info. | | |
| Version[^2] | The version of the software that is loaded. | | |


[comment]: Footnotes
[^1]: required child section
[^2]: required parameter