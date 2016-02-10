#!/bin/bash

. `dirname $0`/setEnv.sh

[ ! -d 'logs' ] && mkdir logs

echo "${DB_USER}@${DB_INSTANCE} Updating table ID2EC..."
java -Xmx6G -cp $CP uk.ac.ebi.intenz.tools.release.ID2EC $TARGET_DB
