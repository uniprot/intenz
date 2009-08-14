#!/bin/bash

SCRIPT_DIR=`dirname $0`
DBINDEXING_HOME=$SCRIPT_DIR/..
CONFIG_DIR=${1:-$DBINDEXING_HOME/src/main/resources}
TARGET_DB=`grep '^intenz.target.database' $CONFIG_DIR/db-indexing.properties | cut -d '=' -f 2`
DB_USER=`grep '^user' $CONFIG_DIR/${TARGET_DB}.properties | cut -d '=' -f 2`
DB_PASSWD=`grep '^password' $CONFIG_DIR/${TARGET_DB}.properties | cut -d '=' -f 2`
INTENZ_HOME=$DBINDEXING_HOME/../../..

CP=$CONFIG_DIR
for JAR in $DBINDEXING_HOME/target/*.jar
do
    CP=$CP:$JAR
done

