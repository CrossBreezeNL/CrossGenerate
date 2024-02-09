# Model configuration

## Model

### Syntax
``` xml
<Model>
  <ModelNamespaces>
    <ModelNamespace ... />
  </ModelNamespaces>
  <ModelAttributeInjections>
    <ModelAttributeInjection ... />
  </ModelAttributeInjections>
  <ModelNodeRemovals>
    <ModelNodeRemoval ... />
  </ModelNodeRemovals>
</Model>
```

### Child sections
| Section                            | Description |
|:---                                |:--- |
| ModelNamespaces                    | See [ModelNamespaces](#modelnamespaces) |
| ModelAttributeInjection            | See [ModelAttributeInjection](#modelattributeinjection) |
| ModelNodeRemoval                   | See [ModelNodeRemoval](#modelnoderemoval) |


## ModelNamespaces
When a model XML file is used that contains namespaces, the namespaces needed for section model binding, model attribute injection and model node removal need to be specified in the model config.

### Syntax
``` xml
<ModelNamespace
  prefix="..."
  namespace="..."  
/>
```

### Parameters
| Parameter                              | Description | Default | Remark |
|:---                                    |:--- |:--- |:--- |
| prefix[^1]                             | The prefix used to address elements in the specific namespace | | |
| namespace[^1]                          | The namespace as used in the model XML | | |


## ModelAttributeInjection
Model attribute injection can be used to inject attributes to the model before generation starts. This is usefull for enriching the model with additional information that is needed during generation. An example of model attribute injection is adding ETL specific datatypes to an attribute or column definition,  derived from the database datatype that is already present in the model.

### Syntax
``` xml
<ModelAttributeInjection
  modelXPath="..."
  targetAttribute="..."
  targetValue="..."
  targetXPath="..."
>
  <ValueMappings .. />
</ModelAttributeInjection>
```

### Parameters
| Parameter                              | Description | Default | Remark |
|:---                                    |:--- |:--- |:--- |
| modelXPath[^1]                         | The XPath to apply on the model to get to the element on which to inject the attribute. | | See [XPath](../XPath). |
| targetAttribute[^1]                    | The name of the attribute to inject. | |If the target attribute is already present in the model it's value will be overwritten |
| targetValue[^2]                        | The value of the attribute to inject. | | | 
| targetXPath[^2]                        | The XPath to apply on the model element to get the target value. | | See [XPath](../XPath). | 

### Child sections
| Section                            | Description |
|:---                                |:--- |
| ValueMappings[^2]                  | See [ValueMappings](#valuemappings) |


## ValueMappings
A ValueMappings element can be used inside a ModelAttributeInjection element to specify the translation from a set of input values to output values. This can be usefull when a certain attribute in de model needs to be translated using a list of possible values, for example a data type translation.

### Syntax
``` xml
<ValueMappings inputNode="...">
  <ValueMapping ... />
</ValueMappings>
```

### Parameters
| Parameter                              | Description | Default | Remark |
|:---                                    |:--- |:--- |:--- |
| inputNode[^1]                          | The XPath to apply on the model (relative to the modelXPath of the ModelAttributeInjection) to get to the node which contains the value to apply the value translation on. | | See [XPath](../XPath). |

### Child sections
| Section                            | Description |
|:---                                |:--- |
| ValueMapping                       | See [ValueMapping](#valuemapping) |


## ValueMapping
A ValueMapping element can be used insude a ValueMappings element to specify a translation from a specific output value to one output value.

### Syntax
``` xml
<ValueMapping inputValue="..." outputValue="..." />
```

### Parameters
| Parameter                              | Description | Default | Remark |
|:---                                    |:--- |:--- |:--- |
| inputValue[^1]                         | The input value for this value mapping (the value of the inputNode). | | |
| outputValue[^1]                        | The output value for this value mapping. | | |


## ModelNodeRemoval
Model node removal can be used to remove certain elements or attributes from the model before the generation starts. This can be usefull when the model contains more information then should be used in a certain generation step. For example when a source model is automatically extracted from a source system and you don't want to include certain columns in the extraction process, you can remove these nodes from the model before generating.

### Syntax
``` xml
<ModelNodeRemoval
  modelXPath="..."
/>
```

### Parameters
| Parameter                              | Description | Default | Remark |
|:---                                    |:--- |:--- |:--- |
| modelXPath[^1]                         | The XPath to apply on the model to get to the element or attribute which needs to be removed. When the XPath resolves to multiple elements or attributes, all will be removed. | | See [XPath](../XPath). |


[comment]: Footnotes
[^1]: required parameter
[^2]: one of the parameters or child sections is required