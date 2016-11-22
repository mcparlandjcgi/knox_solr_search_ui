#!/bin/bash

# Directories
export INSTALLDIR=/usr/knox_solr_search_ui
JAR=knox-solr-ui.jar

# Allow port over-riding
if [[ 1 -eq ${#} ]];then
    PORT=${1}
    echo "Will run on port ${PORT}"
    JVM_ARGS="-Dserver.port=${PORT}"
fi

# Run it
java -DLOGDIR=${INSTALLDIR}/logs ${JVM_ARGS} -jar ${INSTALLDIR}/${JAR}
