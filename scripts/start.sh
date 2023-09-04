#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

APP_LOG="$PROJECT_ROOT/application.log"
ERROR_LOG="$PROJECT_ROOT/error.log"
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# build 파일 복사
echo "$TIME_NOW > $JAR_FILE 파일 복사" >> $DEPLOY_LOG
cp $PROJECT_ROOT/build/libs/*.jar $JAR_FILE

# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $JAR_FILE > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG

#!/bin/bash
#
#ROOT_PATH="/home/ubuntu/spring-github-action"
#JAR="$ROOT_PATH/application.jar"
#
#APP_LOG="$ROOT_PATH/application.log"
#ERROR_LOG="$ROOT_PATH/error.log"
#START_LOG="$ROOT_PATH/start.log"
#
#NOW=$(date +%c)
#
#echo "[$NOW] $JAR 복사" >> $START_LOG
#cp $ROOT_PATH/build/libs/spring-github-action-1.0.0.jar $JAR
#
#echo "[$NOW] > $JAR 실행" >> $START_LOG
#nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &
#
#SERVICE_PID=$(pgrep -f $JAR)
#echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG
