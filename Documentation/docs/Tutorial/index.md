# Tutorial
Generating historical stage using MS-SQL and X-Generate.


## Introduction
In this tutorial we will learn how to build a historical stage based on our source SQL-database. We will generate XML-model from our database in SQL Server. Then we’ll use our XML-model to generate Integration Service project and Database project. Then we will use those two projects to copy data from the source model to the staging and archive stages in our database.

## Prerequisites
Before you start with the tutorial install the following tools and applications:

-	Installed X-Generate 
-	SQL Server 2019 (Developer Edition)
-	Visual Studio
-	SQL Server Data Tools
-	SSIS (in Visual Studio and in SQL server)

Note: If X-generate is a new tool for you, you can follow the [Getting started](../../GettingStarted) examples to learn more about X-generate.