package com.haoyun.commons.mongodb;

import com.mongodb.WriteResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by songcz on 2017/4/11.
 */
@Repository
public class MongoDaoImpl implements MongoDao {
    
    private MongoTemplate mongoTemplate;
    
    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
    
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    
    @Override
    public void insert(Object o, String collectionName) {
        mongoTemplate.insert(o, collectionName);
    }
    
    @Override
    public <T> T findOne(Query query, String collectionName, Class<T> clazz) {
        return mongoTemplate.findOne(query, clazz, collectionName);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
        return mongoTemplate.find(query, entityClass, collectionName);
    }

    @Override
    public <T> List<T> findAll(Query query, String collectionName, Class<T> clazz) {
        return mongoTemplate.find(query, clazz, collectionName);
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
        return mongoTemplate.findAll(entityClass, collectionName);
    }

    @Override
    public int upsert(Query query, Update update, String collectionName) {
        WriteResult writeResult = mongoTemplate.upsert(query, update, collectionName);
        return writeResult.getN();
    }

    @Override
    public int updateFirst(final Query query, final Update update, final String collectionName) {
        WriteResult writeResult = mongoTemplate.updateFirst(query, update, collectionName);
        return writeResult.getN();
    }

    @Override
    public int updateMulti(Query query, Update update, String collectionName) {
        WriteResult writeResult = mongoTemplate.updateMulti(query, update, collectionName);
        return writeResult.getN();
    }

    @Override
    public void createCollection(String collectionName) {
        mongoTemplate.createCollection(collectionName);
    }
    
    @Override
    public <T> void remove(Query query, String collectionName) {
        mongoTemplate.remove(query, collectionName);
    }
    
    @Override
    public long count(Query query, String collectionName) {
        return mongoTemplate.count(query, collectionName);
    }
    
    @Override
    public <T> T findAndModify(Query query, Update update, String collectionName, Class<T> clazz) {
        return mongoTemplate.findAndModify(query, update, clazz, collectionName);
    }
}
