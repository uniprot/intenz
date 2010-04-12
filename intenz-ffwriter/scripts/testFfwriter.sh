#!/bin/bash
# Parameters:
#   $1: EC number to be exported (just one) or 'all'

if [ -z $1 -o $1 != 'all' ]
then
    echo "Testing requires an EC number"
    exit 1
fi

SCRIPT_DIR=$(dirname $0)
$SCRIPT_DIR/runFfwriter.sh $SCRIPT_DIR/../src/test/appResources $@