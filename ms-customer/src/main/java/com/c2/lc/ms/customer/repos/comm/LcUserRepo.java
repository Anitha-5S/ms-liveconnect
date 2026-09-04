package com.c2.lc.ms.customer.repos.comm;

import com.c2.lc.ms.customer.entities.comm.LcUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository("LcUserRepository")
public interface LcUserRepo extends JpaRepository<LcUser, Long> {

    @Query("SELECT lc FROM LcUser lc WHERE lc.mobileNumber = :mobile AND lc.type = 'B'")
    LcUser findByMobileNumber(@Param("mobile") String mobileNo);

    @Query("SELECT lc FROM LcUser lc WHERE lc.mobileNumber = :mobile")
   List<LcUser> findByMobile(@Param("mobile") String mobileNo);

    @Query("SELECT lc FROM LcUser lc WHERE lc.mobileNumber = :cMobileNo AND lc.type = :cType")
    LcUser getByMobileAndType(String cMobileNo, String cType);

    @Transactional
    @Modifying
    @Query("update LcUser lc set lc.mobileNumber = :c_mobile_no where nId = :userId")
    void updateMobileByUserId(@Param("userId") Long userId, @Param("c_mobile_no") String c_mobile_no);

    @Query("SELECT lc FROM LcUser lc WHERE lc.mobileNumber = :cMobileNo AND lc.c2Code = :c2Code AND lc.type = :type")
    LcUser checkUserExist(String cMobileNo, String c2Code, String type);

    @Query("SELECT lc FROM LcUser lc WHERE lc.c2Code = :c2Code AND lc.brCode = :brCode" +
            " AND lc.tCreatedAt >= :fromDate AND lc.tCreatedAt <= :toDate ")
    List<LcUser> getByC2Code(String c2Code, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("SELECT lc FROM LcUser lc WHERE lc.c2Code = :c2Code AND lc.brCode = :brCode " +
            " AND lc.tCreatedAt >= :fromDate AND lc.tCreatedAt <= :toDate ")
    List<LcUser> getByC2CodeBrCode(String c2Code, String brCode, LocalDateTime fromDate, LocalDateTime toDate);

    @Query("SELECT lc FROM LcUser lc WHERE lc.c2Code = :c2Code ORDER BY tCreatedAt DESC ")
    List<LcUser> getAllByC2Code(String c2Code, Pageable pageable);

}
