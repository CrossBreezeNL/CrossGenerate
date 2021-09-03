# Hello world

This page contains a simple hello world example to get familiar with using CrossGenerate.
The example illustrates how CrossGenerate combines information from a model and a template to generate output.
The desired output is a textfile that contains the text 'hello world'.

The files shown in this example can be [downloaded here](./001_HelloWorld.zip). You need to [download](https://generate.x-breeze.com/download/){target=_blank} CrossGenerate to run the example on your computer.

To use CrossGenerate we use the following set of files:

- Model: An XML file that contains the model information
- Template: A template file, in this example a simple text file
- Config: An XML file that contains a generation configuration for CrossGenerate
- Application config: An XML file that contains the application configuration for CrossGenerate
- Run file: A cmd file that is used to run CrossGenerate with the appropriate parameters

## Model
For this example the model file is the XML file shown below

```xml
<?xml version="1.0" encoding="UTF-8"?>
<example message="hello world" />
```

## Template
The template is a simple textfile that contains the following text

```
ExamplePlaceholder_message
```

## Config
The configuration file contains the information that CrossGenerate needs to combine the model and the template to the desired output.
In the file a for the text template the root section is named `ExampleSection`. This means `ExampleSection` refers to the entire template.
The `<SectionModelBinding>` entry in the config file bindes the section, being the entire template, to the `example` element of the model. `ExamplePlaceholder` is set as placeholder for this section model binding.
With this configuration, CrossGenerate will replace `ExamplePlaceholder_message` in the template with the contents of the `message` attribute in the model, being hello world.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>

  <!-- Generate using a text template, where the root section is called 'ExampleSection'.  -->
  <TextTemplate rootSectionName="ExampleSection">
    <!-- Specify the placeholder part for accessing an attribute of the mapped model element. -->
    <FileFormat currentAccessor="_" />
    <!-- We want all output to be written to a single file. -->
    <Output type="single_output" />
  </TextTemplate>

  <Binding>
    <!-- Map the section 'ExampleSection' of the template on the 'example' element of the model and scan the section in the template for the 'ExamplePlacerholder'. -->
		<SectionModelBinding section="ExampleSection" modelXPath="example" placeholderName= "ExamplePlaceholder" />
  </Binding>
  
</XGenConfig>
```
## Application config
The application config contains the file locations that CrossGenerate uses for the input (model, config and template) and output files

```xml
<?xml version="1.0" encoding="utf-8"?>
<XGenAppConfig>
  <App>
    <ConfigFolder>..\Config</ConfigFolder>
    <ModelFolder>..\Model</ModelFolder>
    <OutputFolder>..\Output</OutputFolder>
    <TemplateFolder>..\Templates</TemplateFolder>
  </App>
</XGenAppConfig>
```

## Run file
A command file is used to invoke CrossGenerate with the appropriate parameters.
In the command file variable `xgenerate` is used to refer to the location where the CrossGenerate jar file is located.
The invocation uses the following parameters:

- config: The application configuration file that is used
- mtc: Combination of model, template and config file that are to be used
- cll: Console log level

See the [Application section](/Application/CommandLine) for an explanation of all commandline arguments.

```shell
@echo off
REM Configure the CrossGenerate java execute command.
set xgenerate=java -jar ..\..\jar\XGenerate.jar

REM If the output folder doesn't exist, create it.
set OutputFolder=..\Output
if not exist "%OutputFolder%" mkdir "%OutputFolder%"

REM Start generate using the following options:
REM  -config > Point to where the CrossGenerate application configuration be found.
REM  -mtc {model-file}::{template-file}::{config-file} > Specify the model, template and config file to use for generating.
REM  -cll INFO > Set the console log level to INFO so we get information about the generation on the console.
%xgenerate% ^
 -config .\XGenAppCfg.xml ^
 -mtc model.xml::example.txt::config.xml ^
 -cll INFO

REM Pause the command, so one can read the output before the terminal is closed.
pause
```

## Output
When running CrossGenerate for this example, the output folder contains a file called example.txt which contains the output illustrated below
```
hello world
```

## Summary
This example shows how CrossGenerate can be used to generate output using a model and a template. Various aspects are illustrated in the other examples in this section, such as working with models that have more data, filtering data before adding it to a template, working with XML templates and apply pre-processing on model or template.

## Download
The files shown in this example can be downloaded [here](./001_HelloWorld.zip).

