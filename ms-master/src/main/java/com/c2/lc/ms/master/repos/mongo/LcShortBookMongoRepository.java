package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.LcShortBook;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LcShortBookMongoRepository")
public interface LcShortBookMongoRepository extends MongoRepository<LcShortBook, Object> {

    @Query("{c_item_code : ?0, c_br_code : ?1,l_user_id:?2,l_firm_id: ?3}")
    LcShortBook getByItemAndBranch(String itemCode, String brCode, long userId, long firmId);

    @Query("{c_br_code : ?0,l_user_id:?1,l_firm_id: ?2}")
    List<LcShortBook> getByBranch(String brCode, long userId, long firmId);

}
