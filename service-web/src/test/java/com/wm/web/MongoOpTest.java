package com.wm.web;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.bulk.BulkWriteResult;
import com.wm.mongo.entity.User;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.data.util.Pair;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2023-01-09 16:29
 */
public class MongoOpTest extends BaseTest{

    @Resource
    private MongoTemplate mongoTemplate;
    //=================================== insert ==============================================/
    @Test
    public void simpleInsert(){
        //插入新建记录
        User user = new User();user.setUsername("用户名499");
        user.setAge(490);user.setGender(1);
        user.setReportTimestamp(new Date());user.setLng(120.0404);
        mongoTemplate.insert(user);
        //mongoTemplate.insert(user,"user");
        //mongoTemplate.insert(userList,"user");
    }
    //=================================== update ==============================================/
    @Test
    public void simpleInsertOrUpdate(){
        //1.根据id更新记录。注意null值也会更新进去！！
        User updateUser = new User();
        updateUser.setId("43211111111");updateUser.setAge(666);
        updateUser.setUsername("用户名666");updateUser.setLng(121.0001);
        //有就更新没有 则插入
        mongoTemplate.save(updateUser);

        //2.
        User orginUser = mongoTemplate.findById("63b8028468f87570e3fbd366",User.class,"user");
        BeanUtil.copyProperties(updateUser,orginUser, CopyOptions.create().setIgnoreNullValue(true));
        mongoTemplate.save(updateUser);
    }

    @Test
    public void updateByCriteria(){
        Query query = new Query();
        //query.addCriteria(Criteria.where("_id").is("63bce6707fd4433e52e3fffc"));
        query.addCriteria(Criteria.where("age").is(111));
        Update update = Update.update("username", "test99999");
        //所有符合条件的都更新
        mongoTemplate.updateMulti(query, update, "user");
        //mongoTemplate.upsert(query,update,"user"); //没有则插入,有则更新但是只更新第一条，适用于id查询
    }

    @Test
    public void batchUpdate(){
        List<User> list = new ArrayList<>();
        User user1 = new User();
        user1.setUsername("test1");
        user1.setAge(111);

        User user2 = new User();
        user2.setUsername("test2");
        user2.setAge(55);

        list.add(user1);list.add(user2);

        List<Pair<Query, Update>> updateList = new ArrayList<>(list.size());
        //UNORDERED是平行处理，即使某条记录出错了，其余的也会继续处理
        //ORDERED是队列排序处理，只要中途有个失败了，那么后续的操作流程就会终止了
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "user");
        list.forEach(data -> {
            //如果query查询到有数据就更新
            Query query = new Query(new Criteria("age").is(data.getAge()));
            Update update = new Update();
            //如果userId是主键，必须使用setOnInsert()
            update.set("username",data.getUsername());
            update.set("age",data.getAge());

            Pair<Query, Update> updatePair = Pair.of(query, update);
            updateList.add(updatePair);
        });

        //没有符合条件的则插入，有符合条件的更新所有满足条件的记录
        operations.upsert(updateList);

        //必须执行才行
        BulkWriteResult result = operations.execute();
    }


    //=================================== query ==============================================/

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

    //分页查询
    @Test
    public void findUsersPage() {
        String username = "用户名";
        int pageNo = 1; //当前页
        int pageSize = 10; //每页的大小

        Query query = new Query(); //条件构建部分
        String regex = String.format("%s%s%s", "^.*", username, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where("username").regex(pattern));
        int totalCount = (int) mongoTemplate.count(query, User.class); //查询记录数
        //其中的skip表示跳过的记录数，当前页为1则跳过0条，为2则跳过10条，（也就是跳过第一页的10条数据）
        List<User> userList = mongoTemplate.find(query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class); //分页查询

        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("list", userList);
        pageMap.put("totalCount",totalCount);
        System.out.println(pageMap);
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

    //=================================== 地理位置操作 ==============================================/

    //insert update 事务
}