package com.wm.mongo.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.wm.mongo.util.MongoOptionUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.Resource;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-01-09 12:45
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.wm.mongo.dao", mongoTemplateRef = MongoDBConfig.PRIMARY_MONGO_TEMPLATE)
public class MongoDBConfig {

    public final static String PRIMARY_MONGO_TEMPLATE = "primaryMongoTemplate";

    @Resource
    private ApplicationContext appContext;

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    @Qualifier(MultipleMongoProperties.PRIMARY_MONGOP_ROPERTIES)
    private MongoProperties mongoProperties;

    @Primary
    @Bean(name = PRIMARY_MONGO_TEMPLATE)
    public MongoTemplate primaryMongoTemplate() throws Exception {
        MongoDbFactory factory = primaryFactory(this.mongoProperties);
        MongoMappingContext mongoMappingContext = new MongoMappingContext();
        mongoMappingContext.setApplicationContext(appContext);
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory),mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(factory,converter);
    }

    @Bean
    @Primary
    public MongoDbFactory primaryFactory(MongoProperties mongoProperties) throws Exception {
        mongoProperties = MongoOptionUtil.getMongoProperties(mongoProperties);
        MongoClientOptions options = MongoOptionUtil.mongoClientOptions(applicationName);
        MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(mongoProperties.getUsername(),
                mongoProperties.getAuthenticationDatabase() != null ? mongoProperties.getAuthenticationDatabase() : mongoProperties.getDatabase(),
                mongoProperties.getPassword());
        MongoClient mongoClient = new MongoClient(new ServerAddress(mongoProperties.getHost(),mongoProperties.getPort()), mongoCredential, options);
        return new SimpleMongoDbFactory(mongoClient, mongoProperties.getDatabase());
    }
}