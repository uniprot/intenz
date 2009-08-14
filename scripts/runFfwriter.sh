#!/bin/bash
# Parameters (optional):
#   $1: ec1
#   $2: ec2
#   $3: ec3
#   $4: ec4
# If none provided, all SIB entries are exported.

SCRIPT_DIR=`dirname $0`

function addJarsToCp(){
    for JAR in $1/*.jar
    do
        [ -z $CP ] && CP=$JAR || CP=$CP:$JAR
    done
}

cd $SCRIPT_DIR/..

mvn clean package

CP=src/main/appResources
addJarsToCp target
java -Xmx512M -cp $CP uk.ac.ebi.intenz.tools.sib.EnzymeFlatFileWriterApp $@

