#!/bin/bash
# Parameter:
# $DB_PROPS_FILE - properties file with configuration for DB connection

if [ -z $INTENZ_CONFIG_DIR ]
then
    echo "Set variable INTENZ_CONFIG_DIR to a directory containing database configuration"
    exit 1
fi

DB_PROPS_FILE=${1:-intenz-db-prod.properties}

REL_NO=`grep 'intenz.release.number' $INTENZ_CONFIG_DIR/intenz-release.properties | cut -d '=' -f 2`
REL_DATE=$(grep 'intenz.release.date=' $INTENZ_CONFIG_DIR/intenz-release.properties | cut -d '=' -f 2)

DB_MODULE_HOME=$(dirname $0)/..

STATS_DUMP_DIR=$DB_MODULE_HOME/src/site/resources/statistics

DB_USER=`grep '^user' $INTENZ_CONFIG_DIR/$DB_PROPS_FILE | cut -d '=' -f 2`
DB_PASSWD=`grep '^password' $INTENZ_CONFIG_DIR/$DB_PROPS_FILE | cut -d '=' -f 2`
DB_INSTANCE=`grep '^instance' $INTENZ_CONFIG_DIR/$DB_PROPS_FILE | cut -d '=' -f 2`

echo "Dumping statistics from ${DB_USER}@${DB_INSTANCE} for IntEnz release $REL_NO"
echo 'exit' | \
	sqlplus ${DB_USER}/${DB_PASSWD}@${DB_INSTANCE} \
	@$DB_MODULE_HOME/src/main/sql/util/intenzStats \
	$STATS_DUMP_DIR/intenzStats-rel$REL_NO.html

sed -i 's/\(<title>\)\(.*\)\(<\/title>\)/\1\2 - Rel. $REL_NO ($REL_DATE)\3/'\
    $STATS_DUMP_DIR/intenzStats-rel$REL_NO.html