#1.mysql需要同步的两张表
    CREATE TABLE `t_user` (
    `id` int(11) NOT NULL,
    `name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
    `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `age` int(11) DEFAULT NULL,
    `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 =============================================================
    CREATE TABLE `t_device` (
    `id` int(11) NOT NULL,
    `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `imei` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `device_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
#2.代码 flink cdc接入两张表,连表查询发送到kafka
#3.clickhouse
    新建一张kafka引擎表、
    最终存储表、
    一张物化视图用于将引擎表中数据导入ck
    -- auto-generated definition
##3.1 引擎表
create table kafka_sink_virtual
(
id          Int32,
name String,
phone String,
address String,
device_name String
)
engine = Kafka SETTINGS
kafka_broker_list = '192.168.40.131:19093,192.168.40.131:19094,192.168.40.131:19095',
kafka_topic_list = 'wm-flink0418-cdc', kafka_group_name = 'ck_wmgroup1',
kafka_format = 'JSONEachRow', kafka_skip_broken_messages = 1000;

##3.2 最终存储表
    -- auto-generated definition
    create table kafka_sink_real
    (
    id          Int32,
    name String,
    phone String,
    address String,
    device_name String
    )
    engine = MergeTree ORDER BY id
    SETTINGS index_granularity = 8192;
##3.3 物化试图
      CREATE
    MATERIALIZED
    VIEW
    flink_kafka_ck.consumer_mv1
    TO
    flink_kafka_ck.kafka_sink_real
    (
    `id`          Int32,
    `name` String,
    `phone` String,
    `address` String,
    `device_name` String
    )
    AS
    SELECT id,name,phone,address,device_name
    FROM flink_kafka_ck.kafka_sink_virtual;
