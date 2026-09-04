package com.c2.lc.ms.user.repos;

import com.c2.lc.ms.user.entities.FirmRoleLockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FirmRoleLockRepo extends JpaRepository<FirmRoleLockEntity, Long> {

    @Query("SELECT fr FROM FirmRoleLockEntity fr WHERE fr.cMobileNo = :mobile" +
            " AND fr.cC2Code = :c2Code AND fr.cActCode = :actCode")
    FirmRoleLockEntity getExists(@Param("mobile") String mobileNo, @Param("c2Code") String c2Code,
                             @Param("actCode") String actCode);
}
