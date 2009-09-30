#!/bin/bash
#
# Test the export of IntEnz database.
# Parameter:
#   $1 - [optional] EC number to be exported

EXPORT_HOME=`dirname $0`/..
CONFIG_DIR=$EXPORT_HOME/src/test/appResources

`dirname $0`/doExport.sh $CONFIG_DIR $@
