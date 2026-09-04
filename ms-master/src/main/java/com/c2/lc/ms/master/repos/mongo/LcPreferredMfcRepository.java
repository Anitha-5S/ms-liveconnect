package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.LcPreferredMfc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LcPreferredMfcRepository extends MongoRepository<LcPreferredMfc, String> {

    @Query("{c_state_code : ?0}")
    List<LcPreferredMfc> preferredMfc(String stateCode, Pageable pageable);

    @Query(value = "{c_state_code : ?0}",count = true)
    long preferredMfcCount(String stateCode);
}
