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
| Version[^2] | The [version](#crossgenerate-versions) of the software that is loaded. | | |

### CrossGenerate versions
CrossGenerate is released using a three level release numbering, x.y.z, where x indicates a major release, y a feature release and z a bugfix release. When specifying a version number in the license section of the configuration, you can:

- Specify a full release number, for example 2.4.0. In this case you ensure that you always generate using a specific (sub)release
- Specify major and feature release only, for example 2.4. In this case you will automatically use the most recent bugfix release within the specified feature release.
- Specify major release only, for example 2. In this case you will automatically use the most recent feature/bugfix release within the specified major release.


[comment]: Footnotes
[^1]: required child section
[^2]: required parameter