#!/bin/bash

set -uo pipefail

VOICE='Serena (Premium)'
export LIFTWIZARD_FILE_MATCH_RULE_RERECORD=true

# mvnd is the maven daemon, and is much faster but doesn't work for builds that include maven plugins plus runs of those maven plugins
# mvnw is the regular maven wrapper.
export MAVEN='mvnd'
export MAVEN='./mvnw'

function echoSay {
    echo "$1"
    say --voice "$VOICE" "$1"
}

function failWithMessage {
    if [ "$1" -ne 0 ]; then
        say --voice "$VOICE" "$2 failed with exit code $1"
        exit 1
    fi
}

function checkLocalModification {
    git diff --quiet
    failWithMessage $? "Locally modified files"
}

COMMIT_MESSAGE=$(git log --format=%B -n 1 HEAD)

echoSay "[[volm 0.10]] Beginning build of commit: $COMMIT_MESSAGE" &

$MAVEN clean            --threads 2C
$MAVEN checkstyle:check --threads 2C
EXIT_CODE=$?

if [ $EXIT_CODE -ne 0 ]; then
    ./mvnw checkstyle:check
    echoSay "Checkstyle failed on commit: '$COMMIT_MESSAGE' with exit code: $EXIT_CODE"
    exit 1
fi

checkLocalModification
echoSay "[[volm 0.10]] Checkstyle succeeded on commit: '$COMMIT_MESSAGE'"
exit 0
