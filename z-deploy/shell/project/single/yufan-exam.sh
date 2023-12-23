#!/bin/bash
pid=`ps -ef | grep yufan-exam.jar | grep -v 'grep'| awk '{print $2}'| wc -l`
if [ "$1" = "start" ];then
        if [ $pid -gt 0 ];then
    echo 'yufan-exam is running.'
    else nohup java -Dspring.profiles.active=prod -Dproject.name=yufan-exam -Dfastjson.parser.safeMode=true -Xmx512m -Xms512m -XX:NewRatio=1 -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256M -Xss256k -XX:+AlwaysPreTouch -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly -XX:+ParallelRefProcEnabled -XX:+CMSParallelInitialMarkEnabled -XX:MaxTenuringThreshold=3 -XX:+UnlockDiagnosticVMOptions -XX:ParGCCardsPerStrideChunk=1024 -XX:+ExplicitGCInvokesConcurrent -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -jar /opt/service/yufan-exam.jar >boot.log 2>&1 &
        fi
elif [ "$1" = "stop" ];then exec ps -ef | grep  yufan-exam | grep -v grep | awk '{print $2}'| xargs kill -9
    echo ' yufan-exam is stoped.'
else
        echo "Please input like this:"./jenkins.sh start"or "./jenkins stop""
fi