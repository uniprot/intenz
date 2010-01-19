#!/bin/bash

SCRIPTDIR=`dirname $0`
$SCRIPTDIR/runFfwriter.sh
$SCRIPTDIR/runComparator.sh

