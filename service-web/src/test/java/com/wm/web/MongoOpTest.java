package com.wm.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-01-09 16:29
 */
public class MongoOpTest extends BaseTest{

    @Resource
    private MongoTemplate mongoTemplate;

    @Test
    public void getIOTDB(){
        Query query = new Query();
        List<JSONObject> result = mongoTemplate.find(query, JSONObject.class,"user");
        System.out.println(JSON.toJSONString(result));
    }
}