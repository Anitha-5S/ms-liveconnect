package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.DefaultImageUrl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("DefaultImageRepository")
public interface DefaultImageRepository extends MongoRepository<DefaultImageUrl , String> {
}
