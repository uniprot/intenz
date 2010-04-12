#!/bin/bash
#
# Parameters:
# $1 - Oracle credentials (username/password)
# $2 - Oracle instance
# $3 - [optional] Oracle dump file

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL OVERWRITE ALL DATA IN $2. ARE YOU SURE?"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

WD=$(pwd)
cd $(dirname $0)/../src/main/sql

echo
echo "*** Dropping everything in $2..."
echo sqlplus $1@$2 @drop_all
echo exit | sqlplus $1@$2 @drop_all

echo
echo "*** Loading data..."
if [ $3 ]
then
    # Use absolute path of dump file:
    if [ "$(echo $3 | grep -c '^/')" == 0 ]
    then
        DUMP_FILE=$WD/$3
    else
        DUMP_FILE=$3
    fi
    # Check if dump file is gzipped, unpack if needed:
    if [ "$(file $DUMP_FILE | grep -c 'gzip compressed data')" == 1 ]
    then
        echo "$3 - dump file is gzipped"
        # Unpack dump file
        TMP_DMP=$$.dmp
        gunzip -c $DUMP_FILE > $TMP_DMP
        DUMP_FILE=$TMP_DMP
    fi
    FILE_PARAM="FILE=$DUMP_FILE"
fi
echo "$ORACLE_HOME/bin/imp $1@$2 PARFILE=parfile.fullimport $FILE_PARAM"
echo exit | $ORACLE_HOME/bin/imp $1@$2 PARFILE=parfile.fullimport $FILE_PARAM
# Cleanup:
[ -n $TMP_DMP ] && [ -f $TMP_DMP ] && rm $TMP_DMP

echo
echo "*** Restoring permissions..."
GRANTS=grant_to_roles
case $2 in
iweb*)
    GRANTS=${GRANTS}_public
    ;;
esac
echo sqlplus $1@$2 @$GRANTS
echo exit | sqlplus $1@$2 @$GRANTS

echo
echo "$3 loaded into $2. Done."
