#!/bin/bash
#
# Test the export of IntEnz database

EXPORT_HOME=`dirname $0`/..
CONFIG_DIR=$EXPORT_HOME/src/test/appResources

`dirname $0`/export.sh $CONFIG_DIR
