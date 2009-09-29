#!/bin/bash

SCRIPTDIR=`dirname $0`
ENZYME_DOT_DAT=ftp://ftp.expasy.org/databases/enzyme/enzyme.dat
LOCAL_COPY=$SCRIPTDIR/../target/enzyme.dat
CP=.
for JAR in $CRIPTDIR/../target/*.jar
do
    CP=$CP:$JAR
done

wget -O $LOCAL_COPY $ENZYME_DOT_DAT
java -cp $CP uk.ac.ebi.intenz.tools.sib.EnzymeFlatFileComparatorApp

