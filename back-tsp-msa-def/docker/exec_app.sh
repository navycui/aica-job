#!/bin/sh
#JAVA_TOOL_OPTIONS=" -javaagent:/whatap/whatap.agent-2.2.0.jar -Dwhatap.micro.enabled=true -Dspring.profiles.active=${ACTIVE_PROFILES}"
#JAVA_TOOL_OPTIONS="${JAVA_TOOL_OPTIONS} -Xbootclasspath/p:/deploy/wt7/lib/wsthx.jar -javaagent:/deploy/wt7/lib/wsthxt.jar -Dwclh.file=/deploy/wt7/config/ai-tsp/settings.ini -DW_INAME=ai-tsp" 
JAVA_TOOL_OPTIONS=" -javaagent:/deploy/wt7/lib/wsthxt.jar -Xbootclasspath/p:/deploy/wt7/lib/wsthx.jar -Dwclh.file=/deploy/wt7/config/ai-tsp/settings.ini -DW_INAME=ai-tsp" 
#export JAVA_TOOL_OPTIONS

java ${JAVA_TOOL_OPTIONS}  org.springframework.boot.loader.JarLauncher
