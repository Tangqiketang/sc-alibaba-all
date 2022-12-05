授权数据库特殊用户
grant all privileges on babycare.* to 'superwm'@'%' identified by '9NEtMDXitYI=' ;
grant all PRIVILEGES on test1_sharding.* to root@'%' identified by '123456';


备份
docker exec -it dockermysql mysqldump -uroot -proot!Q2w  data-manager > data-manager.sql