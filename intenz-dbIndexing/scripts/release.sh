#!/bin/bash

cd `dirname $0`/..
svn update
mvn clean package
[ $? != 0 ] && echo "MAVEN BUILD FAILURE" && exit 1

. scripts/updateIntEnzText.sh
[ $? != 0 ] && echo "FAILURE UPDATING intenz_text TABLE!" && exit 1

. scripts/updateId2Ec.sh
[ $? != 0 ] && echo "FAILURE UPDATING id2ec TABLE!" && exit 1

DB_MODULE_HOME=$(dirname $0)/../../intenz-database
echo exit | sqlplus ${DB_USER}/${DB_PASSWD}@${DB_INSTANCE} \
    @${DB_MODULE_HOME}/src/main/sql/grant_to_roles 
