# Model configuration

## Model

### Syntax
``` xml
<Model>    
  <ModelAttributeInjections>
    <ModelAttributeInjection ... />
  </ModelAttributeInjections>
</Model>
```

### Child sections
| Section                            | Description |
|:---                                |:--- |
| ModelAttributeInjection            | See [ModelAttributeInjection](#modelattributeinjection) |

## ModelAttributeInjection
Model attribute injection can be used to inject attributes to the model before generation starts. This is usefull for enriching the model with additional information that is needed during generation. An example of model attribute injection is adding ETL specific datatypes to an attribute or column definition,  derived from the database datatype that is already present in the model.
### Syntax
``` xml
<ModelAttributeInjection
  modelXPath="..."
  targetAttribute="..."
  targetValue="..."
  targetXPath="..."
/>
```

### Parameters
| Parameter                              | Description | Default | Remark |
|:---                                          |:--- |:--- |:--- |
| modelXPath[^1]                         | The XPath to apply on the model to get to the element on which to inject the attribute. | | |
| targetAttribute[^1]                     | The name of the attribute to inject. | |If the target attribute is already present in the model it's value will be overwritten |
| targetValue[^2]                         | The value of the attribute to inject. | | | 
| targetXPath[^2]                        | The XPath to apply on the model element to get the target value. | | | 


[comment]: Footnotes
[^1]: required parameter
[^2]: one of the parameters is required