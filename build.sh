#!/bin/bash

set -euxo pipefail

# Remove artifacts we are about to build from local caches
rm -rf .m2/cool/klass/
rm -rf ~/.m2/repository/cool/klass/

mvn -N io.takari:maven:wrapper -Dmaven=3.6.1
./mvnw de.qaware.maven:go-offline-maven-plugin:1.1.0:resolve-dependencies --activate-profiles !shade,!run-demo --no-transfer-progress
# ./mvnw -T1C clean install --activate-profiles !shade,!run-demo
./mvnw --offline --no-transfer-progress --activate-profiles !shade,!run-demo -DforkCount=0
