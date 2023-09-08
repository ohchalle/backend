PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE="$PROJECT_ROOT/spring-webapp.jar"

DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)

# 현재 구동 중인 애플리케이션 pid 확인
CURRENT_PID=$(pgrep -f $JAR_FILE)

# 프로세스가 켜져 있으면 종료
if [ -z "$CURRENT_PID" ]; then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다" >> "$DEPLOY_LOG"
else
  echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료 " >> "$DEPLOY_LOG"
  sudo kill -9 "$CURRENT_PID"
fi


##!/bin/bash
#
#ROOT_PATH="/home/ubuntu/spring-github-action"
#JAR="$ROOT_PATH/application.jar"
#STOP_LOG="$ROOT_PATH/stop.log"
#SERVICE_PID=$(pgrep -f $JAR) # 실행중인 Spring 서버의 PID
#
#if [ -z "$SERVICE_PID" ]; then
#  echo "서비스 NouFound" >> $STOP_LOG
#else
#  echo "서비스 종료 " >> $STOP_LOG
#  kill "$SERVICE_PID"
#  # kill -9 $SERVICE_PID # 강제 종료를 하고 싶다면 이 명령어 사용
#fi
