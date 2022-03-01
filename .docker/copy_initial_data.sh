#!/bin/bash

mkdir /tmp/pattern-atlas-content
git clone --single-branch --branch ${PATTERN_ATLAS_CONTENT_REPOSITORY_BRANCH} ${PATTERN_ATLAS_CONTENT_REPOSITORY_URL} /tmp/pattern-atlas-content

cp /tmp/pattern-atlas-content/liquibase_changelog/*.xml /var/www/java/

if [ "$PATTERN_ATLAS_FETCH_INITIAL_DATA" = "true" ]
then
	rm /var/www/java/patternatlas.xml
	mv /var/www/java/patternatlas_full.xml /var/www/java/patternatlas.xml
fi
