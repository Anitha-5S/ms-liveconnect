package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UGeoDistrictMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<UGeoDistrictMstEntity, String> {

    @Query("SELECT d FROM UGeoDistrictMstEntity d WHERE d.cGeoStateCode = :stateCode")
    List<UGeoDistrictMstEntity> findByStateCode(@Param("stateCode") String stateCode);
}
