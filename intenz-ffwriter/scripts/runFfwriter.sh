#!/bin/bash
# Parameters (optional):
#   $1: EC number to be exported (just one)
# If none provided, all SIB entries are exported.

SCRIPT_DIR=`dirname $0`

function addJarsToCp(){
    for JAR in $1/*.jar
    do
        [ -z $CP ] && CP=$JAR || CP=$CP:$JAR
    done
}

cd $SCRIPT_DIR/..

mvn -P apps clean package

#CP=src/main/appResources
addJarsToCp target
date
echo "Exporting ${1-all SIB entries}..."
java -Xmx512M -cp $CP uk.ac.ebi.intenz.tools.sib.EnzymeFlatFileWriterApp $@
date
echo "Finished"
