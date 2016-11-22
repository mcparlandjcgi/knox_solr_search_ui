#!/bin/bash

export INSTALLDIR=/usr/knox_solr_search_ui
if [[ ! -d ${INSTALLDIR} && ! -w /usr ]];then
   echo "ERROR: ${INSTALLDIR} doesn't exist, and you can't create it - try sudo mkdir -p ${INSTALLDIR}?"
   exit 0
fi

if [[ ! -d ${INSTALLDIR} && -w /usr ]];then
    mkdir -p ${INSTALLDIR}/logs
fi

JAR=knox-solr-ui.jar
find . -name ${JAR} -exec cp {} ${INSTALLDIR} \;

cp run.sh ${INSTALLDIR}
cp override.properties ${INSTALLDIR}

