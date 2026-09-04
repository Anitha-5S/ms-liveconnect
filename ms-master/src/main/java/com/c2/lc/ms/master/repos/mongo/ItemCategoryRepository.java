package com.c2.lc.ms.master.repos.mongo;


import com.c2.lc.ms.master.entities.mongo.ItemCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("ItemCategoryRepository")
public interface ItemCategoryRepository extends MongoRepository<ItemCategory, String> {
}
