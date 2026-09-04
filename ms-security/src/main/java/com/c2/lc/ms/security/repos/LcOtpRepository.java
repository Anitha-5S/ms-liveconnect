package com.c2.lc.ms.security.repos;

import com.c2.lc.ms.security.entities.LcOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository("LcOtpRepository")
public interface LcOtpRepository extends JpaRepository<LcOtpEntity, String> {

    @Query(value = "SELECT p FROM LcOtpEntity p WHERE p.mobileNumber = :c_mobile_no")
    LcOtpEntity findByMobileNo(String c_mobile_no);
}
