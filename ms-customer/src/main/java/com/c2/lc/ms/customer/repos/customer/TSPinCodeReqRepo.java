package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.TSPinCodeReqEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("TSPinCodeReqRepo")
public interface TSPinCodeReqRepo extends JpaRepository<TSPinCodeReqEntity, Long> {

    @Query(value = "SELECT DISTINCT p.c_pincode , MAX(p.t_created_at) " +
            "   FROM ts_pincode_wise_request p " +
            "   WHERE p.c_c2code = :c2Code " +
            "   GROUP BY p.c_pincode " +
            "   ORDER BY MAX(p.t_created_at) DESC ", nativeQuery = true)
    List<Object[]> getAllReq(String c2Code, Pageable pageable);

    @Query("SELECT p FROM TSPinCodeReqEntity p WHERE p.c2Code = :c2Code AND p.serviceActiveStatus = 'Y' ")
    List<TSPinCodeReqEntity> getServicePin(String c2Code, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update ts_pincode_wise_request set c_active_status = :status where c_c2code = :c2Code and c_pincode = :pin ", nativeQuery = true)
    void activatePinCodeReq(String c2Code, String pin, String status);

    @Query("SELECT p FROM TSPinCodeReqEntity p WHERE p.c2Code = :c2Code AND p.cPin = :pin ")
    List<TSPinCodeReqEntity> getByPin(String c2Code, String pin);

    @Query("SELECT p FROM TSPinCodeReqEntity p WHERE p.c2Code = :c2Code AND p.cPin = :pinCode ")
    List<TSPinCodeReqEntity> getByPin(String c2Code, String pinCode, Pageable pageable);

    @Query(value = "SELECT c_active_status " +
            "   FROM ts_pincode_wise_request tpwr WHERE c_pincode = :pinCode " +
            "   ORDER BY t_created_at DESC ", nativeQuery = true)
    List<Object[]> getStatusByPinCode(String pinCode);

    @Query("SELECT p FROM TSPinCodeReqEntity p WHERE p.c2Code = :c2Code AND p.serviceActiveStatus = 'Y' ")
    List<TSPinCodeReqEntity> getServicePin(String c2Code);

    @Query("SELECT p FROM TSPinCodeReqEntity p WHERE p.c2Code = :c2Code ")
    List<TSPinCodeReqEntity> getAllReqCount(String c2Code);
}
