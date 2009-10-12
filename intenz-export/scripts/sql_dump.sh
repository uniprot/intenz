#!/bin/bash

USAGE=$(cat <<'USAGE'
Parameters:
$1: output directory for the generated SQL files.
$2: oracle database login.
USAGE)

if [ $# < 2 ]
then
    echo $USAGE
    exit 1
fi

if [ ! -d $1 ]
then
    mkdir $1
    [ $? != 0 ] && echo "Unable to create directory $1" && exit 2
fi 

THIS_DIR=`dirname $0`
SQL_DIR=$THIS_DIR/../src/main/sql
OUTPUT_DIR=$1
DB_LOGIN=$2

cp $SQL_DIR/setup_mysql_intenz*.sql $THIS_DIR/../src/main/resources/IntEnz4MySQL-README.txt $OUTPUT_DIR

echo "***** SQL dump started at $(date)"
echo exit | sqlplus $DB_LOGIN @$SQL_DIR/sql_dump $OUTPUT_DIR &>/dev/null
cd $OUTPUT_DIR
tar cvfz IntEnz4MySQL.tgz *.sql *.txt
echo "***** SQL dump finished at $(date)"
