#!/bin/bash
#
# Parameters:
# $1 - Oracle username@instance
# $2 - [optional] Oracle dump file. May be gzipped. Defaults to Oracle's
#      default 'expdat.dmp')

echo
echo "**************************** W A R N I N G ****************************"
echo "THIS WILL OVERWRITE ALL DATA IN $1. ARE YOU SURE?"
echo "(Ctrl-C to cancel, Enter to continue)"
read ok

read -p "Password for ${1}:" -s PASSWORD

WD=$(pwd)
cd $(dirname $0)/../src/main/sql

echo
echo "*** Dropping everything in $1..."
echo sqlplus $1 @drop_all
echo exit | sqlplus ${1/@/\/$PASSWORD@} @drop_all

echo
echo "*** Loading data..."
if [ $2 ]
then
    # Use absolute path of dump file:
    if [ "$(echo $2 | grep -c '^/')" == 0 ]
    then
        DUMP_FILE=$WD/$2
    else
        DUMP_FILE=$2
    fi
    # Check if dump file is gzipped, unpack if needed:
    if [ "$(file $DUMP_FILE | grep -c 'gzip compressed data')" == 1 ]
    then
        echo "$2 - dump file is gzipped"
        # Unpack dump file
        TMP_DMP=$$.dmp
        gunzip -c $DUMP_FILE > $TMP_DMP
        DUMP_FILE=$TMP_DMP
    fi
    FILE_PARAM="FILE=$DUMP_FILE"
fi
echo "$ORACLE_HOME/bin/imp ${1} PARFILE=parfile.fullimport $FILE_PARAM"
echo exit | $ORACLE_HOME/bin/imp ${1/@/\/$PASSWORD@} \
    PARFILE=parfile.fullimport $FILE_PARAM
# Cleanup:
[ -n $TMP_DMP ] && [ -f $TMP_DMP ] && rm $TMP_DMP

echo
echo "*** Restoring permissions..."
GRANTS=grant_to_roles
case $1 in
*@iwebpro* | *@enzyme)
    GRANTS=grant_to_roles_public
    ;;
esac
echo sqlplus $1 @$GRANTS
echo exit | sqlplus ${1/@/\/$PASSWORD@} @$GRANTS

echo
echo "$2 loaded into $1. Done."
