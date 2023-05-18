package com.wm.es.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 描述:
 * /**
 * 注解：@Document用来声明Java对象与ElasticSearch索引的关系
 *              indexName 索引名称(是字母的话必须是小写字母)
 *              type 索引类型
 *              shards 主分区数量，默认5
 *              replicas 副本分区数量，默认1
 *              createIndex 索引不存在时，是否自动创建索引，默认true
 *                         不建议自动创建索引(自动创建的索引 是按着默认类型和默认分词器)
 * 注解：@Id 表示索引的主键
 * 注解：@Field 用来描述字段的ES数据类型，是否分词等配置，等于Mapping描述
 *              index 设置字段是否索引，默认是true，如果是false则该字段不能被查询
 *              store 标记原始字段值是否应该存储在 Elasticsearch 中，默认值为false，以便于快速检索。虽然store占用磁盘空间，但是减少了计算。
 *              type 数据类型(text、keyword、date、object、geo等)
 *              analyzer 对字段使用分词器，注意一般如果要使用分词器，字段的type一般是text。
 *              format 定义日期时间格式
 * 注解：@CompletionField 定义关键词索引 要完成补全搜索
 *              analyzer 对字段使用分词器，注意一般如果要使用分词器，字段的type一般是text。
 *              searchAnalyzer 显示指定搜索时分词器，默认是和索引是同一个，保证分词的一致性。
 *              maxInputLength:设置单个输入的长度，默认为50 UTF-16 代码点
 *
 *
 *  * @Document 作用在类，标记实体类为文档对象，一般有两个属性
 *  * indexName：对应索引库名称
 *  * type：对应在索引库中的类型
 *  * shards：分片数量，默认5
 *  * replicas：副本数量，默认1
 *  * @Id 作用在成员变量，标记一个字段作为id主键
 *  * @Field 作用在成员变量，标记为文档的字段，并指定字段映射属性：
 *  * type：字段类型，是枚举：FieldType，可以是text、long、short、date、integer、Nested、object等
 *  * text：存储数据时候，会自动分词，并生成索引
 *  * keyword：存储数据时候，不会分词建立索引
 *  * Numerical：数值类型，分两类
 *  * 基本数据类型：long、integer、short、byte、double、float、half_float
 *  * 浮点数的高精度类型：scaled_float
 *  * 需要指定一个精度因子，比如10或100。elasticsearch会把真实值乘以这个因子后存储，取出时再还原。
 *  * Date：日期类型
 *  * elasticsearch可以对日期格式化为字符串存储，但是建议我们存储为毫秒值，存储为long，节省空间。
 *  * index：是否索引，布尔类型，默认是true ?
 *  * store：是否存储，布尔类型，默认是false ?
 *  * analyzer：分词器名称，这里的ik_max_word即使用ik分词器
 *  *
 *
 *
 * @auther WangMin
 * @create 2023-05-08 23:42
 */
@Data
@Document(indexName = "es_user",type = "user")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EsUser implements Serializable {

    @Id
    @Field(store = true, type = FieldType.Keyword)
    private String id;

    @Field(store = true, type = FieldType.Keyword)
    @ApiModelProperty(value = "用户名")
    private String username;

    @Field(index = false, store = true, type = FieldType.Integer)
    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "经度")
    @Field(type = FieldType.Double)
    private Double lng;


    @ApiModelProperty(value = "地址")
    @Field(store = true, type = FieldType.Text, analyzer = "ik_smart")
    //Text可以分词 ik_smart=粗粒度分词 ik_max_word 为细粒度分词
    private String address;

    @Field(index = false, store = true, type = FieldType.Date, format = DateFormat.basic_date_time)
    @ApiModelProperty("上报时间")
    private Date reportTimestamp;

    @Field(type = FieldType.Keyword)
    private String[] sCourseList;

    @Field(type = FieldType.Keyword)
    private List<String> sColorList; //集合类型 由数组中第一个非空值决定

}