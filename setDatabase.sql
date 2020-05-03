/*
SQL File to Create Database for Billboard Application
By Team 60
*/

/*Create Database if it dosent exsits*/
CREATE DATABASE IF NOT EXISTS BillboardDatabase;

/*Use the Database as default*/
USE BillboardDatabase;

/*Drop tables if database exists these tables*/
DROP TABLE IF EXISTS `BillboardDatabase`.`Users`;
DROP TABLE IF EXISTS `BillboardDatabase`.`Billboards`;
DROP TABLE IF EXISTS `BillboardDatabase`.`Schedules`;

/*Create new copies of tables*/
CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Users` (
    `Username` varchar(255) NOT NULL default '',
    `Password` varchar(255) NOT NULL default '',
    `Salt`  varchar(255) NOT NULL default '',
    `CreateBillboard` bool default 0,
    `EditBillboard` bool default 0,
    `ScheduleBillboard` bool default 0,
    `EditUser` bool default 0,
    PRIMARY KEY (`Username`)
);


CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Billboards` (
    `BillboardName` varchar(255) NOT NULL default '',
      `Creator` varchar(255) NOT NULL default '',
      `XMLCode` TEXT,
      PRIMARY KEY (`BillboardName`)
  );


  CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Schedules` (
      `StartDateTime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `BillboardName` varchar(255) NOT NULL default '',
      `Duration` TIME NOT NULL DEFAULT '00:01:00',
      PRIMARY KEY (`StartDateTime`)
  );


INSERT INTO Billboards
VALUES ("TestBillboard", "TestUser", "TestXMLCODE");

INSERT INTO Billboards
VALUES ("TestBillboard2", "TestUser2", "TestXMLCODE2");

INSERT INTO Billboards
VALUES ("TestBillboard3", "TestUser3", "TestXMLCODE3");