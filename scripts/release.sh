#!/bin/bash

cd `dirname $0`/..
cvs update
mvn clean package
[ $? != 0 ] && echo "MAVEN BUILD FAILURE" && exit 1

. scripts/updateIntEnzText.sh
[ $? != 0 ] && echo "FAILURE UPDATING intenz_text TABLE!" && exit 1

. scripts/updateId2Ec.sh
[ $? != 0 ] && echo "FAILURE UPDATING id2ec TABLE!" && exit 1
