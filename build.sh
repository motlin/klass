#!/bin/bash

set -uo pipefail

VOICE='Serena (Premium)'
MAVEN='mvnd'
COMMAND="Build"
INCREMENTAL=false
SERIAL=false
RECORD=false
LIFTWIZARD_FILE_MATCH_RULE_RERECORD=false

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
        --record)
            RECORD=true
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

if [ "$RECORD" = true ] || [[ $COMMIT_MESSAGE == *\[record\]* ]]; then
    LIFTWIZARD_FILE_MATCH_RULE_RERECORD=true
fi
export LIFTWIZARD_FILE_MATCH_RULE_RERECORD

if [[ $COMMIT_MESSAGE == *\[serial\]* ]]; then
    SERIAL=true
fi

if [ "$SERIAL" = true ]; then
    PARALLELISM="--fail-fast"
else
    PARALLELISM="--threads 2C --fail-at-end"
fi

echo "Beginning build of commit: $COMMIT_MESSAGE"

if [ "$INCREMENTAL" != true ]; then
    $MAVEN clean --threads 2C --quiet
fi

ALWAYS_RUN_PLUGINS='liftwizard-generator-reladomo-code:*,liftwizard-generator-reladomo-database:*,liftwizard-generator-xsd2bean:*,klass-compiler:*,klass-generator-dropwizard:*,klass-generator-dto:*,klass-generator-graphql-data-fetcher:*'
$MAVEN install $PARALLELISM -Dmaven.build.cache.alwaysRunPlugins=$ALWAYS_RUN_PLUGINS -Dcheckstyle.skip -Denforcer.skip -Dmaven.javadoc.skip -Dlicense.skip=true -Dmdep.analyze.skip=true --activate-profiles 'dev'
EXIT_CODE=$?

if [ $EXIT_CODE -ne 0 ]; then
    if [ "$SERIAL" != true ]; then
        ./mvnw install -Dmaven.build.cache.enabled=false -Dcheckstyle.skip -Denforcer.skip -Dmaven.javadoc.skip -Dlicense.skip=true -Dmdep.analyze.skip=true --activate-profiles 'dev'
    fi
    failWithMessage $EXIT_CODE "$MAVEN install"
    exit 1
else
    ./mvnw install dependency:analyze -DfailOnWarning=true
    EXIT_CODE=$?
    if [ $EXIT_CODE -ne 0 ]; then
        failWithMessage $EXIT_CODE "./mvnw dependency:analyze"
        exit 1
    fi
fi

checkLocalModification
echo "$COMMAND succeeded on commit: '$COMMIT_MESSAGE'"
exit 0
