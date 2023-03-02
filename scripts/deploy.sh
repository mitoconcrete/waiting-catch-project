#!/usr/bin/env bash

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=waiting-catch

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 pid 확인" > /var/log/deploy.log

CURRENT_PID=$(pgrep -f $PROJECT_NAME.*.jar)

echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID" >> /var/log/deploy.log

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다." >> /var/log/deploy.log
else
  echo "kill -15 $CURRENT_PID" >> /var/log/deploy.log
  kill -15 $CURRENT_PID
  sleep 3000
fi

echo "> 새 애플리케이션 배포" >> /var/log/deploy.log

JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "JAR name: $JAR_NAME" >> /var/log/deploy.log

nohup java -jar -Dspring.config.location=/home/ec2-user/app/application.properties -Dspring.profiles.active=prod $REPOSITORY/$JAR_NAME 2>&1 &