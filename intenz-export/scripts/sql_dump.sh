#!/bin/bash
# Parameters:
# $1: output directory for the generated SQL files.
# $2: oracle database login.

if [ ! $1 ]
then
    echo "Output directory needed"
    exit 1
fi

[ ! -d $1 ] && mkdir $1 || exit 2

THIS_DIR=`dirname $0`
SQL_DIR=$THIS_DIR/../src/main/sql
OUTPUT_DIR=$1
DB_LOGIN=$2

echo "***** SQL dump started at $(date)"
echo exit | sqlplus $DB_LOGIN @$SQL_DIR/sql_dump $OUTPUT_DIR &>/dev/null
cd $OUTPUT_DIR
cp $SQL_DIR/setup_mysql_intenz*.sql $THIS_DIR/../src/main/resources/IntEnz4MySQL-README.txt .
tar cvfz IntEnz4MySQL.tgz *.sql *.txt
echo "***** SQL dump finished at $(date)"
