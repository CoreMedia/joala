#!/bin/bash

mvn com.google.code.sortpom:maven-sortpom-plugin:sort \
  -Dsort.sortOrderFile=recommended_2008_06.xml \
  -Dsort.createBackupFile=false \
  -Dsort.expandEmptyElements=false \
  -Dsort.sortDependencies=true \
  -Dsort.sortPlugins=true \
  -Dsort.keepBlankLines=true
