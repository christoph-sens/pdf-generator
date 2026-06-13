#!/bin/sh

APP_HOME="$(cd "$(dirname "$0")" && pwd)"
GRADLE_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$GRADLE_JAR" ]; then
    echo >&2 "Error: gradle-wrapper.jar not found at $GRADLE_JAR"
    exit 1
fi

exec java -cp "$GRADLE_JAR" org.gradle.wrapper.GradleWrapperMain "$@"



