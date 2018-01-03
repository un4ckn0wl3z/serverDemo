package com.haoyun.commons.redis;

import com.alibaba.fastjson.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by Administrator on 2017/4/11.
 */
@Repository
public class RedisDaoImpl implements RedisDao {

    private static final String PREFIX_KEY = "my_lock";

    private RedisTemplate redisTemplate;

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean addString(String key, String name) {
        final String k = key;
        final String n = name;
        boolean result = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(k);
                byte[] name = serializer.serialize(n);
                return connection.setNX(key, name);
            }
        });
        return result;
    }

    @Override
    public void delete(String key) {
        List<String> list = new ArrayList<>();
        list.add(key);
        delete(list);
    }

    @Override
    public void delete(List keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public String get(String keyId) {
        final String k = keyId;
        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(k);
                byte[] value = connection.get(key);
                if(value == null){
                    return null;
                }
                String name = serializer.deserialize(value);
                return name;
            }
        });
//        JSONObject obj= JSON.parseObject(result);
        return result;
    }

    @Override
    public boolean update(String keyId, String name) {
        final String k = keyId;
        final String n = name;
        if(get(k) == null){
            throw new NullPointerException("数据行不存在，key = " + k);
        }
        boolean result = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(k);
                byte[] name = serializer.serialize(n);
                connection.set(key, name);
                return true;
            }
        });
        return result;
    }


    @Override
    public byte[] getStringByte(String o) {
        RedisSerializer serializer = redisTemplate.getStringSerializer();
        byte[] value = serializer.serialize(o);
        return value;
    }

    @Override
    public String getSetRandomValue(String key) {
        final String k = key;
        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(k);
                byte[] value = connection.sRandMember(key);
                String name = serializer.deserialize(value);
                return name;
            }
        });
        return result;
    }

    @Override
    public String getSetRandomValueDel(String key) {
        final String k = key;
        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(k);
                byte[] value = connection.sPop(key);
                String name = serializer.deserialize(value);
                return name;
            }
        });
        return result;
    }

    @Override
    public boolean addRoomCodePoolSet(String key, String value) {
        final String k = key;
        final String v = value;
        boolean result = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {


                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] key = serializer.serialize(k);
                byte[] name = serializer.serialize(v);
                connection.sAdd(key,name);
                return true;
            }
        });
        return result;
    }

    /**
     * 获取锁
     * @param lockName roomCode
     * @return
     */
    @Override
    public boolean acquireLock(String lockName) {
        String redisKey = PREFIX_KEY + lockName;
        // 参数需要测试后进行修改
        long expire = 15; // 单位：秒
        long timeout = 5000; // 单位毫秒
        long redisValue = System.currentTimeMillis() + timeout + 1;
        // 通过SETNX试图获取一个lock
        if(setNX(redisKey, String.valueOf(redisValue), expire)){ // SETNX成功，则成功获取一个锁
            return true;
        }else{
            long oldValue = Long.valueOf(getValue(redisKey).toString());
            // 超时
            if(oldValue < System.currentTimeMillis()){
                String getValue = getAndSet(redisKey, String.valueOf(redisValue));
                // 获取锁成功
                if(getValue.equals(oldValue)){
                    return true;
                }else{ // 已被其他进程捷足先登
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    /**
     * 释放锁
     * @param lockName
     */
    @Override
    public void releaseLock(String lockName) {
        String redisKey = PREFIX_KEY + lockName;
        redisTemplate.delete(redisKey);
    }

    private boolean setNX(final String key, final String value, final long expire) {
        return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection) {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                boolean locked = connection.setNX(keyBytes, redisTemplate.getDefaultSerializer().serialize(value));
                if(locked){
                    // 给key设置生存时间，当key过期时，他会被自动删除
                    connection.expire(keyBytes, expire);
                }
                return locked;
            }
        });
    }

    private Object getValue(final String key) {
        return redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bs = connection.get(redisTemplate.getStringSerializer().serialize(key));
                return redisTemplate.getDefaultSerializer().deserialize(bs);
            }
        });
    }

    private String getAndSet(final String key, final String value) {
        return (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] result = connection.getSet(redisTemplate.getStringSerializer().serialize(key), //将给定 key 的值设为 value
                // ，并返回 key 的旧值(old value)。
                        redisTemplate.getDefaultSerializer().serialize(value));
                if (result != null) {
                    return new String(result);
                }
                return null;
            }
        });
    }

    /**
     * 设置过期时间
     * @param key
     */
    @Override
    public void expire(final String key, final long liveTime) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                connection.expire(keyBytes, liveTime);
                return null;
            }
        });
    }

    @Override
    public boolean exists(final String key) {
        boolean result = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                boolean tag = connection.exists(keyBytes);
                return tag;
            }
        });
        return result;
    }

    @Override
    public void addHash(final String key, final JSONObject json) {

        final Map<byte[], byte[]> mapBytes = new HashMap<>();
        Set<String> keySet = json.keySet();
        for(String key1 : keySet){
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] jsonKey = serializer.serialize(key1);
            byte[] jsonValue = serializer.serialize(json.getString(key1));
            mapBytes.put(jsonKey, jsonValue);
        }

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                connection.hMSet(keyBytes, mapBytes);
                return null;
            }
        });
    }

    @Override
    public void addHash(final String key, final Map map) {

        Map<String, Object> map1 = map;
        final Map<byte[], byte[]> mapBytes = new HashMap<>();

        for(Map.Entry<String, Object> entry : map1.entrySet()){
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] mapKey = serializer.serialize(entry.getKey());
            byte[] mapValue = serializer.serialize((String) entry.getValue());
            mapBytes.put(mapKey, mapValue);
        }

        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                connection.hMSet(keyBytes, mapBytes);
                return null;
            }
        });
    }

    @Override
    public void pushList(final String key, final String value) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                byte[] valueBytes = serializer.serialize(value);
                connection.lPush(keyBytes, valueBytes);
                return null;
            }
        });
    }

    @Override
    public void pushList(final List list) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

                for (Object obj : list) {
                    Map map = (Map) obj;
                    for (Object key : map.keySet()) {
                        byte[] keyBytes = serializer.serialize(key.toString());
                        byte[] valueBytes = serializer.serialize(map.get(key).toString());
                        connection.lPush(keyBytes, valueBytes);
                    }
                }

                return null;
            }
        });
    }

    @Override
    public List<String> rangeList(final String key, final Integer start, final Integer end) {
        List<String> lst = (List<String>) redisTemplate.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                List<byte[]> list = connection.lRange(keyBytes, start, end);
                List<String> result = new ArrayList<String>();
                if(list.size() == 0){
                    return result;
                }
                for(byte[] bytes : list){
                    String value = serializer.deserialize(bytes);
                    result.add(value);
                }
                return result;
            }
        });
        return lst;
    }

    @Override
    public Map<String, String> getMap(final String key) {
        final Map<String, String> map = (Map<String, String>) redisTemplate.execute(new RedisCallback<Map<String, String>>() {
            @Override
            public Map<String, String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                Map<byte[], byte[]> valueMap = connection.hGetAll(keyBytes);
                Map<String, String> result = new HashMap<String, String>();
                if(valueMap.size() == 0){
                    return result;
                }
                for(Map.Entry<byte[], byte[]> entry : valueMap.entrySet()){
                    String mapKey = serializer.deserialize(entry.getKey());
                    String mapValue = serializer.deserialize(entry.getValue());
                    result.put(mapKey, mapValue);
                }
                return result;
            }
        });

        return map;
    }

    @Override
    public void removeList(final String key, final String value) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                byte[] valueBytes = serializer.serialize(value);
                connection.lRem(keyBytes, 0, valueBytes);
                return null;
            }
        });
    }

    @Override
    public Set<String> getKey(final String key) {
        Set<String> keys = (Set<String>) redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                Set<byte[]> set = connection.keys(keyBytes);
                Set<String> result = new HashSet<>();
                if(set.size() == 0){
                    return result;
                }
                Iterator<byte[]> iterator = set.iterator();
                while (iterator.hasNext()){
                    String value = serializer.deserialize(iterator.next());
                    result.add(value);
                }
                return result;
            }
        });

        return keys;
    }

    @Override
    public String getValue(final String key, final String field) {
        String value = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                byte[] fieldBytes = serializer.serialize(field);
                byte[] valueBytes = connection.hGet(keyBytes, fieldBytes);
                if(valueBytes.length == 0){
                    return null;
                }

                String value = serializer.deserialize(valueBytes);
                return value;
            }
        });

        return value;
    }

    @Override
    public Long publish(final String channel, final String message) {
        Long value = (Long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(channel);
                byte[] fieldBytes = serializer.serialize(message);
                return connection.publish(keyBytes, fieldBytes);
            }
        });

        return value;
    }

    @Override
    public Boolean hSet(final String key, final String field, final String value) {
        Boolean result = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = serializer.serialize(key);
                byte[] fieldBytes = serializer.serialize(field);
                byte[] valueBytes = serializer.serialize(value);

                return connection.hSet(keyBytes, fieldBytes, valueBytes);
            }
        });

        return result;
    }
}
