package com.c2.lc.ms.customer.repos.seller;
import com.c2.lc.ms.customer.entities.customer.FirmSellersEntity;
import com.c2.lc.ms.customer.entities.seller.LcSellerBuyerPriorityEntity;
import com.c2.lc.ms.customer.entities.seller.pk.LcUserSellerPriorityEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("LcUserSellerPriority")
public interface LcUserSellerPriority extends JpaRepository<LcSellerBuyerPriorityEntity, LcUserSellerPriorityEntityPK> {

   // @Query("SELECT ls FROM LcUserSellerPriority ls WHERE ls.id.nFirmId = :firmId")
    @Query(value = "SELECT * FROM lc_seller_buyer_priority WHERE n_firm_id = :firmId", nativeQuery = true)
    List<LcSellerBuyerPriorityEntity> getByFirmId(Long firmId);

}
