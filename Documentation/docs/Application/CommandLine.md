# Command line

CrossGenerate can be started from the command line (invoked via the Java executable). Here the command line options are specified.

## Command
Here the command line options are specified.

``` xml
java -jar XGenerate.jar [GenerationOptions]
```

### GenerationOptions
All generation options are key-value based. So you can specify an option key with a dash before it followed by a space and its value.

In the following table all options are listed. All options have two keys which can be used, the fully written one and the abbreviated one. The two options are seperated by a comma in the 'Parameter' column.

| Parameter                        | Description | Default | Allowed values |
|:---                              |:--- |:--- |:--- |
| -c, -Config [^1]                 | The location of the application configuration file. See [Application config](./XGenAppConfig) | | |
| -mtc, -ModelTemplateConfig [^2]  | A model-template-config combination in the form: "_MODEL_FILE_"::"_TEMPLATE_FILE_"::"_GEN_CONFIG_FILE_". Absolute and relative paths are allowed here. | | |
| -d, -Debug                       | Whether to run the generator in debug mode. | false | true, false |
| -cll, -ConsoleLogLevel           | The log level for the console. | SEVERE | See [Log levels](#log-levels). |
| -fll, -FileLogLevel              | The log level for the log file. | INFO | See [Log levels](#log-levels). |
| -fld, -FileLogDestination        | The destionation for the log file. If not specified, no log file will be written. | | |

### Log levels
The following log levels are supported.

| Level           | Description |
|:---             |:--- |
| INFO            | Errors, warnings and informational messages. |
| WARNING         | Errors and warnings. |
| SEVERE          | Only errors. |


[comment]: Footnotes
[^1]: required options
[^2]: at least one must be given
