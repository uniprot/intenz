#!/bin/bash
#
# Exports IntEnz production database in all formats.

EXPORT_HOME=`dirname $0`/..
CONFIG_DIR=$EXPORT_HOME/src/main/appResources

SITEMAP_OUT_DIR=`grep 'intenz.export.sitemap.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
[ -d $SITEMAP_OUT_DIR ] || mkdir -p $SITEMAP_OUT_DIR

BIOPAX_OUT_DIR=`grep 'intenz.export.biopax.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
[ -d $BIOPAX_OUT_DIR ] || mkdir -p $BIOPAX_OUT_DIR

XML_OUT_DIR=`grep 'intenz.export.xml.output.dir' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`
[ -d $XML_OUT_DIR ] || mkdir -p $XML_OUT_DIR

EXPORT_FORMAT=`grep 'intenz.export.format' $CONFIG_DIR/intenz-export.properties | cut -d '=' -f 2`

`dirname $0`/doExport.sh $CONFIG_DIR

cd $EXPORT_HOME

if [ "${EXPORT_FORMAT#*SITEMAP*}" != "$EXPORT_FORMAT" ]
then
	gzip $SITEMAP_OUT_DIR/sitemap.xml
fi

if [ "${EXPORT_FORMAT#*BIOPAX*}" != "$EXPORT_FORMAT" ]
then
	gzip $BIOPAX_OUT_DIR/intenz-biopax.owl
fi

if [ "${EXPORT_FORMAT#*INTENZ_XML*}" != "$EXPORT_FORMAT" ]
then
	cp $EXPORT_HOME/../intenz-xml/src/main/resources/{intenz,enzyme}.xsd $XML_OUT_DIR
	cp $EXPORT_HOME/src/main/resources/IntEnzXML-README.txt $XML_OUT_DIR
	cd $XML_OUT_DIR
	zip -r IntEnzXML.zip *
fi
