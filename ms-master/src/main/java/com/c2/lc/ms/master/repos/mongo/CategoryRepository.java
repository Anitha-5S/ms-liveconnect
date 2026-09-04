package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("CategoryRepository")
public interface CategoryRepository extends MongoRepository<Category, String> {

}
