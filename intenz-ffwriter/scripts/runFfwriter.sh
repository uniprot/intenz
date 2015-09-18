#!/bin/bash
# Parameters:
#   $1: configuration directory (optional)
#   $2: EC number to be exported (just one) (optional). If none provided,
#       all SIB entries are exported.

SCRIPT_DIR=$(dirname $0)

function addJarsToCp(){
    for JAR in $1/*.jar
    do
        [ -z $CP ] && CP=$JAR || CP=$CP:$JAR
    done
}

cd $SCRIPT_DIR/..

mvn -P apps clean package

if [ -d $1 ]
then
	CONF_DIR=$1
	shift
fi

CP=${CONF_DIR:-$SCRIPT_DIR/../src/main/appResources}
addJarsToCp target
date
echo "Exporting ${1:-all SIB entries}..."
java -Xmx2048M -cp $CP uk.ac.ebi.intenz.tools.sib.EnzymeFlatFileWriterApp $@
date
echo "Finished"
