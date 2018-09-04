# XPath
XPath is a method for traversing elements and attributes (nodes) in a XML document. In CrossGenerate this is used in the configuration files to point to model and XML template nodes.

## Basics
For the basic explanation on XPath, please consult the following website:

- <a href="https://www.w3schools.com/xml/xpath_intro.asp" target="_blank">W3Schools - XPath Tutorial</a>

## Examples
Here some examples are given which represent commonly occuring configurtions containg XPath when using CrossGenerate.

### Selection
When using XPath an important concept is the use of axis. When traversing nodes the first part of the XPath is always the axis.

#### Child node selection
The '/' at the beginning of the expression makes sure it selects all attribute elements relative to the current node.
``` xml
/attribute
```

#### Child attribute selection
In this example we want to select all name attributes of the current node.
``` xml
/@name
```

#### Parent attribute value
To get to a node of the parent use the '../' axis. For example here we select the name attribute of the parent.
``` xml
../@name
```

#### Any attribute selection
The '//' at the beginning of the expression makes sure it selects all attribute elements no matter where they reside in the model (so not only of the current node).
``` xml
//attribute
```

### Filtering

#### Filter on a element
In this example we want to select the attributes where the datatype is 'varchar'.
``` xml
/attribute[@datatype='varchar']
```

#### Case insenstive filter
To make the comparison in the filter case insensitive, make sure to use lower-case() or upper-case() functions before comparing the value.
``` xml
/attribute[lower-case(@datatype)='varchar']
```
One can also use different boolean type function to perform checks in filters. For example contains(), starts-with() & ends-with().

#### Filter on attribute
Here we add a filter on the current datatype attribute. See how we use the '.' to select the current node value.
``` xml
/attribute/@datatype[.='varchar']
```

### Functions
There are quite some functions available in XPath to perform different types of actions. For example string manipulation or math functions. See the 'Saxon - XPath Functions' reference on the bottom of this page of a full list of functions available.

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

## References
In this section a list of relevant references is given.

- <a href="https://www.w3schools.com/xml/xml_xpath.asp" target="_blank">W3Schools - XML and XPath</a>
- <a href="https://www.w3schools.com/xml/xpath_intro.asp" target="_blank">W3Schools - XPath Tutorial</a>
- <a href="https://www.saxonica.com/html/documentation/expressions/" target="_blank">Saxon - XPath Expression Syntax</a>
- <a href="https://www.saxonica.com/html/documentation/functions/fn/" target="_blank">Saxon - XPath Functions</a>