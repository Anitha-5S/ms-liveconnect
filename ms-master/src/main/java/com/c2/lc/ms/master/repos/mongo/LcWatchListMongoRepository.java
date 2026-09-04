package com.c2.lc.ms.master.repos.mongo;
import com.c2.lc.ms.master.entities.mongo.LcShortBook;
import com.c2.lc.ms.master.entities.mongo.LcWatchList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LcWatchListMongoRepository")
public interface LcWatchListMongoRepository extends MongoRepository<LcWatchList, String> {

    @Query("{c_item_code : ?0, c_br_code : ?1,l_user_id:?2,l_firm_id: ?3}")
    LcWatchList getByItemAndBranch(String itemCode, String brCode, long userId, long firmId);

    @Query("{c_br_code : ?0,l_user_id:?1,l_firm_id: ?2}")
    List<LcWatchList> getByBranch(String brCode, long userId, long firmId);

}
