#!/bin/bash
# Parameters:
#   $1: EC number to be exported (just one)

[ $1 ] || (echo "Testing requires an EC number" && exit 1)

SCRIPT_DIR=$(dirname $0)
$SCRIPT_DIR/runFfwriter.sh $SCRIPT_DIR/../src/test/appResources $@