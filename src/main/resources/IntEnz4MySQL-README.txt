IntEnz for MySQL
=============================================================================
This directory contains SQL files to setup the IntEnz schema in MySQL and
populate it with IntEnz data.
They have been successfuly tested with MySQL Community Server 5.1.
 

Usage
-----------------------------------------------------------------------------
$ cd <this directory>
$ mysql <your mysql login options>
mysql> create database intenz;
mysql> use intenz
mysql> source setup_mysql_intenz.sql


Limitations
-----------------------------------------------------------------------------
- IntEnz must use the InnoDB engine in order to preserve its foreign keys.
- Some of the primary keys are missing due to limitations in MySQL.
- 'CHECK' constraints are ignored by MySQL.

