#!/bin/bash
#
# Exports IntEnz production database in all formats.
# Parameters:
#   $1 - [optional] configuration files directory

if [ -z $INTENZ_CONFIG_DIR ]
then
    echo "Set variable INTENZ_CONFIG_DIR to a directory containing database configuration"
    exit 1
fi

EXPORT_HOME=`dirname $0`/..
CONFIG_DIR=${1:-$EXPORT_HOME/src/main/appResources}
INTENZ_DIR=$EXPORT_HOME/..
INTENZ_PUBLIC_DIR=$INTENZ_DIR/webapp/intenz-public

SITEMAP_OUT_DIR=`grep 'intenz.export.sitemap.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
[ -d $SITEMAP_OUT_DIR ] || mkdir -p $SITEMAP_OUT_DIR

BIOPAX_OUT_DIR=`grep 'intenz.export.biopax.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
[ -d $BIOPAX_OUT_DIR ] || mkdir -p $BIOPAX_OUT_DIR

XML_OUT_DIR=`grep 'intenz.export.xml.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
[ -d $XML_OUT_DIR ] || mkdir -p $XML_OUT_DIR

cd $EXPORT_HOME
svn update
mvn clean package

PROXY_OPTS=$(grep 'ebi.proxy.java.options' $INTENZ_CONFIG_DIR/ebi-proxy.properties | cut -d '=' -f 2)

MEM_OPTS="-Xmx512M"

CP=$CONFIG_DIR
for JAR in $EXPORT_HOME/target/*.jar
do
    CP=$CP:$JAR
done

java $PROXY_OPTS $MEM_OPTS -cp $CP uk.ac.ebi.intenz.tools.export.ExporterApp

gzip $SITEMAP_OUT_DIR/sitemap.xml
cp $SITEMAP_OUT_DIR/sitemap.xml.gz $INTENZ_PUBLIC_DIR/src/main/webapp

gzip $BIOPAX_OUT_DIR/intenz-biopax.owl

cp $EXPORT_HOME/src/main/resources/{intenz,enzyme}.xsd $XML_OUT_DIR
cp $EXPORT_HOME/src/main/resources/IntEnzXML-README.txt $XML_OUT_DIR
cd $XML_OUT_DIR
zip -r IntEnzXML.zip *

