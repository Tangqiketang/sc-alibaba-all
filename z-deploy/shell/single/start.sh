#!/bin/sh

usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status]"
    exit 1
}

is_exist(){
  pid=`ps -ef|grep babycare.jar|grep -v grep|awk '{print $2}' `
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> babycare is already running PID=${pid} <<<"
  else
    nohup java -jar babycare.jar --spring.profiles.active=aliyun > /dev/null 2>&1 &
    echo ">>> start babycare successed PID=$! <<<"
   fi
  }

stop(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 2
    echo ">>> babycare process stopped <<<"
  else
    echo ">>> babycare is not running <<<"
  fi
}

status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> babycare is running PID is ${pid} <<<"
  else
    echo ">>> babycare is not running <<<"
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