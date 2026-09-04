package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UGeoCityMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<UGeoCityMstEntity, String> {


    @Query("SELECT c FROM UGeoCityMstEntity c WHERE c.cGeoDistrictCode = :districtCode")
    List<UGeoCityMstEntity> findByDistrictCode(@Param("districtCode") String districtCode);

    @Query(value = "SELECT * FROM u_geo_city_mst ugcm " +
            " INNER JOIN  u_geo_district_mst ugdm ON ugdm.c_code = ugcm.c_geo_district_code " +
            " INNER JOIN  u_geo_state_mst ugsm ON ugsm.c_code  = ugdm.c_geo_state_code " +
            " WHERE ugsm.c_code = :stateCode",
            nativeQuery = true)
    List<UGeoCityMstEntity> findByStateCode(@Param("stateCode") String stateCode);
}
