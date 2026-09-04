package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.LcManufacture;
import com.c2.lc.ms.master.models.BannerModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManufactureRepository extends MongoRepository<LcManufacture, String> {
}
