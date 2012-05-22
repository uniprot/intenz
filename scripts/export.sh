#!/bin/bash
#
# Exports IntEnz production database in all formats.
# Parameters:
#   Those for ExporterApp (see javadocs)

`dirname $0`/doExport.sh $@

[ $? == 0 ] || exit 1

EXPORT_HOME=`dirname $0`/..
cd $EXPORT_HOME

# Parse parameters sent to ExporterApp in order to compress files:
for PARAM in $@
do
    case "$LAST_OPT" in
        '-sitemap'|'-biopax'|'-keggEnzyme')
			gzip $PARAM
			;;
        '-intenzXml')
			cp $EXPORT_HOME/../intenz-xml/src/main/resources/{intenz,enzyme}.xsd $PARAM
			cp $EXPORT_HOME/src/main/resources/IntEnzXML-README.txt $PARAM
			cd $PARAM
			zip -r IntEnzXML.zip *
			;;
    esac
    case $PARAM in
        '-intenzXml'|'-sitemap'|'-biopax'|'-keggEnzyme') LAST_OPT=$PARAM ;;
        *) unset LAST_OPT ;;
    esac
done
