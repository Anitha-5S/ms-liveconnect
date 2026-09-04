package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface LcItemRepository extends MongoRepository<LcItem, String> {

    @Query("{c_mfg_code : ?0}")
    List<LcItem> getItemByManufactureCode(String mCode);

    @Query("{c_bar_code : ?0}")
    LcItem getByBarCode(String mCode);

    @Query("{_id : ?0}")
    LcItem getById(String itemCode);

}
