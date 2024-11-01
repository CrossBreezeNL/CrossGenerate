# XPath
XPath is a method for traversing elements and attributes (nodes) in a XML document. In CrossGenerate this is used in the configuration files to point to model and XML template nodes.

## Basics
For the basic explanation on XPath, please consult the following website:

- <a href="https://www.w3schools.com/xml/xpath_intro.asp" target="_blank">W3Schools - XPath Tutorial</a>

## Examples
Here some examples are given which represent commonly occurring configurations containing XPath when using CrossGenerate.

### Selection
When using XPath an important concept is the use of axes. An axis is a relationship of a certain node or set of nodes in the XML document to the current node. When traversing nodes the first part of the XPath is usually the axis. If no axis is given, the current node (self) is the default. See <a href="https://www.w3schools.com/xml/xpath_axes.asp" target="_blank">W3Schools - XPath Axes</a> for an explanation of this concept.

#### Child element selection
The `./` at the beginning of the expression makes sure it selects all elements and attributes relative to the current node. In the example below we select the `attribute` element as a child of the current node.
``` xml
./attribute
```

By default XPath will select the child nodes, so the following XPath expression will yield the same results:
``` xml
attribute
```

#### Child attribute selection
In this example we want to select all `name` attributes of the current node. This will result in a single result or no result, since a node can have only one attribute with a certain name.
``` xml
@name
```

#### Parent attribute value
To get to the parent node use the `..` axis. For example here we select the `name` attribute of the parent.
``` xml
../@name
```

#### Any element selection
The '//' at the beginning of the expression makes sure it selects all nodes no matter where they reside in the XML document (so not only descendants of the current node).
``` xml
//attribute
```

### Filtering

#### Filter on an element
In this example we want to select the attribute elements where the datatype is `varchar`.
``` xml
attribute[@datatype='varchar']
```

#### Using functions in a filter
Besides the standard comparison operators (=, >, <, ...) you can also use XPath functions in a filter as long as the result of the expression is a boolean result.

An example is to make the comparison in the filter case insensitive. For this we will use lower-case() or upper-case() functions before comparing the value.
``` xml
attribute[lower-case(@datatype)='varchar']
```

One can also use different boolean type functions to perform checks in filters. For example contains(), starts-with() & ends-with().

#### Filter on element text

In the examples above we filter in the value of an attribute of an element, but you can also filter on the attribute, while selecting the attribute in the XPath. In the example below we add a filter on the current datatype attribute. See how we use the '.' to select the current node value.
``` xml
attribute/@datatype[.='varchar']
```

### Functions
There are quite some functions available in XPath to perform different types of actions. For example string manipulation or mathematical functions. See the references on the bottom of this page for an overview of all functions available.

#### Concatenate
Here we use the concat function to concatenate the current node name attribute and the parent node name attribute.
``` xml
concat(.@name, ' - ', ../@name)
```

#### Count
Here we use the count function to count the numbers of attributes.
``` xml
count(/attribute)
```

#### Replace[^1]
Here we use the replace function to replace (parts of) the value of the attribute `name`. In the example below we replace spaces with underscores.
``` xml
replace(@name, ' ', '_')
```

## References
In this section a list of relevant references is given.

- <a href="https://www.w3schools.com/xml/xml_xpath.asp" target="_blank">W3Schools - XML and XPath</a>
- <a href="https://www.w3schools.com/xml/xpath_intro.asp" target="_blank">W3Schools - XPath Tutorial</a>
- <a href="https://www.w3schools.com/xml/xsl_functions.asp" target="_blank">XPath Function Reference</a>
- <a href="https://www.w3.org/TR/xquery-operators/" target="_blank">XQuery and XPath Reference</a>

[comment]: Footnotes
[^1]: The replace XPath function is only available in `modelXPath` expressions and not in `templateXPath` expressions.