# Generate XML model
In this tutorial we will use a source-database with two tables Customer and Order. First you need to create the database and fill it and then we will generate XML-file of the source data model based on the source-database structure.

## Create the source-database 
Run the flowing SQL script to create the source-database and its tables and to fill the tables with some records.

``` sql
USE [master]
GO
/****** Object:  Database [Example_source]    Script Date: 2020-04-27 10:14:51 PM ******/
CREATE DATABASE [Example_source]
 CONTAINMENT = NONE
GO
ALTER DATABASE [Example_source] SET COMPATIBILITY_LEVEL = 130
GO
GO
ALTER DATABASE [Example_source] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Example_source] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Example_source] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Example_source] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Example_source] SET ARITHABORT OFF 
GO
ALTER DATABASE [Example_source] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Example_source] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Example_source] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Example_source] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Example_source] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Example_source] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Example_source] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Example_source] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Example_source] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Example_source] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Example_source] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Example_source] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Example_source] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Example_source] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Example_source] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Example_source] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Example_source] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Example_source] SET RECOVERY FULL 
GO
ALTER DATABASE [Example_source] SET  MULTI_USER 
GO
ALTER DATABASE [Example_source] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Example_source] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Example_source] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Example_source] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Example_source] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'Example_source', N'ON'
GO
ALTER DATABASE [Example_source] SET QUERY_STORE = OFF
GO
USE [Example_source]
GO
/****** Object:  Schema [ExampleSchema]    Script Date: 2020-04-27 10:14:51 PM ******/
CREATE SCHEMA [ExampleSchema]
GO
/****** Object:  Schema [User_3]    Script Date: 2020-04-27 10:14:51 PM ******/
CREATE SCHEMA [User_3]
GO
/****** Object:  Table [dbo].[Customer]    Script Date: 2020-04-27 10:14:51 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Customer](
	[Id] [int] NOT NULL,
	[FirstName] [varchar](50) NULL,
	[LastName] [varchar](100) NULL,
	[City] [varchar](50) NULL,
	[Country] [varchar](3) NULL,
	[Phone] [varchar](20) NULL,
 CONSTRAINT [PK_CUSTOMER] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Order]    Script Date: 2020-04-27 10:14:51 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Order](
	[Id] [int] NOT NULL,
	[OrderDate] [datetime] NULL,
	[OrderNumber] [varchar](50) NULL,
	[CustomerId] [int] NULL,
	[TotalAmount] [decimal](12, 2) NULL,
 CONSTRAINT [PK_ORDER] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Order]  WITH CHECK ADD  CONSTRAINT [FK_ORDER_ORDER_CUS_CUSTOMER] FOREIGN KEY([CustomerId])
REFERENCES [dbo].[Customer] ([Id])
GO
ALTER TABLE [dbo].[Order] CHECK CONSTRAINT [FK_ORDER_ORDER_CUS_CUSTOMER]
GO

INSERT [dbo].[Customer] ([Id], [FirstName], [LastName], [City], [Country], [Phone]) VALUES (1, N'John', N'Smith', N'New York', N'USA', N'123456')
GO
INSERT [dbo].[Customer] ([Id], [FirstName], [LastName], [City], [Country], [Phone]) VALUES (2, N'Laura', N'Green', N'Alabama', N'USA', N'654321')
GO
INSERT [dbo].[Order] ([Id], [OrderDate], [OrderNumber], [CustomerId], [TotalAmount]) VALUES (1, CAST(N'2020-04-20T00:00:00.000' AS DateTime), N'1', 1, CAST(120.00 AS Decimal(12, 2)))
GO
INSERT [dbo].[Order] ([Id], [OrderDate], [OrderNumber], [CustomerId], [TotalAmount]) VALUES (2, CAST(N'2020-04-22T00:00:00.000' AS DateTime), N'2', 2, CAST(125.00 AS Decimal(12, 2)))
GO

USE [master]
GO
ALTER DATABASE [Example_source] SET  READ_WRITE 
GO
```

