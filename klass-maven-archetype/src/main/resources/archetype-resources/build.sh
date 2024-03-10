#!/bin/bash

set -uo pipefail

MAVEN='mvnd'
COMMAND="Build"
INCREMENTAL=false
SERIAL=false
ANALYZE_SKIP=false
RECORD=false

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
        *)
            printUnknownFlagAndExit "$1"
            ;;
    esac
    shift
done

failWithMessage() {
    EXIT_CODE=$1
    REASON=$2

    if [ "$EXIT_CODE" -ne 0 ]; then
        MESSAGE="$COMMAND failed on commit: '$COMMIT_MESSAGE' due to: '$REASON' with exit code $EXIT_CODE"
        echo "$MESSAGE"

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

PROFILES="dev"
if [ "$RECORD" = true ] || [[ $COMMIT_MESSAGE == *\[record\]* ]]; then
  PROFILES="$PROFILES,rerecord"
fi

if [[ $COMMIT_MESSAGE == *\[serial\]* ]]; then
    SERIAL=true
fi

if [ "$SERIAL" = true ]; then
    PARALLELISM="--fail-fast"
else
    PARALLELISM="--threads 2C --fail-at-end"
fi

checkLocalModification
rm -rf ~/.m2/repository/cool/klass

echo "Beginning build of commit: $COMMIT_MESSAGE"

if [ "$INCREMENTAL" != true ]; then
    $MAVEN clean $PARALLELISM --quiet
fi

$MAVEN verify $PARALLELISM -Dcheckstyle.skip -Denforcer.skip -Dmaven.javadoc.skip -Dlicense.skip=true -Dmdep.analyze.skip="$ANALYZE_SKIP" --activate-profiles $PROFILES
EXIT_CODE=$?

if [ $EXIT_CODE -ne 0 ]; then
    failWithMessage $EXIT_CODE "$MAVEN verify"
    exit 1
fi

checkLocalModification
echo "$COMMAND succeeded on commit: '$COMMIT_MESSAGE'"
exit 0
