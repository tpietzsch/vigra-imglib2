#!/bin/sh

AVIAN="$(ls -t ../avian/build/*/avian | head -n 1)"
ARTIFACT=vigra-imglib2-1.0.0-SNAPSHOT.jar
CLASSPATH="$(ls target/dependency/*.jar | tr '\n' :)target/$ARTIFACT"
"$@" $AVIAN -Dpath.separator=: -Dfile.separator=/ -classpath "$CLASSPATH" -Dscijava.log.level=debug Example
