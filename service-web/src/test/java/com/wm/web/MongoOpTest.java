package com.wm.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wm.mongo.entity.User;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
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
    public void simpleInsert(){
        User user = new User();user.setUsername("用户名399");
        user.setAge(390);user.setGender(1);
        user.setReportTimestamp(new Date());user.setLng(120.0101);
        mongoTemplate.insert(user,"user");
    }

    //使用对象的方式返回，id主键会自动转成整个string
    @Test
    public void queryByCriteria(){
        //等同于 where gender=1 and 10<=age<=20 and (username="用户名2" or gender=1) order by username
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("gender").is(1);
        criteria.and("age").lte(20).gte(10);
        criteria.orOperator(Criteria.where("username").is("用户名2"),Criteria.where("gender").is(1));
        //criteria.andOperator(Criteria.where("startTime").lt(now),Criteria.where("endTime").gt(now));
        //Pattern pattern = Pattern.compile("^.*" + eventRuleName + ".*$", Pattern.CASE_INSENSITIVE);
        //criteria.and("eventRuleName").regex(pattern);
        query.addCriteria(criteria);
        //排序
        query.with(Sort.by("username"));

        List<User> result = mongoTemplate.find(query, User.class,"user");
        System.out.println(JSON.toJSONString(result));
    }

    @Test
    public void queryPage(){
        //@Min(value = 1, message = "最小值为1") @PathVariable(value = "pageIndex", required = true) Integer pageIndex,
        //@Range(min = 1, max = 100, message = "pageSize必须在1到100之间") @PathVariable(value = "pageSize", required = true) Integer pageSize,
        int pageIndex = 1;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageIndex-1,pageSize);
        Query query = new Query();
        query.with(Sort.by("username"));
        query.with(pageable);
        List<JSONObject> list = mongoTemplate.find(query, JSONObject.class,"user");
        Page<JSONObject> result = PageableExecutionUtils.getPage(list,pageable,()->mongoTemplate.count(query,JSONObject.class));
        System.out.println(JSON.toJSONString(result));
    }


    @Test
    public void queryById(){
        JSONObject result = mongoTemplate.findById("63b8028468f87570e3fbd366",JSONObject.class,"user");
        System.out.println(JSON.toJSONString(result));
    }


    @Test
    public void aggregate(){
        // 管道 先过滤条件,根据性别进行分组统计，结果为个数和总值
        //db.user.aggregate([{"$match":{"gender":{"$in":[0,1,2]}}},{"$group":{"_id":"$gender","count":{"$sum":1},"sum":{"$sum":"$gender"}}}])
        //select gender as _id,count(*) as count,sum(gender) as sum from user where gender in (0,1,2) group by gender; 注意_id是固定写法
        MatchOperation match = Aggregation.match(Criteria.where("gender")
                                          .in(Arrays.asList(0,1,2)));
        GroupOperation group = Aggregation.group("gender")
                                           .count().as("count")
                                           .sum("gender").as("sum")
                                           .push(Aggregation.CURRENT).as("detailRecord"); //把包含的数据也放到查询里
        ProjectionOperation projection = Aggregation.project("count","sum","detailRecord")
                                                    .andExclude(Fields.UNDERSCORE_ID)
                                                    .and(ArrayOperators.Slice.sliceArrayOf("$detailRecord").offset(1).itemCount(20)).as("detailRecord");

        Aggregation aggregation = Aggregation.newAggregation(match,group,projection);

        AggregationResults<JSONObject> result = mongoTemplate.aggregate(aggregation, "user", JSONObject.class);
        System.out.println("query: " + aggregation + " | groupQuery " + result.getMappedResults());
    }

    //insert update 事务
}