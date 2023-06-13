# Create templates

After we created all config files, now it's time to create the templates of the source code that should be generated based on our model. 
The main goal of this tutorial is to generate a historical staging area. To make it posible to have a historical staging area of our source-database, we need to build a Visual Studio database-project. we also need to create text templates that helps to generate database tables, views and stored procedure.

In this tutorial we will create the folowing templates:

- Database-project
- Target-database
- Schmea's of the target-database
- Target-database tables
- Stored procedures to copy data from the staging tables to the archive tables

## Attachments

The entire tutorial, including all config-files and templates, can be found in the following zip file:

- [Tutorial.zip](../CrossGenerate_Tutorial.zip)