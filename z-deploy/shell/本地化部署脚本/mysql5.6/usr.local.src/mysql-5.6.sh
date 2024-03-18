#!/bin/sh
useradd mysql  -M -s /sbin/nologin
cd /usr/local/src
tar -xzvf mysql-5.6.51-linux-glibc2.12-x86_64.tar.gz
mv /usr/local/src/mysql-5.6.51-linux-glibc2.12-x86_64 /usr/local/
ln -s /usr/local/mysql-5.6.51-linux-glibc2.12-x86_64 /usr/local/mysql
echo "[mysqld]
skip-name-resolve
basedir=/usr/local/mysql
datadir=/usr/local/mysql/data
socket=/tmp/mysql.sock
user = mysql
tmpdir = /tmp
symbolic-links=0
port=3306
max_connections=200
character-set-server=utf8
default-storage-engine=INNODB
lower_case_table_names=1
max_allowed_packet=16M

#GTID
#gtid_mode = on
#enforce_gtid_consistency = 1
#log_slave_updates = 1

#binlog
#binlog_format = ROW
#log-bin=/usr/local/mysql/log/binlog/mysql-bin
#expire_logs_days=3
#server-id = 1

#slow_query_log
#slow_query_log = 1
#long_query_time = 1
#slow-query-log-file = /usr/local/mysql/log/slow.log

innodb_buffer_pool_size = 4G
read_buffer_size = 262144
innodb_log_file_size = 96M
innodb_log_files_in_group = 4
innodb_log_buffer_size = 16M
sync_binlog=1

[mysqld_safe]
log-error=/usr/local/mysql/data/mysql_error.log
pid-file=/usr/local/mysql/data/mysql.pid
# include all files from the config directory

[client]
default-character-set=utf8

[mysql]
default-character-set=utf8" > /etc/my.cnf
cd /usr/local/mysql
apt -y install autoconf libaio1
./scripts/mysql_install_db --user=mysql --basedir=/usr/local/mysql --datadir=/usr/local/mysql/data
cp /usr/local/mysql/support-files/mysql.server /etc/init.d/mysqld
chmod +x /etc/init.d/mysqld
mkdir -p /var/lib/mysql
touch /var/lib/mysql/mysql.sock
chown -R mysql.mysql /var/lib/mysql
update-rc.d mysqld defaults 90
#添加环境变量，并立刻重新加载环境变量和启动
echo -e '##### mysql \nexport PATH=/usr/local/mysql/bin:$PATH' >>/etc/profile
source /etc/profile
systemctl start mysqld
/usr/local/mysql/bin/mysql <<EOF
use mysql;
update user set password=password('PV8NQ@RSkVHZ') where user='root';
grant all on *.* to 'root'@'%' identified by 'PV8NQ@RSkVHZ' with grant option;
exit
EOF
systemctl restart mysqld

