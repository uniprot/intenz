#!/bin/bash
# Parameter:
# $1 - properties file with configuration for DB connection
# $2 - IntEnz release number

[ $# -lt 2 ] && echo "Need two parameters" && exit 1

DB_MODULE_HOME=$(dirname $0)/..

STATS_DUMP_DIR=$DB_MODULE_HOME/src/site/resources/statistics

DB_USER=`grep '^user' $1 | cut -d '=' -f 2`
DB_PASSWD=`grep '^password' $1 | cut -d '=' -f 2`
DB_INSTANCE=`grep '^instance' $1 | cut -d '=' -f 2`

echo "Dumping statistics from ${DB_USER}@${DB_INSTANCE} for IntEnz release $2"
echo 'exit' | \
	sqlplus ${DB_USER}/${DB_PASSWD}@${DB_INSTANCE} \
	@$DB_MODULE_HOME/src/main/sql/util/intenzStats \
	$STATS_DUMP_DIR/intenzStats-rel$2.html
