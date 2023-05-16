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