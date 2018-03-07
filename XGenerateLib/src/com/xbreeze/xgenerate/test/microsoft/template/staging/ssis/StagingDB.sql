USE [master];
GO

CREATE DATABASE [Staging];
GO

USE [Staging];
GO

CREATE SCHEMA [system_name];
GO

CREATE TYPE [dbo].[attribute_fulldatatype] FROM [varchar](10) NOT NULL;
GO

CREATE TABLE [system_name].[entity_name](
	[attribute_name] [dbo].[attribute_fulldatatype] NULL,
	[StageDateTime] [datetime2](2) NOT NULL
);
GO