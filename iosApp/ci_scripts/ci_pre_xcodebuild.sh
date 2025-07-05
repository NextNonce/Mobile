#!/usr/bin/env bash
set -euo pipefail

# 1) Ensure OpenJDK 17 is installed
if [[ ! -d "$(brew --prefix openjdk@17)/libexec/openjdk.jdk" ]]; then
  echo "☁️ [Xcode Cloud] Installing openjdk@17 via Homebrew…"
  brew update
  brew install openjdk@17
fi

# 2) Point JAVA_HOME at it (no sudo, no /usr/libexec/java_home)
export JAVA_HOME="$(brew --prefix openjdk@17)/libexec/openjdk.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

echo "✅ [Xcode Cloud] JAVA_HOME is now $JAVA_HOME"