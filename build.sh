#!/bin/bash

set -euxo pipefail

# Remove artifacts we are about to build from local caches
rm -rf .m2/cool/klass/
rm -rf ~/.m2/repository/cool/klass/

mvn -N io.takari:maven:wrapper -Dmaven=3.6.2
./mvnw de.qaware.maven:go-offline-maven-plugin:1.1.0:resolve-dependencies --activate-profiles !shade,!run-demo,enforcer-strict
# ./mvnw --threads 1C --offline --activate-profiles !shade,!run-demo,enforcer-strict
./mvnw --offline --activate-profiles !shade,!run-demo,enforcer-strict -DforkCount=0
