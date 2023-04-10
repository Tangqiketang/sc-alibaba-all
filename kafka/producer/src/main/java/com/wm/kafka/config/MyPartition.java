package com.wm.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 描述:
 * 自定义根据key分区到partition
 *
 * topic-test 3个partition 2个副本
 *
 * broker0-
 *      test0
 * broker1-
 *      test1
 * broker2-
 *      test2
 *
 * @auther WangMin
 * @create 2023-04-09 23:45
 */
@Slf4j
public class MyPartition implements Partitioner {
    Random random = new Random();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //获取分区列表
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int partitionNum = 0;
        if(key == null){
            partitionNum = random.nextInt(partitionInfos.size());//随机分区
        } else {
            partitionNum = Math.abs((key.hashCode())/partitionInfos.size());
        }
        log.info("当前Key:{},当前Value:{},当前存储分区:{}",key, value, partitionNum);
        return partitionNum;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}