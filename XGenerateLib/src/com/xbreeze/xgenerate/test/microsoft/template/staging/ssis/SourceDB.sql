USE [master]
GO

CREATE DATABASE [system_name];
GO

USE [system_name]
GO

CREATE TYPE [attribute_fulldatatype] FROM [varchar](10) NOT NULL;
GO

CREATE TABLE [dbo].[entity_name](
	[attribute_name] [attribute_fulldatatype] NOT NULL
);
GO