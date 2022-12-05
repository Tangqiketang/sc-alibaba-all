#!/bin/sh

#项目名称
PROJECT_NAME="data-manager"
#项目jar包
PROJECT_JAR=$PROJECT_NAME".jar"
ACTIVE_ENV="aliyun"


usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status]"
    exit 1
}

is_exist(){
  pid=`ps -ef|grep $PROJECT_JAR|grep -v grep|awk '{print $2}' `
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> $PROJECT_NAME is already running PID=${pid} <<<"
  else
    nohup java -jar $PROJECT_JAR --spring.profiles.active=$ACTIVE_ENV > /dev/null 2>&1 &
    echo ">>> start $PROJECT_NAME successed PID=$! <<<"
   fi
  }

stop(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2
    echo ">>> $PROJECT_NAME process stopped <<<"
  else
    echo ">>> $PROJECT_NAME is not running <<<"
  fi
}

status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> $PROJECT_NAME is running PID is ${pid} <<<"
  else
    echo ">>> $PROJECT_NAME is not running <<<"
  fi
}

restart(){
  stop
  start
}

case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0