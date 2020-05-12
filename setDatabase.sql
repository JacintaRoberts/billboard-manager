/*
SQL File to Create Database for Billboard Application
By Team 60
*/

/*Create Database if it doesn't exist*/
CREATE DATABASE IF NOT EXISTS BillboardDatabase;

/*Use the Database as default*/
USE BillboardDatabase;

/*Create new copies of tables*/
CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Users` (
    `Username` varchar(255) NOT NULL default '',
    `Password` varchar(255) NOT NULL default '',
    `RandomSalt` varchar(255) NOT NULL default '',
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



/*Initial admin user - login is root, pass*/
INSERT INTO Users
VALUES ("root", "a461ab9266dbbec4623de686f806a23e69337f524527e282bb325092159f0d87",
"8bca1326370a157d9c33acd5a173440d9475d3955ae559872f47cfe34aa793bd", true, true, true, true);


INSERT INTO Billboards
VALUES ("TestBillboard", "TestUser", "TestXMLCODE");

INSERT INTO Billboards
VALUES ("TestBillboard2", "TestUser2", "TestXMLCODE2");

INSERT INTO Billboards
VALUES ("TestBillboard3", "TestUser3", "TestXMLCODE3");