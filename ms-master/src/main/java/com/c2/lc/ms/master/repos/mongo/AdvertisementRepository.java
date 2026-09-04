package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.models.Advertisement;
import com.c2.lc.ms.master.models.BannerModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("AdvertisementRepository")
public interface AdvertisementRepository extends MongoRepository<Advertisement, String> {

    @Query("{_id : ?0}")
    Advertisement getById(String id);

    @Query("{c_city:?0}")
    List<Advertisement> getByCity(String city);

    @Query("{c_state:?0}")
    List<Advertisement> getByState(String state);

}