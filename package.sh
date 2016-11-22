#!/bin/bash

PACKAGE_NAME=knox_solr_search_ui
PACKAGE_DIR=package_contents

if [[ -f ${PACKAGE_NAME}.tar.gz ]];then
    rm ${PACKAGE_NAME}.tar.gz
fi

if [[ -d ${PACKAGE_DIR} ]];then
    rm -fr ${PACKAGE_DIR}
fi

mkdir ${PACKAGE_DIR}
cp install.sh run.sh override.properties target/knox-solr-ui.jar ${PACKAGE_DIR}/

cd ${PACKAGE_DIR}
tar cvzf ${PACKAGE_NAME}.tar.gz *
mv ${PACKAGE_NAME}.tar.gz ..
cd ..

rm -fr ${PACKAGE_DIR}

