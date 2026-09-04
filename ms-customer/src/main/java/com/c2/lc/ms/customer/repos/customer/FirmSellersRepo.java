package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.FirmSellersEntity;
import com.c2.lc.ms.customer.entities.customer.pk.FirmSellersPKEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FirmSellersRepo")
public interface FirmSellersRepo extends JpaRepository<FirmSellersEntity, FirmSellersPKEntity> {

    @Query("SELECT fs FROM FirmSellersEntity fs WHERE fs.id.nFirmId = :firmId")
    List<FirmSellersEntity> getByFirmId(Long firmId);

    @Query(value = " select * from firm_sellers fs2  ", nativeQuery = true)
    List<FirmSellersEntity> getFirmSellers(Pageable pageable);

    @Query("SELECT fs FROM FirmSellersEntity fs WHERE fs.cSellerCode = :seller AND  fs.cBuyerCode = :buyer")
    List<FirmSellersEntity> getBySellerBuyer(String seller, String buyer);
}

