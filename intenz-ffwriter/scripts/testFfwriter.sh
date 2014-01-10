#!/bin/bash
# Parameters:
#   $1: (optional) EC number to be exported (just one). If none provided, all
#       EC classification is exported.

if [ -z $1 ]
then
    echo "Testing requires an EC number"
    exit 1
fi

SCRIPT_DIR=$(dirname $0)
$SCRIPT_DIR/runFfwriter.sh $SCRIPT_DIR/../src/test/appResources $@
