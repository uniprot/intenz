#!/bin/bash
#
# Generate statistics from the database.
# Parameters: (same as for IntEnzDbStatisticsApp.main)
# $1 - Db connection configuration file name.
# $2 - Output directory.

SCRIPT_DIR=`dirname $0`

cd $SCRIPT_DIR/..
mvn -P apps clean package

CP=$INTENZ_CONFIG_DIR:src/main/appResources:src/main/appResources/templates
for JAR in target/*.jar
do
  CP=$CP:$JAR
done

echo
date
echo 'Generating statistics...'

java -Xmx512M -cp $CP uk.ac.ebi.intenz.stats.db.IntEnzDbStatisticsApp $@ \
    > logs/intenz-statistics-$(date +%Y%m%d).log

echo
date
echo 'Statistics finished!'
