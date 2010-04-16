#!/bin/bash
#
# Import external cross-references
# Parameters:
#   $1 - xrefs to be imported, one of GO or UNIPROT (default: all)

SCRIPT_DIR=$(dirname $0)
CONFIG_DIR=$SCRIPT_DIR/../src/test/appResources

$SCRIPT_DIR/doImport.sh $CONFIG_DIR $@
