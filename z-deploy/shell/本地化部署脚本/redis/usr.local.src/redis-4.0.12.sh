#!/bin/bash
apt -y install gcc make
useradd redis -M -s /sbin/nologin
cd /usr/local/src
tar -zxvf redis-4.0.12.tar.gz
cd redis-4.0.12
make PREFIX=/usr/local/redis4.0 install
ln -s /usr/local/redis4.0 /usr/local/redis
mkdir /usr/local/redis/6379
chown -R redis.redis /usr/local/redis/
cd /usr/local/redis/6379
#添加环境变量
echo -e '##### redis \nexport PATH=/usr/local/redis/bin:$PATH' >>/etc/profile
#编辑配置文件
cp /usr/local/src/redis-4.0.12/redis.conf /usr/local/redis/6379/redis_6379.conf
chown redis.redis /usr/local/redis/6379/redis_6379.conf
sed -i "s/daemonize no/daemonize yes/g" /usr/local/redis/6379/redis_6379.conf 
sed -i "s#pidfile /var/run/redis_6379.pid#pidfile /usr/local/redis/6379/redis_6379.pid#g" /usr/local/redis/6379/redis_6379.conf 
sed -i "s#logfile ""#logfile "/usr/local/redis/6379/redis_6379.log"#g" /usr/local/redis/6379/redis_6379.conf 
sed -i "s/dbfilename dump.rdb/dbfilename dump_6379.rdb/g" /usr/local/redis/6379/redis_6379.conf 
sed -i "s/stop-writes-on-bgsave-error yes/stop-writes-on-bgsave-error no/g" /usr/local/redis/6379/redis_6379.conf 
echo "requirepass PV8NQ@RSkVHZ" >> /usr/local/redis/6379/redis_6379.conf

#启动redis
#source /etc/profile
#/usr/local/redis/bin/redis-server /usr/local/redis/6379/redis_6379.conf
#添加开机启动
#编辑 /etc/rc.local  在exit 0 之前添加/usr/local/redis/bin/redis-server /usr/local/redis/6379/redis_6379.conf

echo '[Unit]
Description=Redis
After=network.target

[Service]
WorkingDirectory=/usr/local/redis/6379
User=redis
Group=redis
Type=forking
PIDFile=/usr/local/redis/6379/redis_6379.pid
ExecStart=/usr/local/redis/bin/redis-server /usr/local/redis/6379/redis_6379.conf
ExecReload=/bin/kill -s HUP $MAINPID
ExecStop=/bin/kill -s QUIT $MAINPID

[Install]
WantedBy=multi-user.target' > /lib/systemd/system/redis_6379.service
systemctl daemon-reload
systemctl enable redis_6379
systemctl start redis_6379
systemctl status redis_6379
