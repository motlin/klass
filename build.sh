#!/bin/bash

set -euxo pipefail

# Remove artifacts we are about to build from local caches
rm -rf .m2/cool/klass/

./mvnw de.qaware.maven:go-offline-maven-plugin:1.2.8:resolve-dependencies --activate-profiles !shade,!run-demo,enforcer-strict -Dmaven.repo.local='.m2'
#./mvnw --threads 2C --offline --activate-profiles !shade,!run-demo,enforcer-strict -Dmaven.repo.local='.m2' $@
./mvnw --offline --activate-profiles !shade,!run-demo,enforcer-strict -DforkCount=0 -Dmaven.repo.local='.m2' $@
