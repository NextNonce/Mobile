#!/usr/bin/env bash
set -eux

# 1) Install Java 17 if not already present
if ! /usr/libexec/java_home -v 17 >/dev/null 2>&1; then
  echo "☁️ [Xcode Cloud] Java 17 not found, installing via Homebrew…"
  brew update
  brew install openjdk@17
fi

# 2) Point JAVA_HOME at it
export JAVA_HOME="$(/usr/libexec/java_home -v 17)"
export PATH="$JAVA_HOME/bin:$PATH"

echo "✅ [Xcode Cloud] JAVA_HOME is now set to $JAVA_HOME
