package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.models.ItemSummaryModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSummaryRepository extends MongoRepository<ItemSummaryModel,String> {

    @Query("{ 'cItemCode' : ?0 }")
    ItemSummaryModel findByItemCode(String itemCode);

}
