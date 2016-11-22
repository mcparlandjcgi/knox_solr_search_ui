#!/bin/bash

# Directories
export INSTALLDIR=/usr/knox_solr_search_ui
JAR=knox-solr-ui.jar

# Allow port over-riding
if [[ 1 -eq ${#} ]];then
    PORT=${1}
    echo "Will run on port ${PORT}"
fi

# Run it
java -DLOGDIR=${INSTALLDIR}/logs -jar ${INSTALLDIR}/${JAR}

