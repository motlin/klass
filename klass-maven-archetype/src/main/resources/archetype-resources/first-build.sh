#!/bin/bash

set -euxo pipefail

chmod +x build.sh
chmod +x first-build.sh

git init
git add -A
git commit -m "Initial import."

./mvnw wrapper:wrapper -Dmaven=4.0.0-alpha-7
git add -A
git commit -m "Add maven wrapper."

git rm -f ${rootArtifactId}-reladomo-pojos/src/main/java/com/${rootArtifactId}/DeleteMe*
git commit -m "Remove DeleteMe*."

export LIFTWIZARD_FILE_MATCH_RULE_RERECORD=true
./build.sh --no-analyze --record

git add -A
git commit -m "Initial code generation."
