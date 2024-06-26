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

VOICE='Serena (Premium)'
MAVEN='mvnd'
COMMAND="Checkstyle"
INCREMENTAL=false
SERIAL=false

while [[ $# -gt 0 ]]; do
    key="$1"

    case $key in
        --no-daemon)
            MAVEN='./mvnw'
            shift
            ;;
        --incremental)
            INCREMENTAL=true
            shift
            ;;
        --serial)
            SERIAL=true
            shift
            ;;
        *)
            # unknown option
            shift
            ;;
    esac
done

function echoSay {
    echo "$1"
    say --voice "$VOICE" "$1" &
}

function failWithMessage {
    EXIT_CODE=$1
    REASON=$2

    if [ "$EXIT_CODE" -ne 0 ]; then
        MESSAGE="$COMMAND failed on commit: '$COMMIT_MESSAGE' due to: '$REASON' with exit code: '$EXIT_CODE'"

        echoSay "$MESSAGE"
        # osascript -e "display notification \"$MESSAGE\" with title \"$COMMAND failed\""
        curl -s \
            --form-string "token=$PUSHOVER_TOKEN" \
            --form-string "user=$PUSHOVER_USER" \
            --form-string "message=$MESSAGE" \
            --form-string "title=$COMMAND" \
            https://api.pushover.net/1/messages.json
        exit 1
    fi
}

function checkLocalModification {
    git diff --quiet
    failWithMessage $? "Locally modified files"
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

if [[ $COMMIT_MESSAGE == *\[serial\]* ]]; then
    SERIAL=true
fi

if [ "$SERIAL" = true ]; then
    PARALLELISM="--threads=1 --fail-fast"
else
    PARALLELISM="--fail-at-end"
fi

echo "Beginning build of commit: $COMMIT_MESSAGE"

if [ "$INCREMENTAL" != true ]; then
    $MAVEN clean --quiet
fi

$MAVEN checkstyle:check $PARALLELISM
EXIT_CODE=$?

if [ $EXIT_CODE -ne 0 ]; then
    if [ "$SERIAL" != true ]; then
        ./mvnw checkstyle:check
    fi
    failWithMessage $EXIT_CODE "$MAVEN checkstyle:check"
    exit 1
fi

checkLocalModification
echo "$COMMAND succeeded on commit: '$COMMIT_MESSAGE'"
exit 0
