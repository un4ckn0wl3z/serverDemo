package com.haoyun.commons.mongodb;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

/**
 * Created by songcz on 2017/4/11.
 */
public interface MongoDao {
    
    public void insert(Object o, String collectionName);
    
    public <T> T findOne(Query query, String collectionName, Class<T> clazz);
    
    //查找所有  
    public <T> List<T> findAll(Query query, String collectionName, Class<T> clazz);

    public <T> List<T> find(Query query, Class<T> entityClass, String collectionName);

    //查找所有
    public <T> List<T> findAll(Class<T> entityClass, String collectionName);

    // 更新sert
    public int upsert(Query query, Update update, String collectionName);

    // 更新一个
    public int updateFirst(final Query query, final Update update, final String collectionName);

    //更新多个
    public int updateMulti(Query query, Update update, String collectionName);

    //创建集合  
    public void createCollection(String collectionName);
    
    //根据条件删除
    public <T> void remove(Query query, String collectionName);
    
    public long count(Query query, String collectionName);
    
    public <T> T findAndModify(Query query, Update update, String collectionName, Class<T> clazz);
}
