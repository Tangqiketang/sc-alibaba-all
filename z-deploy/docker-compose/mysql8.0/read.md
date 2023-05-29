1.进主容器
docker exec -it mysql8-master1 bash;
mysql -uroot -p123456;
GRANT ALL ON *.* TO master;
flush privileges;

2.进从容器
docker exec -it mysql8-slave1 bash;
mysql -uroot -p123456;
GRANT REPLICATION CLIENT ON *.* TO slave;
GRANT REPLICATION SLAVE ON *.* TO slave;
GRANT SUPER ON *.* TO slave;
GRANT RELOAD ON *.* TO slave;
GRANT SELECT ON *.* TO slave;
flush privileges;
从容器中切换账号
docker exec -it mysql8-slave1 bash;
mysql -uslave -p123456;
CHANGE MASTER TO master_host='192.168.40.131', master_port=3306, master_user='master', master_password='123456', master_auto_position=1;
START SLAVE;

SHOW SLAVE STATUS\G
