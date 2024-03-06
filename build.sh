#!/bin/bash

set -euxo pipefail

# Remove artifacts we are about to build from local caches
rm -rf .m2/io/liftwizard/
rm -rf ~/.m2/repository/io/liftwizard/
rm -rf .m2/cool/klass/
rm -rf ~/.m2/repository/cool/klass/

./mvnw de.qaware.maven:go-offline-maven-plugin:1.1.0:resolve-dependencies --activate-profiles !shade,!run-demo,enforcer-strict
./mvnw --threads 2C --offline --activate-profiles !shade,!run-demo,enforcer-strict $@
#./mvnw --offline --activate-profiles !shade,!run-demo,enforcer-strict -DforkCount=0 $@
