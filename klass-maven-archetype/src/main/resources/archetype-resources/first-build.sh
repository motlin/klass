#!/bin/bash

set -euxo pipefail

chmod +x build.sh
chmod +x first-build.sh

git init
git add -A
git commit -m "Initial import."

git rm -f stackoverflow-reladomo-pojos/src/main/java/com/stackoverflow/DeleteMe*
git commit -m "Remove DeleteMe*."

./build.sh --no-analyze --record
