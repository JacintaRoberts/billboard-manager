CREATE DATABASE IF NOT EXISTS BillboardDatabase;

USE BillboardDatabase;

DROP TABLE IF EXISTS `BillboardDatabase`.`Users`;

DROP TABLE IF EXISTS `BillboardDatabase`.`Billboards`;

DROP TABLE IF EXISTS `BillboardDatabase`.`Schedules``;


CREATE TABLE IF NOT EXISTS `BillboardDatabase`.`Users` (
    `Username` varchar(255) NOT NULL default '',
    `Password` varchar(255) NOT NULL default '',
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