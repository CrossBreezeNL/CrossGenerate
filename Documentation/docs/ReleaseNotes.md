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

## [Version 2.7](../2.7/)

- [ ] 2.7.0 <sup>XX-XX-2019</sup>
>
> !!! success "New features"
>     * [ ] Config
>         * [ ] Model
              - [X] Added ValueMappings support in ModelAttributeInjection in order to perform a translation on a model node.

## [Version 2.6](../2.6/)

- [ ] 2.6.0 <sup>24-05-2019</sup>
>
> !!! success "New features"
>     * [ ] Commandline options
>         - [X] Option to enable progress screen
> !!! info "Enhanced features"
>     * [ ] Config
>         * [ ] Text sections
>             - [X] When no single or multiline comment prefix or suffix markers have been specified in the FileFormat a log message is given (INFO level) to support troubleshooting.
>             - [X] Added 'all' prefix and suffix style.
>         * [ ] Model
              - [X] Added support for model XML files with namespaces. Namespaces needed for section model binding, model attribute injection and model node removal can now be specified in the model config. 
> !!! warning "Bug fixes"
>     * [ ] Template
>         * [ ] Text template
>             - [X] Fixed issue where end of a section could also be found in the begin part of a section.
>             - [X] Fixed issue where a section can't be created inside a comment.

## [Version 2.5](../2.5/)

- [ ] 2.5.0 <sup>04-01-2019</sup>
>
> !!! info "Enhanced features"
>     * [ ] Template
>         * [ ] Text and XML template section annotations
>             - [X] Within a section annotation, the placeholderName from the ModelSectionBinding can be overridden with a specific placeholderName for the section.
>             - [X] Within a section annotation, the placeholderName from the ModelSectionBinding can be overridden with a specific placeholderName for the section.
>     * [ ] Config
>         * [ ] Config re-use    
>             - [X] Include directive in config now better supports relative includes (relative to the referencing file instead of application folder)
>             - [X] Include directive in config supports including parts of another config file with the xpointer function
>         * [ ] Text and XML sections
>             - [X] A section can be marked as optional. When the section is not found in the template, an informational message will be given instead of a warning.
> !!! warning "Bug fixes"
>     * [ ] Config
>         * [ ] Text and XML sections
>             - [X] Reduced severity of sections defined in config but not found in template to warning or informational. This can be a result of applying a more generic config, in which case it is intended behaviour.

## [Version 2.4](../2.4/)

- [ ] 2.4.0 <sup>07-11-2018</sup>
>
> !!! success "New features"
>     * [ ] Binding
>         * [ ] Bind a model element on a template section (SectionModelBinding)
>             - [X] Specify a variable name for the current element within a SectionModelBinding (variableName)
>         * [ ] Define a placeholder for a model element within a section binding (Placeholder)
>             - [X] Specify a variable name for the element the modelXPath of the placeholder points to (variableName)
>         * [X] Define a literal (placeholder) for a model element within a section binding (Literal)
> !!! info "Enhanced features"
>     * [X] Improved error message on XPath parse error for Model & XML Template XPath expressions
> !!! warning "Bug fixes"
>     * [ ] Model
>         * [ ] Model Attribute injection
>             - [X] Error message when applying incorrect XPath in targetXPath attribute was misleading, suggesting incorrect modelXPath instead of targetXPath
>     * [ ] Template
>         * [ ] Text template
>             - [X] With sections with a prefix the leading whitespace was included twice for first repetition
>             - [X] Carriage return with line-feed (CRLF) in template were converted into LF and removed from placeholder value

## [Version 2.3](../2.3/)

- [ ] 2.3.1 <sup>11-09-2018</sup>
> 
> !!! warning "Bug fixes"
>     * [ ] Template
>         * [ ] XML Template
>             - [X] TemplatePlaceholderInjection ignored namespace part of template node

- [ ] 2.3 <sup>25-07-2018</sup>
>
> !!! success "New features"
>     * [ ] Template
>         * [ ] XML Template
>             - [X] Remove a node from the template (TemplateNodeRemoval)
> !!! warning "Bug fixes"
>     * [ ] Template
>         * [ ] XML Template
>             - [X] Wrong section bounds found for XML nodes when template contained special characters

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
>             - [X] Adjecant placeholders were not resolved in section based template
>             - [X] Suffix was on next line when section includes newline
>         * [ ] XML Template
>             - [X] XML Output formatting was not same as input

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
