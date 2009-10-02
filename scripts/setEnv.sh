#!/bin/bash
# Parameter:
# $1 - (optional) directory containing the configuration file
#      [default: $DBINDEXING_HOME/src/main/appResources]

if [ -z $INTENZ_CONFIG_DIR ]
then
    echo "Set variable INTENZ_CONFIG_DIR to a directory containing database configuration"
    exit 1
fi

SCRIPT_DIR=`dirname $0`
DBINDEXING_HOME=$SCRIPT_DIR/..
CONFIG_DIR=${1:-$DBINDEXING_HOME/src/main/appResources}
TARGET_DB=`grep '^intenz.target.database' $CONFIG_DIR/db-indexing.properties | cut -d '=' -f 2`
DB_USER=`grep '^user' $INTENZ_CONFIG_DIR/${TARGET_DB}.properties | cut -d '=' -f 2`
DB_PASSWD=`grep '^password' $INTENZ_CONFIG_DIR/${TARGET_DB}.properties | cut -d '=' -f 2`
DB_INSTANCE=`grep '^instance' $INTENZ_CONFIG_DIR/${TARGET_DB}.properties | cut -d '=' -f 2`
INTENZ_HOME=$DBINDEXING_HOME/..

CP=$INTENZ_CONFIG_DIR
for JAR in $DBINDEXING_HOME/target/*.jar
do
    CP=$CP:$JAR
done

