package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.bos.AdminBannerBo;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.TypeWiseBannerModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("TypeWiseBannerRepository")
public interface TypeWiseBannerRepository extends MongoRepository<TypeWiseBannerModel, String> {

    @Query("{c_offer_type : ?0}")
    List<TypeWiseBannerModel> getByOfferType(String offerType, Pageable pageable);

    @Query("{n_banner_temp_id : ?0}")
    TypeWiseBannerModel getTypeWiseBannerByTempId(int bannerTempId);
}
