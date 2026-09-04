package com.c2.lc.ms.master.repos.mysql;

import org.springframework.stereotype.Repository;
import com.c2.lc.ms.master.models.EgCustomerCreationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface EgCustomerCreationRepository extends MongoRepository<EgCustomerCreationLog, String> {
}
