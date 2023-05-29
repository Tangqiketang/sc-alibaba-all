授权数据库特殊用户
grant all privileges on babycare.* to 'superwm'@'%' identified by '9NEtMDXitYI=' ;
grant all PRIVILEGES on test1_sharding.* to root@'%' identified by '123456';


备份
https://www.zhihu.com/question/38374712
docker exec -it mysql_master_1 mysqldump -uroot -proot!Q2w  data-manager > data-manager.sql
docker exec -it mysql_master_1 mysqldump --opt --single-transaction  --default-character-set=utf8  -uroot -p123456 -A > allwmmysql.sql
=========================================================
千万级别表数据更新：
    mysql在表数据量很大的时候，如果修改表结构会导致锁表，业务请求被阻塞。
    mysql在5.6之后引入了在线更新，但是在某些情况下还是会锁表，
    所以一般都采用pt工具( Percona Toolkit)
    pt-online-schema-change --user='root' --host='localhost' --ask-pass --alter "add index idx_user_id(room_id,create_time)"
D=fission_show_room_v2,t=room_favorite_info --execute

https://blog.csdn.net/baidu_34007305/article/details/112944448 同步数据


===================================================================================================
mysql




====================================================================================================
常用的但容易忘的：
    1、如果有主键或者唯一键冲突则不插入： insert ignore into;
    2、如果有主键或者唯一键冲突则更新,注意这个会影响自增的增量： INSERT INTO room_remarks(room_id,room_remarks) VALUE(1,"sdf") ON DUPLICATE KEY UPDATE room_remarks="234";
    3、如果有就用新的替代，values如果不包含自增列，自增列的值会变化： REPLACE INTO room_remarks(room_id,room_remarks) VALUE(1,"sdf");
    4、备份表： CREATE TABLE user_info SELECT * FROM user_info;
    5、复制表结构： CREATE TABLE user_v2 LIKE user;
    6、从查询语句中导入： INSERT INTO user_v2 SELECT * FROM user或者INSERT INTO user_v2(id,num) SELECT id,num FROM user;
    7、连表更新： UPDATE user a, room b SET a.num=a.num+1 WHERE a.room_id=b.id;
    8、连表删除： DELETE user FROM user,black WHERE user.id=black.id;

锁相关(作为了解，很少用)
    1、共享锁： select id from tb_test where id = 1 lock in share mode;
    2、排它锁： select id from tb_test where id = 1 for update;

优化时用到：
1、强制使用某个索引： select * from table force index(idx_user) limit 2;
2、禁止使用某个索引：select * from table ignore index(idx_user) limit 2;
3、禁用缓存(在测试时去除缓存的影响)： select SQL_NO_CACHE from table limit 2;

SQL编写注意
    1、where语句的解析顺序是从右到左，条件尽量放where不要放having。
    2、采用延迟关联(deferred join)技术优化超多分页场景，比如limit 10000,10,延迟关联可以避免回表。
    3、distinct语句非常损耗性能，可以通过group by来优化。
    4、连表尽量不要超过三个表。