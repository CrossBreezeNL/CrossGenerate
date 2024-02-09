# Address book

This page contains examples that use an address book as an example.
Several important concepts of CrossGenerate are illustrated using the examples on this page. Have a look at the [hello world](./helloworld.md) example for the basics first.

Like with the hello world example, all examples contain the same types of files (template, config etc) but only files that introduce a new concept are described on this page.
The downloads that are provided for each example however do contain the complete set of files.

You need to [download](https://generate.x-breeze.com/download/){target=_blank} CrossGenerate to run the examples on your computer.

## Model
All examples on this page use the Addressbook model shown below

```xml
<?xml version="1.0" encoding="UTF-8"?>
<AddressBook name="AddressBook">
    <Person id="1" firstname="John" lastname="Smith" function="teacher" />
    <Person id="2" firstname="Jane" lastname="Smith" function="student" />
    <Person id="3" firstname="Henry" lastname="Jones" function="student" />
    <Person id="4" firstname="Maria" lastname="Evans" function="teacher" />
</AddressBook>
```

## Example 1: A simple address file
In this example, the content of the model is used to generate a text file with a warm welcome for every contact in the address book.

### Template
The template file is a text file with the following content. Note that in this example, the text file contains a directive for CrossGenerate, an annotation called `@XGenTextSection`. This means that the line directly following the annotation is marked as a section, in this case a section named 'PersonSection'.

```
--@XGenTextSection(name='PersonSection')
Hello PersonPlaceholder_firstname !

```

### Config
Like with the hello world example, the config defines a root section. Here it is called AddressBookSection. Remember that another section is defined in the template using the `@XGenTextSection` in the previous paragraph.
The `Binding` element in the config defines section model bindings for both sections. The PersonSection is defined within the scope of the AddressBookSection. As can be seen, the modelXPath is in this case also relative to the encompassing section, e.g. Person under AddressBook.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>  
  <!-- Generate using a text template, where the root section is called 'AddressBookSection'.  -->
  <TextTemplate rootSectionName="AddressBookSection">
    <!-- Specify the placeholder part for accessing an attribute of the mapped model element. -->
    <FileFormat singleLineCommentPrefix="--" />
    <!-- We want all output to be written to a single file. -->
    <Output type="single_output" />
  </TextTemplate>

  <Binding>
    <!-- Map the section 'AddressBookSection' of the template on the 'AddressBook' element of the model and scan the section in the template for the 'AddressBookPlaceholder'. -->
    <SectionModelBinding section="AddressBookSection" modelXPath="/AddressBook" placeholderName="AddressBookPlaceholder">
      <!-- Within the AddressBookSection PersonSection is mapped on the 'Person' element of the model. -->
      <SectionModelBinding section="PersonSection" modelXPath="Person" placeholderName="PersonPlaceholder" />
    </SectionModelBinding>
  </Binding>

</XGenConfig>
```

### Output
When running CrossGenerate the following output is created:
```
Hello John !
Hello Jane !
Hello Henry !
Hello Maria !
```

### Download
The files for this example can be [downloaded here](./002_AddressBook.zip).


## Example 2: A filtered address file
For this example, only teachers need to be targeted. This means a filter should be applied on the model data.

### Template
The template file is very similar to the previous example, only the section and placeholders are named differently to avoid confusion.
```
--@XGenTextSection(name='TeacherSection')
Hello TeacherPlaceholder_firstname !

```

### Config
The config is again similar to the previous example, note that now there is a filter in the modelXPath expression of the section model binding for `TeacherSection`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<XGenConfig>  
  <!-- Generate using a text template, where the root section is called 'AddressBookSection'.  -->
  <TextTemplate rootSectionName="AddressBookSection">
    <!-- Specify the placeholder part for accessing an attribute of the mapped model element. -->
    <FileFormat singleLineCommentPrefix="--" />
    <!-- We want all output to be written to a single file. -->
    <Output type="single_output" />
  </TextTemplate>
  <Binding>
    <!-- Map the section 'AddressBookSection' of the template on the 'AddressBook' element of the model and scan the section in the template for the 'AddressBookPlaceholder'. -->
    <SectionModelBinding section="AddressBookSection" modelXPath="/AddressBook" placeholderName="AddressBookPlaceholder">
      <!-- Within the AddressBookSection TechaerSection is mapped on the 'Person' element of the model, for persons that have teacher as their function. -->
      <SectionModelBinding section="TeacherSection" modelXPath="Person[@function='teacher']" placeholderName="TeacherPlaceholder"/>
    </SectionModelBinding>
  </Binding>
</XGenConfig>
```

### Output
When running CrossGenerate the following output is created:
```
Hello John !
Hello Maria !
```

### Download
The files for this example can be [downloaded here](./003_AddressBook_filter.zip).