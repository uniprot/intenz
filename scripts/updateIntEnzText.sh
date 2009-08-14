#!/bin/bash

. `dirname $0`/setEnv.sh

echo "${DB_USER}@${TARGET_DB} Updating table INTENZ_TEXT..."
java -cp $CP uk.ac.ebi.intenz.tools.release.IntEnzText $TARGET_DB
echo 'exit' | sqlplus ${DB_USER}/${DB_PASSWD}@${TARGET_DB} @$DBINDEXING_HOME/src/main/sql/intenz_text
