package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("ContactDetailRepository")
public interface ContactDetailRepo extends JpaRepository<ContactDetailEntity, Long> {

    @Query("SELECT cd FROM ContactDetailEntity cd WHERE cd.cMobileNo = :mobileNo")
    ContactDetailEntity getByMobileNo(@Param("mobileNo") String mobileNo);

    @Query("SELECT cd FROM ContactDetailEntity cd WHERE cd.cMobileNo = :mobileNo")
    List<ContactDetailEntity> doesExistMobileNo(@Param("mobileNo") String mobileNo);

    @Query("SELECT cd FROM ContactDetailEntity cd WHERE cd.nUserId = :userId")
    List<ContactDetailEntity> getByUserId(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("update ContactDetailEntity cd set cd.cMobileNo = :c_mobile_no where nContactId = :nContactId")
    void updateMobileByContactId(@Param("nContactId") Long nContactId, @Param("c_mobile_no") String c_mobile_no);

    @Query("SELECT cd FROM ContactDetailEntity cd WHERE cd.nContactId = :addressId")
    ContactDetailEntity getByContactId(@Param("addressId") long addressId);

    @Transactional
    @Modifying
    @Query("update ContactDetailEntity cd set cd.cDeliveryAddressStatus = :setDelivery where nUserId = :userId")
    void UpdateByUserId(@Param("userId") Long userId, @Param("setDelivery") String setDeliveryStatus);

    @Query("SELECT cAddressType FROM ContactDetailEntity cd WHERE cd.nUserId = :userId")
    List<String> getAddrTypeByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT c_mobile_no FROM contact_detail  WHERE n_contact_id IN :contactIdList ",nativeQuery = true)
    List<String> getAllMobileNumber(List<Long> contactIdList);
}
