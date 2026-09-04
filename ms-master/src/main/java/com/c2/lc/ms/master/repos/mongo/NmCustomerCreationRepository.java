package com.c2.lc.ms.master.repos.mongo;

import org.springframework.stereotype.Repository;
import com.c2.lc.ms.master.models.NmCustomerCreationLog;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface NmCustomerCreationRepository extends MongoRepository<NmCustomerCreationLog, String> {

}
