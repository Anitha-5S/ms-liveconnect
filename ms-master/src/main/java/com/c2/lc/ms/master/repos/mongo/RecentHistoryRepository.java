package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.RecentHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("RecentHistoryRepository")
public interface RecentHistoryRepository extends MongoRepository<RecentHistory, String> {
}
