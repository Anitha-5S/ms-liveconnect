package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.HsnGstMappingEntity;
import com.c2.lc.ms.master.entities.mysql.HsnGstMappingEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HsnGstMappingRepository extends JpaRepository<HsnGstMappingEntity, HsnGstMappingEntityPK> {

    @Query("SELECT h FROM HsnGstMappingEntity h WHERE h.cHsnSacCode = :cHsnSacCode and h.cGstCode != '00'")
    HsnGstMappingEntity findByHsnCode(@Param("cHsnSacCode") String cHsnSacCode);
}
