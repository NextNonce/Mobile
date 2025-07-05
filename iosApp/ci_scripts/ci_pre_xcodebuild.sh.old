#!/usr/bin/env bash
set -eu

# 1) If macOS can’t find Java 17, install it via brew (and symlink so java_home sees it)
if ! /usr/libexec/java_home -v 17 &>/dev/null; then
  echo "☁️ [Xcode Cloud] Java 17 not found, installing via Homebrew…"
  brew update
  brew install openjdk@17

  # for java_home to see it, symlink into /Library/Java/JavaVirtualMachines:
  sudo ln -sf /usr/local/opt/openjdk@17/libexec/openjdk.jdk \
             /Library/Java/JavaVirtualMachines/openjdk-17.jdk
fi

# 2) Point JAVA_HOME at Java 17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH="$JAVA_HOME/bin:$PATH"

echo "✅ [Xcode Cloud] JAVA_HOME is now set to $JAVA_HOME"