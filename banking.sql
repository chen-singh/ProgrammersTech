create database banking;
use  banking;
create table accounts(create table accounts(accountnumber varchar(50) not null primary key ,name varchar(255) not null ,  balance int not null , pin
 int(4) not null);
create table user( create table user(email varchar(255) not null , accountnumber int not null  , password varchar(255) not null);