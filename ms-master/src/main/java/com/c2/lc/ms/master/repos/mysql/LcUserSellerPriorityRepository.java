package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LcUserSellerPriorityEntity;
import com.c2.lc.ms.master.entities.mysql.LcUserSellerPriorityEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LcUserSellerPriority")
public interface LcUserSellerPriorityRepository extends JpaRepository<LcUserSellerPriorityEntity, LcUserSellerPriorityEntityPK> {

   // @Query("SELECT ls FROM LcUserSellerPriority ls WHERE ls.id.nFirmId = :firmId")
    @Query(value = "SELECT * FROM lc_seller_buyer_priority WHERE n_firm_id = :firmId", nativeQuery = true)
    List<LcUserSellerPriorityEntity> getByFirmId(Long firmId);

}
