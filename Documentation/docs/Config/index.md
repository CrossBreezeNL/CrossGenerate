# Configuration

The configuration for CrossGenerate is designed to be the glue between model and template. It contains information about the template, the model and how they are to be combined in generation (binding).

## Syntax

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>
  <Model>
    ...
  </Model>
  <!--
    The following section is the template,
    this can either be XmlTemplate or TextTemplate.
  -->
  <...Template>
    ...
  </...Template>
  <Binding>
    ...
  </Binding>
</XGenConfig>
```

## Child sections
| Section                             | Description |
|:---                                 |:--- |
| Model                               | See [Model](./Model) |
| XmlTemplate or TextTemplate[^1]     | See [XmlTemplate](./Template/XmlTemplate) or [TextTemplate](./Template/TextTemplate) |
| Binding[^1]                         | See [Binding](./Binding) |


## Config re-use
CrossGenerate supports splitting a config into multiple XML files to enable re-use. For this the XML feature XML Inclusions is used.
In order to use XML inclusion, the appropriate XML namespace, xmlns:xi="http://www.w3.org/2001/XInclude", needs to be declared in each XML file that includes another XML file. XML files that are included in a config file can also include other XML files, which enables nested includes of configuration elements. The path to the XML file being included can be absolute or relative to config file where the include is defined. To re-use only a portion of a config file, the include directive can be provided with an `xpointer` attribute containing the XPath expression for selecting the portion of the file to be included.

### Example config with re-use
As can be seen in this example, parts of the config can be stored in a separate file. In this case, the bindings declaration could be used in multiple config files.

#### Main config file
``` xml
<?xml version="1.0" encoding="UTF-8"?>            
<XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
  <TextTemplate rootSectionName="Template">
    <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="single_output" />
  </TextTemplate>              
  <Binding>        
    <xi:include href="bindings.xml"/>
  </Binding>          
</XGenConfig>

```

#### bindings.xml
``` xml
<?xml version="1.0" encoding="UTF-8"?>     
<SectionModelBinding section="Template" modelXPath ="/system" placeholderName="system">
  <SectionModelBinding section="Tables" modelXPath="./entities/entity" placeholderName="table">
    <Placeholders>
      <Placeholder name="system" modelXPath="../.." />
    </Placeholders>
  </SectionModelBinding>
</SectionModelBinding>
```

### Example re-use with xpointer
The example below illustrates the use of xpointer for referring to a certain selection within a referenced file. The `xpointer` directive in the config file below results in only the SectionModelBinding with `section="Tables"` to be included from bindings.xml

``` xml
<?xml version="1.0" encoding="UTF-8"?>            
<XGenConfig xmlns:xi="http://www.w3.org/2001/XInclude">
  <TextTemplate rootSectionName="Template">
    <FileFormat singleLineCommentPrefix="--" annotationPrefix="@XGen" annotationArgsPrefix="(" annotationArgsSuffix=")" />
    <Output type="single_output" />
  </TextTemplate>              
  <Binding>        
    <SectionModelBinding section="Template" modelXPath ="/system" placeholderName="system">
      <xi:include href="bindings.xml" xpointer="//SectionModelBinding[@section='Tables']"/>
    </SectionModelBinding>
  </Binding>          
</XGenConfig>

```

#### bindings.xml
``` xml
<?xml version="1.0" encoding="UTF-8"?>     
<SectionModelBinding section="Template" modelXPath ="/system" placeholderName="system">
  <SectionModelBinding section="Tables" modelXPath="./entities/entity" placeholderName="table">
    <Placeholders>
      <Placeholder name="system" modelXPath="../.." />
    </Placeholders>
  </SectionModelBinding>
  <SectionModelBinding section="OtherObjects" modelXPath="./otherobjects/otherobject" placeholderName="object">
    <Placeholders>
      <Placeholder name="system" modelXPath="../.." />
    </Placeholders>
  </SectionModelBinding>
</SectionModelBinding>
```


[comment]: Footnotes
[^1]: required child section