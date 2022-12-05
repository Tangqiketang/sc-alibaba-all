#!/bin/bash
MY_PATH=$(cd `dirname $0`; pwd)
cd $MY_PATH

_start_time=`date +%F_%T`
ip="192.168.1.30"　　#源服务器内网ip
pub_ip="xxxx"　　#源服务器外网ip
port=3306

time mysql -h${ip} -P${port} -uroot -pxxxx -e \'show databases;\' | grep -Ev "Database|information_schema|mysql|performance_schema" | grep -v "^test$" | xargs mysqldump -uroot -pxxxx -h${ip} -P${port} --single-transaction --master-data=2 --flush-logs -BRE --set-gtid-purged=OFF --events > ./${pub_ip}_mysql.sql
#time mysqldump -uroot -pxxxx -h${ip} -P${port} --single-transaction --master-data=2 --flush-logs -BRE --set-gtid-purged=OFF --events zombie108 > ./zombie108_mysql.sql

if [ $? == 0 ];thentime mysql -h127.0.0.1 -P3306 -uroot -pxxx < ./${pub_ip}_mysql.sql  mysql-000166.sql
fi

_end_time=`date +%F_%T`
echo "start_time:${_start_time}"
echo "end_time:${_end_time}"


--single-transaction：获取到innodb的一致性快照备份，不锁表　　
--flush-logs：刷新binlog日志　　
-B:导出多个数据库　　
-R:导出存储过程以及自定义函数　　
-E:导出事件　　
--set-gtid-purged=OFF：关闭gtid复制