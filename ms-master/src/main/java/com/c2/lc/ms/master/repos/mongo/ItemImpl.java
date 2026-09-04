package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.LcItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemImpl {

    @Autowired private MongoTemplate mongoTemplate;

    public LcItem save(LcItem lcItem){
        return mongoTemplate.save(lcItem);
    }

    public LcItem getById(String id){
        Query query=new Query();
        query.addCriteria(Criteria.where("itemCode").is(id));
        return mongoTemplate.findOne(query, LcItem.class);
    }
    public List<LcItem>searchByName(String name){
        Query query=new Query();
        query.addCriteria(Criteria.where("itemName").regex(name));
        return mongoTemplate.find(query, LcItem.class);
    }
}
