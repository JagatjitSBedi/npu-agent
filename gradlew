#!/usr/bin/env sh

DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
JAVA_EXEC=java

exec "$JAVA_EXEC" -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
