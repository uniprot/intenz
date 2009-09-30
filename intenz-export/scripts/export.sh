#!/bin/bash
#
# Exports IntEnz production database in all formats.

EXPORT_HOME=`dirname $0`/..
CONFIG_DIR=$EXPORT_HOME/src/main/appResources}

`dirname $0`/doExport.sh $CONFIG_DIR

gzip $SITEMAP_OUT_DIR/sitemap.xml
cp $SITEMAP_OUT_DIR/sitemap.xml.gz $INTENZ_PUBLIC_DIR/src/main/webapp

gzip $BIOPAX_OUT_DIR/intenz-biopax.owl

cp $EXPORT_HOME/src/main/resources/{intenz,enzyme}.xsd $XML_OUT_DIR
cp $EXPORT_HOME/src/main/resources/IntEnzXML-README.txt $XML_OUT_DIR
cd $XML_OUT_DIR
zip -r IntEnzXML.zip *

