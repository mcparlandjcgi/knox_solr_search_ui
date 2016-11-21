#!/bin/bash

# Old way
#mvn -DLOGDIR=target/logs spring-boot:run

if [[ 1 -eq ${#} ]];then
    PORT=${1}
    echo "Will run on port ${PORT}  
fi

java -jar knox-solr-ui.jar