## Generate XML-file
To generate the XML-file based on the database you will need to use the system views of the Information_Schema. To generate the xml file from our source database, connect to the source-database in SQL Server management Studio and execute the following SQL-script. 
Note: to save the query-result in XML-file you can use CTRL+Shift+F before executing the SQL-script. Then, after executing the script SSMS will ask you to choose the directory in which you will want to save your XML-file. 

```sql
SELECT (select 'Example_source' as "@name",
        (select T.[TABLE_NAME] as "@name",
                (select column_name as "@name",
                 Cast(  CASE IS_NULLABLE
                        WHEN 'Yes' THEN 'false'
                        WHEN 'No' THEN 'true'
                        end  as Varchar(100)) as "@required",
                cast ( Case 
                        WHEN column_name IN (select C1.COLUMN_NAME FROM  
                                                INFORMATION_SCHEMA.TABLE_CONSTRAINTS TC1  
                                                JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE C1  
                                                ON C1.CONSTRAINT_NAME=TC1.CONSTRAINT_NAME  
                                                WHERE  
                                                 TC1.CONSTRAINT_TYPE='PRIMARY KEY' and C1.TABLE_NAME= C.TABLE_NAME  )
                                                 THEN 'true'
                                                 else 'false'
                                                 end
                                                 as varchar(10)
                ) as "@primary" ,
                (SELECT [Type] = CASE 
                                    WHEN stp.[name] IN ('varchar', 'char') THEN stp.[name] + '(' + IIF(sc.max_length = -1, 'max', CAST(sc.max_length AS VARCHAR(25))) + ')' 
                                    WHEN stp.[name] IN ('nvarchar','nchar') THEN stp.[name] + '(' + IIF(sc.max_length = -1, 'max', CAST(sc.max_length / 2 AS VARCHAR(25)))+ ')'      
                                    WHEN stp.[name] IN ('decimal', 'numeric') THEN stp.[name] + '(' + CAST(sc.[precision] AS VARCHAR(25)) + ', ' + CAST(sc.[scale] AS VARCHAR(25)) + ')'
                                    WHEN stp.[name] IN ('datetime2') THEN stp.[name] + '(' + CAST(sc.[scale] AS VARCHAR(25)) + ')'
                                    ELSE stp.[name]
                                END
                FROM sys.tables st 
                JOIN sys.schemas ss ON st.schema_id = ss.schema_id
                JOIN sys.columns sc ON st.object_id = sc.object_id
                JOIN sys.types stp ON sc.user_type_id = stp.user_type_id
                Where sc.name= c.COLUMN_NAME  and st.name=t.TABLE_NAME
                ) as "@fulldatatype"
                from INFORMATION_SCHEMA.COLUMNS C
                where C.TABLE_NAME= T.TABLE_NAME
                for XML path ('column'), TYPE) as "columns" , 
                (SELECT TC.CONSTRAINT_NAME as "@id",
                    (select KCU.column_name as "@id", KCU.ORDINAL_POSITION as "@OrdinalPosition"
                    from INFORMATION_SCHEMA.KEY_COLUMN_USAGE KCU
                    where KCU.TABLE_NAME= T.TABLE_NAME
                    and KCU.CONSTRAINT_NAME = TC.CONSTRAINT_NAME
                    for XML path ('keyColumn'), TYPE ) as "keyColumns"
                FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS TC
                where TC.TABLE_NAME= T.TABLE_NAME and TC.CONSTRAINT_TYPE= 'PRIMARY KEY'
                for XML path ('primaryKey'), TYPE) as "keys"
        from INFORMATION_SCHEMA.TABLES T
        where TABLE_CATALOG='Example_source'
        for XML path('table'), TYPE) as "tables"
    for XML path('database'), Type ) 
```
This query should return an XML text that can be copied and saved in an XML-file for later use in the following steps.