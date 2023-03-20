0.创建用户

1.创建数据库database
    show databases;
    users.xml中给用户加入新的数据库
    create database device_log;
2.创建表
2.1 单单创建表
   CREATE TABLE device_log.t_device_prop_history
   (
   `in_time` DateTime comment '入库时间',
   `create_time` DateTime64(3,'Asia/Shanghai') comment '发生时间',
   `product_key` String comment '产品编号',
   `device_name` String comment '设备编号',
   `prop_value` String comment '属性',
   `identifier` String comment '值'
   )
   ENGINE = MergeTree
   PARTITION BY toYYYYMMDD(create_time)
   ORDER BY (create_time,product_key,device_name)
   TTL in_time + toIntervalMonth(1)
   SETTINGS index_granularity = 8192;
2.2 插入记录
   
2.3 更新表
把username为用户名3的记录,更新gender和age字段

2.4 按条件删除
把username为用户名6的记录删除
    
2.5 插入引用关系文档
往用户表中插入地址id

3.查询
db.user.find({条件},{返回哪些字段})
3.1 查找所有
db.user.find();
db.user.find().limit(100).pretty();
db.user.find().limit(100).sort({"username":1}).pretty(); //按照username升序排序
3.2 按条件查询
等同于 select username,age from user where gender=1 and age<=20
db.user.find({"gender":1,"age":{$lte:20}},{"username":1,"age":1})
等同于 where (gender=1) or (age<=10)
db.user.find({$or:[{"gender":1},{"age":{$lte:10}}]})
等同于 where username="用户名3" and (gender=2 or age<=20)
db.user.find({"username":"用户名3",$or:[{"gender":2},{"age":{$lte:20}}]})
4. 聚合查询aggregate
   4.1 等同于 select gender as _id,count(*) as count,sum(gender) as sum from user group by gender; 注意_id是固定写法
   db.user.aggregate([{$group:{_id:"$gender",count:{$sum:1},sum:{$sum:"$gender"}}}])
   4.2 筛选某几个字段
   db.user.aggregate(
   { $project : {
   // _id : 0 , 注释这行则不会返回主键id
   username : 1 ,
   gender : 1
   }});
   4.3 管道
   等同于select gender,count(*) from user where (age>=20 and age<=40) and username="用户名2" group by gender
   db.user.aggregate( [
   { $match : { age : { $gte : 20, $lte : 40 },username:"用户名2" } },
   { $group: { _id: "$gender", count: { $sum: 1 } } }
   ] );


5.创建索引
5.1后台运行创建索引，非唯一索引，索引名称为
db.user.createIndex({gender:1,"username":1},{background:true,unique:false,name:"idx_gender_username"})
5.2查看索引
db.user.getIndexes()
5.3删除索引
db.user.dropIndex("idx_gender_username")
5.4利用索引
索引在ram内存，并且不包含id。排除掉id，防止回表
db.user.find({gender:1},{username:1,_id:0})
5.5强制利用某个索引
db.user.find({gender:1},{user_name:1,_id:0}).hint({gender:1,user_name:1})
6.数据备份
进入容器运行备份
mongodump    -h 127.0.0.1       -u root -p 123  -d iotdb  -o mybackup/ --authenticationDatabase admin
进入容器运行恢复
mongorestore -h 127.0.0.1:27017 -u root -p 123  -d iotdb  /mybackup/iotdb --authenticationDatabase admin
7.监控
mongostat -u root -p 123 --authenticationDatabase admin
8.事务
只支持单文档的原子性操作。
解决方案


