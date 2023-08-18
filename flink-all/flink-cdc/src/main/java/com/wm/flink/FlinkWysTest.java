package com.wm.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-04-15 14:08
 */
public class FlinkWysTest {

    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(3000L);
//        env.setParallelism(1);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
//        TableEnvironment tableEnv = TableEnvironment.create(settings);

        // 2. 创建表
        String createDDL = "CREATE TABLE  mytest_source (" +
                " id INT, " +
                " cluster_id BIGINT, " +
                " `config` STRING, " +
                "PRIMARY KEY (id) NOT ENFORCED"+
                " ) WITH (" +
                "'connector' = 'mysql-cdc', " +
                "'hostname' = '1.117.104.123',  " +
                "'port' = '3306', " +
                "'username' = 'root', " +
                "'password' = 'hik@i2v', " +
                "'database-name' = 'flinktest2'," +
                "'table-name' = 'mytest' " +
                ")";

        tableEnv.executeSql(createDDL);

        String createDDL2 = "CREATE TABLE  mytest_source2 (" +
                " id2 INT, " +
                " cluster_id2 BIGINT, " +
                " `config2` STRING, " +
                "PRIMARY KEY (id2) NOT ENFORCED"+
                " ) WITH (" +
                "'connector' = 'mysql-cdc', " +
                "'hostname' = '1.117.104.123',  " +
                "'port' = '3306', " +
                "'username' = 'root', " +
                "'password' = 'hik@i2v', " +
                "'database-name' = 'flinktest2'," +
                "'table-name' = 'mytest2' " +
                ")";

        tableEnv.executeSql(createDDL2);

        String sinkDDL = "CREATE TABLE  mytest_sink (" +
                " id INT, " +
                " cluster_id BIGINT, " +
                " `config` STRING, " +
                " id2 INT, " +
                " cluster_id2 BIGINT, " +
                " `config2` STRING, " +
                "PRIMARY KEY (id) NOT ENFORCED"+
                ") WITH (" +
                " 'connector' = 'upsert-kafka', " +
                " 'topic' = 'mytest2', " +
             //   " 'properties.bootstrap.servers' = '192.168.40.131:19093,192.168.40.131:19094,192.168.40.131:19095', " +
                " 'properties.bootstrap.servers' = '1.117.104.123:9092', " +
                " 'key.format' = 'json', " +
                " 'value.format' = 'json', " +
                " 'key.json.ignore-parse-errors' = 'true', " +
                " 'value.json.fail-on-missing-field' = 'false', " +
                " 'value.fields-include' = 'ALL' " +
                ")";

        tableEnv.executeSql(sinkDDL);
        tableEnv.executeSql("insert into mytest_sink select * from mytest_source a, mytest_source2 b where a.id =b.id2");
//        Table rasult  = tableEnv.sqlQuery("select * from mytest_source");
//        rasult.executeInsert("mytest_sink");
//        env.execute();
    }

}