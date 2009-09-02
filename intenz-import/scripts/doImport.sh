#!/bin/bash
#
# Import external cross-references
# Parameters:
#   $1 configuration directory
#   $2 xrefs to be imported, one of GO or UNIPROT (default: all)

SCRIPT_DIR=`dirname $0`
CONFIG_DIR=$1

cd $SCRIPT_DIR/..
cvs update
mvn clean package

CP=$CONFIG_DIR
for JAR in $SCRIPT_DIR/../target/*.jar
do
    CP=$CP:$JAR
done

java -cp $CP uk.ac.ebi.intenz.tools.importer.ImportController $2
