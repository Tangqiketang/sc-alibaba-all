#0.概念
   index:   相当于mysql中dataBase
   type:    相当于mysql中table
   document:相当于mysql中row
   field:   相当于mysql中column
   mapping: 相当于mysql中scheme
-------------------------------------------------------------------------------
   must--and   should-or(只影响得分不影响返回)
   match--分词查找 term--完全匹配  fuzzy--误拼写查询 wildcard--模糊查询
   match_phrase--短语匹配  exists--排除null
-------------------------------------------------------------------------------
##0.1type类型分类
   ###0.1.1字符串类型：
      text--需要分词的字符串类型，不能用于排序
      keyword--不需要分词的字符串类型，字段可用于过滤、排序、聚合
   ###0.1.2字符串类型：数字类型：
      byte/short/integer/long/float/double/
   ###0.1.3字符串类型：日期类型：
      默认date----可以指定format格式:  strict_date_optional_time||epoch_millis
                                  或 yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis
   ###0.1.4字符串类型：复杂类型对象：
      Object--对象内部包含对象嵌套
   ###0.1.5字符串类型：数组类型(存相同类型元素)：
      Array-------["good","bad"],[{"name":"wm","age":"32"},{"name":"zyy","age":"32"}]
      进阶版Nested------可以让Array类型中的对象可以被独立索引和搜索
   ###0.1.6字符串类型：IP类型
      ip
   ###0.1.7统计字符串中的单词数量：
      token_count

#1.kibana
##1.1 查看索引具体情况
      左侧菜单-Management-StackManagement-进入之后：
            1.1.1ElasticSearch-索引管理：可以查看分片运行情况、还有mapping表结构
            1.1.2kibana-索引模式(修改字段格式默认值？):第一步定义索引模式，输入索引名称，下一步
      增加了索引模式后，再次进入菜单Discover就可以查数据了

#2.控制台命令行开发工具
      左侧菜单-Management-DevTools

#3.创建数据库索引index及表结构map
   3.1 PUT /wmtestdb1
      {                                                                                                                                            
         "settings": {"number_of_shards": 5,"number_of_replicas": 1 }
      }
   3.2 PUT /wmtestdb1/_mapping
      {
         "properties": {
            "title": {"type": "text","analyzer": "ik_max_word"},
            "images": {"type": "keyword","index": false },
            "price": {"type": "float"},
            "created":  {"type":   "date","format": "strict_date_optional_time||epoch_millis"},
            "mytime":{"type":"date","format": "yyyy-MM-dd HH:mm:ss" }
         }
      }

#4.添加数据
   不指定id则会生成一串序列号
   4.1 POST /wmtestdb1/_doc   
       或者自定义id的方式，这种会覆盖
       POST /wmtestdb1/_doc/1
      {
         "title":"大米手机","images":"http://image.leyou.com/12479122.jpg",
         "price":2899.00,"created":"2023-05-09"
      }
#5.修改数据
   PUT /wmtestdb1/_doc/1
   {
      "title":"超大米手机","images":"http://image.leyou.com/12479122.jpg",
      "price":3899.00,"stock": 100,"saleable":true
   }
#6.删除数据
   DELETE /wmtestdb1/_doc/1
#7.查询
   #7.1 查询所有
      GET /wmtestdb1/_search
      { "query": { "match_all": {} } }
   #7.2 按条件，并且返回结果过滤掉images字段、选定title、price字段. 分词之后包含60%相似
      GET /wmtestdb1/_search
      {
         "_source": {"excludes": "images","includes":["title","price"]},
         "query": {
            "match": {
               "title": {
                  "query": "超大米手机",
                  "minimum_should_match": "60%"
               }
            }
         }
      }
   #7.2 多个字段匹配
      GET /wmtestdb1/_search
      {
         "query": {
            "multi_match": {
               "query": "超大米手机",
               "fields":["title","subTitle"]
            }
         }
      }
   #7.3 
      select title,price from wmtestdb1 where title like分词查 '%超大米手机%' and price=3899 limit 0,3
   must必须、should会影响得分
   GET /wmtestdb1/_search
   {
      "_source": {"excludes": "images","includes":["title","price","stock"]},
      "query": {
         "bool": {
            "must": [{"match": {"title": "超大米手机"} },
                     {"term": {"price": 3899 }}
                    ],
            "should": [{"terms": {"price": ["2699","2799"]} }]
         }
      },
      "sort":[{"stock":"asc"},
                {"price":"asc"}
             ],
      "from":0,
      "size":3
   }