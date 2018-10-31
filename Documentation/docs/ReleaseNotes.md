# Release notes

Here you can find the release notes of all CrossGenerate releases up till the release this documentation applies to.
Click on the header of a version number to go to the documentation of that specific version.

[//]: # (Use the following example to create the release notes for a new release.)
[//]: # ()
[//]: # (## Version X.X <sup>[docs](../X.X/)</sup>)
[//]: # ()
[//]: # (- [ ] x.x.x <sup>xx-xx-xxxx</sup>)
[//]: # (>)
[//]: # (> !!! success "New features")
[//]: # (>     * [ ] Template)
[//]: # (>         * [ ] XML Template)
[//]: # (>             - [X] Some new feature...)
[//]: # (> !!! "Enhanced features")
[//]: # (>     * [ ] Model)
[//]: # (>         - [X] Some enhanced feature...)
[//]: # (> !!! warning "Bug fixes")
[//]: # (>     * [ ] Binding)
[//]: # (>         * [X] Some issue...)

## [Version 2.3](../2.3/)
- [ ] 2.3.2 <sup>dd-mm-2018</sup>
> !!! warning "Bug fixes"
>     * [ ] Model
>         * [ ] Model Attribute injection
>             - [X] Error message when applying incorrect XPath in targetXPath attribute was misleading, suggesting incorrect modelXPath instead of targetXPath

- [ ] 2.3.1 <sup>11-09-2018</sup>
> 
> !!! warning "Bug fixes"
>     * [ ] Template
>         * [ ] XML Template
>             - [X] TemplatePlaceholderInjection ignores namespace part of template node

- [ ] 2.3 <sup>25-07-2018</sup>
>
> !!! success "New features"
>     * [ ] Template
>         * [ ] XML Template
>             - [X] Remove a node from the template (TemplateNodeRemoval)
> !!! warning "Bug fixes"
>     * [ ] Template
>         * [ ] XML Template
>             - [X] Wrong section bounds found for XML nodes when template contains special characters

## [Version 2.2](../2.1/)

- [ ] 2.2 <sup>25-06-2018</sup>
> 
> !!! success "New features"
>     * [ ] Model
>         - [X] Remove a node from the model (ModelNodeRemoval)

## [Version 2.1](../2.1/)

- [ ] 2.1 <sup>03-06-2018</sup>
> 
> !!! success "New features"
>     * [ ] Commandline options
>         - [X] Enable debug mode
>         - [X] Specify log level and destination
>     * [ ] Template
>         * [ ] XML Template
>             - [X] Support text template in XML templates (TextTemplate)
> !!! warning "Bug fixes"
>     * [ ] Template
>         * [ ] Text Template
>             - [X] Issue with adjecant placeholders in section based template
>             - [X] Suffix are on next line when section includes newline
>         * [ ] XML Template
>             - [X] XML Output formatting is not same as input

## [Version 2.0](../2.0/)

- [ ] 2.0 <sup>22-02-2018</sup>
> 
> !!! success "New features"
>     * [ ] Commandline options
>         - [X] Specify Model - Template - Config combinations
>     * [ ] Application config
>         - [X] Specify folder locations (config, model, output & template)
>     * [ ] Generation config
>         - [X] Configure whether output is a single or multiple files (Output)
>     * [ ] Model
>         - [X] Support for a XML model file
>         - [X] Inject an attribute into a model element (ModelAttributeInjection)
>     * [ ] Template
>         * [ ] Text Template
>             - [X] Define a section in text template
>             - [X] Define a section in the config for text templates (TextSection)
>             - [X] Support for prefixes and suffixes in a Section (TextSection.prefix & TextSection.suffix)
>             - [X] Add template comments to a text template (Comment annotation)
>             - [X] Specify annotation format for a text template (FileFormat)
>             - [X] Specify placeholder format for a text template (FileFormat)
>             - [X] Specify comment format for a text template (FileFormat)
>         * [ ] XML Template
>             - [X] Define a section in a XML template
>             - [X] Define a section in the config for XML templates (XmlSection)
>             - [X] Add template comments to a XML template (Comment annotation)
>             - [X] Inject an attribute into a XML template element (TemplateAttributeInjection)
>             - [X] Inject a placeholder in an existing XML node (element or attribute) (TemplatePlaceholderInjection)
>             - [X] Specify annotation format for a xml template (FileFormat)
>             - [X] Specify placeholder format for a xml template (FileFormat)
>             - [X] Specify comment format for a xml template (FileFormat)
>     * [ ] Binding
>         * [X] Bind a model element on a template section (SectionModelBinding)
>         * [X] Define a placeholder for a model element within a section binding (Placeholder)
