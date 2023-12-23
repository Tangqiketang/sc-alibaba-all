#!/bin/bash

find /opt/logs -type f -ctime +7 | xargs rm -f
find /opt/logs/ -type f -cmin +60 | xargs rm -f
echo > /tmp/spring.log.1
echo > /tmp/spring.log.2
echo > /tmp/spring.log.3
echo > /tmp/spring.log.4
echo > /tmp/spring.log.5
echo > /var/log/messages
rm -f /var/log/messages-*
rm -f /tmp/*.jar
rm -f /tmp/spring*.gz
rm -f /tmp/uniubi-uspace*
rm -f /var/log/syslog.*.gz
systemctl restart rsyslog
echo ''>/usr/local/zookeeper-3.4.14/zookeeper.out
echo ''>/usr/local/zookeeper-3.4.14/bin/zookeeper.out
FILES=`ls /tmp/spring.log.*.tmp`
for FILE in $FILES;do
    echo  > $FILE
    done
echo > /var/log/syslog.1
echo > /var/log/syslog
echo "`date +%Y%m%d%H%M%S`" >> /tmp/clean-log.log