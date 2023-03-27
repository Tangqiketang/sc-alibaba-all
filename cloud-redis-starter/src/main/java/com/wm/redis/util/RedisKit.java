package com.wm.redis.util;

import com.wm.redis.constant.BusinessTypeEnum;
import com.wm.redis.constant.RedisKeyConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @auther WangMin
 * @create 2022-06-22 22:56
 */
@Slf4j
@Component
public class RedisKit {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    public RedisConnectionFactory getConnectionFactory() {
        return this.redisTemplate.getConnectionFactory();
    }
    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 添加到带有 过期时间的  缓存
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间(单位秒)
     */
    public void setExpire(final byte[] key, final byte[] value, final long time) {
        redisTemplate.execute((RedisCallback<Long>) connection -> {
            connection.setEx(key, time, value);
            return 1L;
        });
    }

    /**
     * 添加到带有 过期时间的  缓存
     *
     * @param key   redis主键
     * @param value 值
     * @param time  过期时间
     * @param timeUnit  过期时间单位
     */
    public void setExpire(final String key, final Object value, final long time, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }




    /************************************通用***********************************************/

    /**
     * 集合数量
     *
     * @return the long
     */
    public long dbSize() {
        return redisTemplate.execute(RedisServerCommands::dbSize);
    }

    /**
     * 清空redis存储的数据
     *
     * @return the string
     */
    public String flushDB() {
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            connection.flushDb();
            return "ok";
        });
    }



    /**
     * 设置key失效
     * @param key key
     * @param time 秒
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 根据key获取过期时间
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 批量删除
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((List<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 根据正则表达式获取key列表
     *
     * @param patternKey 正则表达式
     * @return 匹配key列表
     */
    public Set<String> keys(String patternKey) {
        try {
            Set<String> keys = redisTemplate.keys(patternKey);
            return keys;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public Set<String> scan(String matchKey) {
        Set<String> keys = redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match("*" + matchKey + "*").count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            try {
                cursor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return keysTmp;
        });

        return keys;
    }


    /************************************string <string-object>***********************************************/

    public ValueOperations<String, Object> opsForValue(){
        return redisTemplate.opsForValue();
    }

    /**
     * 普通缓存放入。存入和取出类型一致
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void multiSave(Map<String, Object> source) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            // 这里逻辑简单不会抛异常
            // 否则需要加上try...catch...finally防止链接未正常关闭 造成泄漏
            connection.openPipeline();
            source.forEach((key, value) -> {
                // hset zset都是可以用的，但是要序列化
                connection.set(RedisSerializer.string().serialize(key),
                        RedisSerializer.json().serialize(value));
                // 设置过期时间 10天
                connection.expire(RedisSerializer.string().serialize(key), TimeUnit.DAYS.toSeconds(10));
            });
            connection.close();
            // executePipelined源码要求RedisCallback必须返回null，否则抛异常
            return null;
        });
    }

    /**
     * 根据key获取整个对象。存入类型和拿出来类型一致
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }
    /**
     * 普通缓存放入并设置时间
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 递增
     * @param key   键
     * @param delta 要增加几(大于0)  1  -1
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }
    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    /************************************List string-list<Object>***********************************************/

    /**
     * redis List 引擎
     * @return the list operations
     */
    public ListOperations<String, Object> opsForList() {
        return redisTemplate.opsForList();
    }

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始 0表示第一个
     * @param end   结束 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * redis List数据结构 : 将一个或多个值 value 插入到列表 key 的表头
     *
     * @param key   the key
     * @param value the value
     * @return the long
     */
    public Long leftPush(String key, Object value) {
        return opsForList().leftPush(key, value);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的头元素
     *
     * @param key the key
     * @return the string
     */
    public Object leftPop(String key) {
        return opsForList().leftPop(key);
    }

    /**
     * redis List数据结构 : 移除并返回列表 key 的末尾元素
     *
     * @param key the key
     * @return the string
     */
    public Object rightPop(String key) {
        return opsForList().rightPop(key);
    }




    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }





    /************************************Hash***********************************************/
    /**
     * 获取hash操作类
     * @return
     */
    public HashOperations<String, String, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    /**
     * hget获取hash结构中某个字段的值
     * @param key  key
     * @param item field
     * @return
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取整个hash结构转成对象
     * @param key
     * @return
     */
    public List<Object> hget(String key){
        BoundHashOperations<String, Object, Object> boundHashOps = redisTemplate.boundHashOps(key);
        List<Object> result = boundHashOps.values();
        return result;
    }


    /**
     * hmget获取整个hash结构
     * @param key
     * @return
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     *hash结构key存入整个hash
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *hash结构key存入整个hash。并设置时间
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hash设置某个filed的值
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * hash设置某个filed的值
     * @param key
     * @param item
     * @param value
     * @param time 秒
     * @return
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除或批量删除hash中某些field字段
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 查询hash中是否有某个field字段
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }


    /************************************Set***********************************************/

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存.可批量
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除Set中值为value的数据
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /*************************************ZSet **************************************************/

    /**
     * 添加数据
     *
     * 添加方式：
     * 1.创建一个set集合
     * Set<ZSetOperations.TypedTuple<Object>> sets=new HashSet<>();
     * 2.创建一个有序集合
     ZSetOperations.TypedTuple<Object> objectTypedTuple1 = new DefaultTypedTuple<Object>(value,排序的数值，越小越在前);
     4.放入set集合
     sets.add(objectTypedTuple1);
     5.放入缓存
     reidsImpl.Zadd("zSet", list);
     * @param key
     * @param tuples
     */
    public void Zadd(String key,Set<ZSetOperations.TypedTuple<Object>> tuples) {
        redisTemplate.opsForZSet().add(key, tuples);
    }

    /**
     * 获取有序集合的成员数
     * @param key
     * @return
     */
    public Long Zcard(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }

    /**
     * 计算在有序集合中指定区间分数的成员数
     * @param key
     * @param min 最小排序分数
     * @param max 最大排序分数
     * @return
     */
    public Long Zcount(String key,Double min,Double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 获取有序集合下标区间 start 至 end 的成员  分数值从小到大排列
     * @param key
     * @param start
     * @param end
     */
    public Set<Object> Zrange(String key,int start,int end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取有序集合下标区间 start 至 end 的成员  分数值从大到小排列
     * @param key
     * @param start
     * @param end
     */
    public Set<Object> reverseRange(String key,int start,int end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 返回 分数在min至max之间的数据 按分数值递减(从大到小)的次序排列。
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<Object> reverseRange(String key,Double min,Double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 返回指定成员的下标
     * @param key
     * @param value
     * @return
     */
    public Long Zrank(String key,Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 删除key的指定元素
     * @param key
     * @param values
     * @return
     */
    public Long ZremoveValue(String key,Object values) {
        return redisTemplate.opsForZSet().remove( key, values);
    }

    /**
     * 移除下标从start至end的元素
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long ZremoveRange(String key,int start,int end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 移除分数从min至max的元素
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long ZremoveRangeByScore(String key,Double min,Double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /*****************************************bitmap [01234567][01234567][01234567]*************************************************/

    public Boolean getBit(String key, Long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    public Boolean setBit(String key, Long offset, boolean value) {
        return redisTemplate.opsForValue().setBit(key, offset, value);
    }

    // 获取 1bit 下标从1-7的所有数据
    public List<Long> getBitList(String key) {
        BitFieldSubCommands command = BitFieldSubCommands.create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(1)
                .get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(2)
                .get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(3)
                .get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(4)
                .get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(5)
                .get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(6)
                .get(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(7);
        return redisTemplate.opsForValue().bitField(key,command);
    }

    /**
     *
     * @param key
     * @return
     */
    public long bitCount(String key){
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.bitCount(key.getBytes());
            }
        });
    }

    /*****************************************************************************************/

    /**获取某一业务类型自增id
     * @param businessEnum 业务类型枚举 redis中key为 business_no:300:20220812
     * @param digit        业务序号位数 默认8
     * @return   20220812 300 00000002  总长度=11+8
     */
    public String generate(BusinessTypeEnum businessEnum, Integer digit) {
        if(null==digit){
            digit=8;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        String key = RedisKeyConstant.BUSINESS_NO_PREFIX + businessEnum.getValue() + ":" + date;
        Long increment = redisTemplate.opsForValue().increment(key);
        return date + businessEnum.getValue() + String.format("%0" + digit + "d", increment);
    }


}