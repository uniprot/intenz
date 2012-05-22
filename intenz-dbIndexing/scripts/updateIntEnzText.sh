#!/bin/bash

. $(dirname $0)/setEnv.sh

[ ! -d 'logs' ] && mkdir logs

echo "${DB_USER}@${DB_INSTANCE} Updating table INTENZ_TEXT..."
java -cp $CP uk.ac.ebi.intenz.tools.release.IntEnzText $TARGET_DB
echo 'exit' | sqlplus ${DB_USER}/${DB_PASSWD}@${DB_INSTANCE} \
	@$DBINDEXING_HOME/src/main/sql/intenz_text
