package com.wm.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-04-15 14:08
 */
public class FlinkWm1Test {

    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(3000L);
//        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
//        TableEnvironment tableEnv = TableEnvironment.create(settings);

        // 2. 创建mysql表1
        String createDDL = "CREATE TABLE  user_source (" +
                " id INT, " +
                " `name`  STRING, " +
                " `phone` STRING, " +
                " age INT, " +
                " `address` STRING, " +
                "PRIMARY KEY (id) NOT ENFORCED"+
                " ) WITH (" +
                "'connector' = 'mysql-cdc', " +
                "'hostname' = '192.168.40.131',  " +
                "'port' = '3306', " +
                "'username' = 'root', " +
                "'password' = '123456', " +
                "'database-name' = 'flink1'," +
                "'table-name' = 't_user' " +
                ")";

        tableEnv.executeSql(createDDL);

        String createDDL2 = "CREATE TABLE  device_source (" +
                " id INT, " +
                " `phone` STRING, " +
                " `imei` STRING, " +
                " `device_name` STRING, " +
                //" `create_time` DATETIME, " +
                "PRIMARY KEY (id) NOT ENFORCED"+
                " ) WITH (" +
                "'connector' = 'mysql-cdc', " +
                "'hostname' = '192.168.40.131',  " +
                "'port' = '3306', " +
                "'username' = 'root', " +
                "'password' = '123456', " +
                "'database-name' = 'flink2'," +
                "'table-name' = 't_device' " +
                ")";

        tableEnv.executeSql(createDDL2);

        String sinkDDL = "CREATE TABLE  kafka_sink (" +
                " id INT, " +
                " `name`  STRING, " +
                " `phone` STRING, " +
                " `address` STRING, " +
                " `device_name` STRING, " +
                //" `create_time` LONG, " +
                "PRIMARY KEY (id) NOT ENFORCED"+
                ") WITH (" +
                " 'connector' = 'upsert-kafka', " +
                " 'topic' = 'wm-flink0418-cdc', " +
                " 'properties.bootstrap.servers' = '192.168.40.131:19093,192.168.40.131:19094,192.168.40.131:19095', " +
              //  " 'properties.bootstrap.servers' = '1.117.104.123:9092', " +
                " 'key.format' = 'json', " +
                " 'value.format' = 'json', " +
                " 'key.json.ignore-parse-errors' = 'true', " +
                " 'value.json.fail-on-missing-field' = 'false', " +
                " 'value.fields-include' = 'ALL' " +
                ")";

        tableEnv.executeSql(sinkDDL);
        tableEnv.executeSql("insert into kafka_sink select b.id,a.name,a.phone,a.address,b.device_name from user_source a, device_source b where a.id =b.id");
//        Table rasult  = tableEnv.sqlQuery("select * from mytest_source");
//        rasult.executeInsert("mytest_sink");
//        env.execute();
    }

}