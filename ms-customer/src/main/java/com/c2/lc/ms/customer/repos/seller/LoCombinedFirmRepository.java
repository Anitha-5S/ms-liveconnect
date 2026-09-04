package com.c2.lc.ms.customer.repos.seller;

import com.c2.lc.ms.customer.entities.seller.LoCombinedFirmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface LoCombinedFirmRepository extends JpaRepository<LoCombinedFirmEntity, String> {

    @Query("SELECT lo FROM LoCombinedFirmEntity lo WHERE lo.cMobile = :mobileNo")
    List<LoCombinedFirmEntity> findByMobile(String mobileNo);

    @Modifying
    @Transactional
    @Query(value = "delete lo.* from lo_combined_firm_temp lo where lo.c_mobile_no = :mobileNo", nativeQuery = true)
    void deleteByMobile(String mobileNo);
}
