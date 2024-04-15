#!/bin/bash

#
# Copyright 2024 Craig Motlin
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -uo pipefail

VOICE="${VOICE:-Serena (Premium)}"
RECORD="${RECORD:-false}"
PUSHOVER="${PUSHOVER:-false}"
SILENT="${SILENT:-false}"

MAVEN='mvnd'
COMMAND="Build"
INCREMENTAL=false
SERIAL=false
ANALYZE_SKIP=false
LIFTWIZARD_RERECORD=false

# Function to print unknown flags and exit
printUnknownFlagAndExit() {
    echo "Unknown flag: $1"
    exit 1
}

# Process command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --no-daemon)
            MAVEN='./mvnw'
            ;;
        --incremental|-i)
            INCREMENTAL=true
            ;;
        --serial|-s)
            SERIAL=true
            ;;
        --record|-r)
            RECORD=true
            ;;
        --no-analyze)
            ANALYZE_SKIP=true
            ;;
        --pushover|-p)
            PUSHOVER=true
            ;;
        --silent|--quiet|-q)
            SILENT=true
            ;;
        *)
            printUnknownFlagAndExit "$1"
            ;;
    esac
    shift
done

# Function to check if a flag is set in either environment variable or command line
checkFlag() {
    key="$1"
    value="${!key:-false}"
    if [ "$value" = true ] || [ "${!key}" = true ]; then
        echo true
    else
        echo false
    fi
}

# Check flags from environment variables
RECORD=$(checkFlag "RECORD")
PUSHOVER=$(checkFlag "PUSHOVER")
SILENT=$(checkFlag "SILENT")
ANALYZE_SKIP=$(checkFlag "ANALYZE_SKIP")

echoSay() {
    echo "$1"
    if [ "$SILENT" = false ]; then
        say --voice "$VOICE" "$1" &
    fi
}

failWithMessage() {
    EXIT_CODE=$1
    REASON=$2

    if [ "$EXIT_CODE" -ne 0 ]; then
        MESSAGE="$COMMAND failed on commit: '$COMMIT_MESSAGE' due to: '$REASON' with exit code $EXIT_CODE"
        echoSay "$MESSAGE"

        # osascript -e "display notification \"$MESSAGE\" with title \"$COMMAND failed\""

        if [ "$PUSHOVER" = true ]; then
            curl -s \
                --form-string "token=$PUSHOVER_TOKEN" \
                --form-string "user=$PUSHOVER_USER" \
                --form-string "message=$MESSAGE" \
                --form-string "title=$COMMAND" \
                https://api.pushover.net/1/messages.json
        fi

        exit 1
    fi
}

checkLocalModification() {
    git diff --quiet
    failWithMessage $? "Locally modified files"

	git status --porcelain | (! grep -q '^??')
    failWithMessage $? "Untracked files"
}

COMMIT_MESSAGE=$(git log --format=%B -n 1 HEAD)

if [[ $COMMIT_MESSAGE == *\[skip\]* || $COMMIT_MESSAGE == *\[pass\]* ]]; then
    echo "Skipping $COMMAND due to [skip] or [pass] in commit: '$COMMIT_MESSAGE'"
    exit 0
fi

if [[ $COMMIT_MESSAGE == *\[stop\]* || $COMMIT_MESSAGE == *\[fail\]* ]]; then
    echo "Stopping $COMMAND due to [stop] or [fail] in commit: '$COMMIT_MESSAGE'"
    exit 1
fi

if [[ $COMMIT_MESSAGE == *\[no-daemon\]* ]]; then
    MAVEN='./mvnw'
fi

PROFILES='maven-enforcer-plugin,maven-dependency-plugin'
if [ "$RECORD" = true ] || [[ $COMMIT_MESSAGE == *\[record\]* ]]; then
    LIFTWIZARD_RERECORD=true
    PROFILES="$PROFILES,rerecord"
fi
export LIFTWIZARD_FILE_MATCH_RULE_RERECORD=${LIFTWIZARD_RERECORD}

if [[ $COMMIT_MESSAGE == *\[serial\]* ]]; then
    SERIAL=true
fi

if [ "$SERIAL" = true ]; then
    PARALLELISM="--threads=1 --fail-fast"
else
    PARALLELISM="--fail-at-end"
fi

checkLocalModification
rm -rf ~/.m2/repository/cool/klass

echo "Beginning build of commit: $COMMIT_MESSAGE"

if [ "$INCREMENTAL" != true ]; then
    $MAVEN clean $PARALLELISM --quiet
fi

mvnd --stop

git clean -f -x '*pom.xml.releaseBackup' '*/spy.log' '*/dependency-reduced-pom.xml' release.properties

$MAVEN verify $PARALLELISM -Dcheckstyle.skip -Denforcer.skip -Dmaven.javadoc.skip -Dlicense.skip=true -Dmdep.analyze.skip="$ANALYZE_SKIP" --activate-profiles $PROFILES
EXIT_CODE=$?

if [ $EXIT_CODE -ne 0 ]; then
    failWithMessage $EXIT_CODE "$MAVEN verify"
    exit 1
fi

checkLocalModification
echo "$COMMAND succeeded on commit: '$COMMIT_MESSAGE'"
exit 0
