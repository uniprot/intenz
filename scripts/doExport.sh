#!/bin/bash
#
# Exports IntEnz production database.
# Parameters:
#   Those for ExporterApp (see javadocs)

if [ -z $INTENZ_CONFIG_DIR ]
then
    echo "Set variable INTENZ_CONFIG_DIR to a directory containing database configuration"
    exit 1
fi

EXPORT_HOME=`dirname $0`/..
INTENZ_DIR=$EXPORT_HOME/..
INTENZ_PUBLIC_DIR=$INTENZ_DIR/webapp/intenz-public

cd $EXPORT_HOME
mvn clean package

PROXY_OPTS=$(grep 'ebi.proxy.java.options' $INTENZ_CONFIG_DIR/ebi-proxy.properties | cut -d '=' -f 2)

MEM_OPTS="-Xmx1024M"

CP=$INTENZ_CONFIG_DIR:$INTENZ_PUBLIC_DIR/src/main/resources
for JAR in $EXPORT_HOME/target/*.jar
do
    CP=$CP:$JAR
done

java $PROXY_OPTS $MEM_OPTS -cp $CP uk.ac.ebi.intenz.tools.export.ExporterApp $@
