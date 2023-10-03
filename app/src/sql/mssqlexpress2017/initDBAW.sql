IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'DBAW')
  BEGIN
    CREATE DATABASE DBAW
  END

use DBAW
drop table Users
create table Users(UserName varchar(30), ID int)
select * from Users
insert into Users values('Andrzej Wysocki',1020)
insert into Users values('user1', 3213)
insert into Users values('user2', 3021)
select * from Users

drop table Products
create table Products(
    ID int IDENTITY(1,1),
    ProductName VARCHAR(30) UNIQUE NOT NULL,
    ProductQuantity int NOT NULL,
	PRIMARY KEY (ID),

)
select* from Products
insert into Products(ProductName, ProductQuantity) values('Product #1', 108);
insert into Products(ProductName, ProductQuantity) values('Product #2', 1080);
insert into Products(ProductName, ProductQuantity) values('Product #3', 10);
insert into Products(ProductName, ProductQuantity) values('Product #4', 18);
insert into Products(ProductName, ProductQuantity) values('Product #5', 18);
insert into Products(ProductName, ProductQuantity) values('Product #6', 100);
insert into Products(ProductName, ProductQuantity) values('Product #7', 100);
insert into Products(ProductName, ProductQuantity) values('Product #8', 100);
insert into Products(ProductName, ProductQuantity) values('Product #9', 108);

select * from Products

