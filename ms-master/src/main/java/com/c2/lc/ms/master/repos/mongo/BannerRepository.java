package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.bos.AdminBannerBo;
import com.c2.lc.ms.master.models.BannerModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("BannerRepository")
public interface BannerRepository extends MongoRepository<BannerModel, String> {

    @Query("{_id : ?0}")
    BannerModel getById(String id);

    @Query("{c_city:?0}")
    List<BannerModel> getByCity(String city);

    @Query("{c_state:?0}")
    List<BannerModel> getByState(String state);

    @Query("{c_c2code:?0, c_offer_type : ?1}")
    List<BannerModel> findByC2Code(String c2code, String offerType);

    @Query("{c_c2code:?0}")
    List<BannerModel> findByC2Code(String c2code);

    @Query("{c_c2code:?0}")
    List<BannerModel> findByC2Code(String c2code, Pageable pageable);

    @Query("{_id : ?0}")
    AdminBannerBo getBannerById(String bannerId);

    @Query("{c_offer_type : ?0, c_c2code : ?1}")
    BannerModel findByOfferType(String offerType, String c2Code);

    @Query("{c_c2code:?0, n_banner_status:1}")
    List<BannerModel> findByC2CodeAndActive(String c2code);
}
