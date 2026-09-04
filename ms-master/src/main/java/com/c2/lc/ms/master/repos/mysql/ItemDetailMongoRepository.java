package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.models.ItemDetailModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDetailMongoRepository extends MongoRepository<ItemDetailModel,String> {

    @Query("{ 'cItemCode' : ?0 }")
    ItemDetailModel findByItemCode(String itemCode);

}
