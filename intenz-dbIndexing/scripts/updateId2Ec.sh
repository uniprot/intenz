#!/bin/bash

. `dirname $0`/setEnv.sh

echo "${DB_USER}@${DB_INSTANCE} Updating table ID2EC..."
java -cp $CP uk.ac.ebi.intenz.tools.release.ID2EC $TARGET_DB
