package com.haoyun.commons.redis;


import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/4/11.
 */
public interface RedisDao<T> {

    // String类型增加
    boolean addString(String key, String name);

    // 删除单个
    void delete(String key);

    // 删除多个
    void delete(List<String> keys);

    // 通过key获取
    String get(String keyId);

    // 修改
    boolean update(String keyId, String name);

    byte[] getStringByte(String o);

    // 获取set中随机value
    String getSetRandomValue(String key);

    // 随机移除set中的value
    String getSetRandomValueDel(String key);

    // 将释放的房间号放回资源池
    boolean addRoomCodePoolSet(String key, String value);

    boolean acquireLock(String lockName);

    void releaseLock(String lockName);

    /**
     * 设置过期时间
     * @param key
     * @param liveTime
     */
    void expire(String key, long liveTime);

    boolean exists(String key);

    void addHash(String key, JSONObject json);

    void addHash(String key, Map map);

    void pushList(String key, String value);

    void pushList(List<Map<String, String>> list);

    List<String> rangeList(String key, Integer start, Integer end);

    Map<String, String> getMap(String key);

    void removeList(String key, String value);

    Set<String> getKey(String key);

    String getValue(String key, String field);

    Long publish(String channel, String message);

    Boolean hSet(String key, String field, String value);
}
