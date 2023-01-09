package com.wm.mongo.util;

import com.mongodb.MongoClientOptions;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-01-09 12:50
 */
public class MongoOptionUtil {

    public MongoOptionUtil() {
    }

    public static MongoClientOptions mongoClientOptions(String clientName) {
        return MongoClientOptions.builder()
                .connectTimeout(10000)
                .socketTimeout(15000)
                .applicationName(clientName)
                .heartbeatConnectTimeout(20000)
                .heartbeatSocketTimeout(15000)
                .heartbeatFrequency(20000)
                .minHeartbeatFrequency(8000)
                .maxConnectionIdleTime(60000)
                .maxConnectionLifeTime(120000)
                .maxWaitTime(3000)
                .connectionsPerHost(20)
                .threadsAllowedToBlockForConnectionMultiplier(10)
                .minConnectionsPerHost(5)
                .build();
    }

    public static MongoProperties getMongoProperties(MongoProperties mongoProperties) {
        String url = mongoProperties.getUri();
        String params[] = url.split(":");
        mongoProperties.setUsername(params[1].replace("//", ""));
        mongoProperties.setPassword(params[2].split("@")[0].toCharArray());
        mongoProperties.setHost(params[2].split("@")[1]);
        mongoProperties.setPort(Integer.parseInt(params[3].split("/")[0]));
        mongoProperties.setDatabase(params[3].split("/")[1]);
        return mongoProperties;
    }
}