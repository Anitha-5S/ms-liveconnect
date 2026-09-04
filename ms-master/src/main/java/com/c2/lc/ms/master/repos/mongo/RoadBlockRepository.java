package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.RoadBlock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("RoadBlockRepository")
public interface RoadBlockRepository extends MongoRepository<RoadBlock, String> {

    @Query("{_id : ?0}")
    RoadBlock getById(String id);

    @Query("{c_city:?0}")
    List<RoadBlock> getByCity(String city);

    @Query("{c_state:?0}")
    List<RoadBlock> getByState(String state);

}
