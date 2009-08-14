#!/bin/bash
#
# Exports IntEnz production database in all formats.
# Parameters:
#   $1 - [optional] configuration files directory

EXPORT_HOME=`dirname $0`/..
CONFIG_DIR=${1:-$EXPORT_HOME/src/main/appResources}
INTENZ_DIR=$EXPORT_HOME/..
INTENZ_PUBLIC_DIR=$INTENZ_DIR/webapps/public/src/main/resources

cd $EXPORT_HOME
cvs update
mvn clean package

PROXY_OPTS=$(grep 'ebi.proxy.java.options' $CONFIG_DIR/ebi-proxy.properties | cut -d '=' -f 2)

MEM_OPTS="-Xmx512M"

CP=$CONFIG_DIR:$INTENZ_DIR:$INTENZ_PUBLIC_DIR
for JAR in $EXPORT_HOME/target/*.jar
do
    CP=$CP:$JAR
done

java $PROXY_OPTS $MEM_OPTS -cp $CP uk.ac.ebi.intenz.tools.export.ExporterApp

SITEMAP_OUT_DIR=`grep 'intenz.export.sitemap.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
gzip $SITEMAP_OUT_DIR/sitemap.xml
cp $SITEMAP_OUT_DIR/sitemap.xml.gz ../../webapps/public/src/main/webapp

BIOPAX_OUT_DIR=`grep 'intenz.export.biopax.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
gzip $BIOPAX_OUT_DIR/intenz-biopax.owl

XML_OUT_DIR=`grep 'intenz.export.xml.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
cp $EXPORT_HOME/src/main/resources/{intenz,enzyme}.xsd $XML_OUT_DIR
cp $EXPORT_HOME/src/main/resources/IntEnzXML-README.txt $XML_OUT_DIR
cd $XML_OUT_DIR
zip -r IntEnzXML.zip *

