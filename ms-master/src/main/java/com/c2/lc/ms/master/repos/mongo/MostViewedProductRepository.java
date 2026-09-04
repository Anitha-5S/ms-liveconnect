package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.MostViewedProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("MostViewedProductRepository")
public interface MostViewedProductRepository extends MongoRepository<MostViewedProduct,String> {
}
