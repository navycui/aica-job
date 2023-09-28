#!/bin/sh
#JAVA_TOOL_OPTIONS=" -javaagent:/whatap/whatap.agent-2.2.0.jar -Dwhatap.micro.enabled=true -Dspring.profiles.active=${ACTIVE_PROFILES}"
JAVA_TOOL_OPTIONS=" -javaagent:/deploy/wt7/lib/wsthxt.jar -Xbootclasspath/p:/deploy/wt7/lib/wsthx.jar -Dwclh.file=/deploy/wt7/config/ai-batch/settings.ini -DW_INAME=ai-batch" 

#export JAVA_TOOL_OPTIONS
  
java ${JAVA_TOOL_OPTIONS} org.springframework.boot.loader.JarLauncher
