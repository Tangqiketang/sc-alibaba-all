#!/bin/sh
#可以用命名空间指定环境,也可以用

#项目名称
PROJECT_NAME="service-feign"
#项目jar包
PROJECT_JAR=$PROJECT_NAME".jar"
#nacos上运行命名空间
PROJECT_NAMESPACE="f739f38b-f593-432b-9458-5912ed89dafd"
#nacos注册中心地址
NACOS_IP="192.168.0.100:8848"
#项目使用哪个地址注册到命令空间
NACOS_DISCOVERY_IP="192.168.40.131"

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
stop(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> PID = $pid begin kill -9 $pid  <<<"
    kill -9  $pid
    sleep 10
    echo ">>> $PROJECT_NAME process stopped <<<"
  else
    echo ">>> $PROJECT_NAME is not running <<<"
  fi
}

start(){
is_exist
if [ $? -eq "0" ]; then
echo ">>>  $PROJECT_NAME  is already running PID=${pid} <<<"
stop
else
nohup java -Xms512m -Xmx2048m  -jar $PROJECT_JAR  -Dfastjson.parser.safeMode=true  --spring.cloud.nacos.discovery.ip=$NACOS_DISCOVERY_IP --spring.cloud.nacos.config.server-addr=$NACOS_IP --spring.cloud.nacos.config.namespace=$PROJECT_NAMESPACE > /dev/null 2>&1 &
echo ">>> start  $PROJECT_NAME successed PID=$! <<<"
fi
}

restart(){
stop
start
echo ">>>  restart success <<<"
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

