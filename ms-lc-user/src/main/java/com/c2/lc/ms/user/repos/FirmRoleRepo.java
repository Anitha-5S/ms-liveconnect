package com.c2.lc.ms.user.repos;
import com.c2.lc.ms.user.entities.FirmRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FirmRoleRepo")
public interface FirmRoleRepo extends JpaRepository<FirmRoleEntity, Long> {

    @Query("SELECT fr FROM FirmRoleEntity fr WHERE fr.cMobileNo = :mobile" +
            " AND fr.cC2Code = :c2Code AND fr.cActCode = :actCode AND fr.cRoleType = :type")
    FirmRoleEntity getExists(@Param("mobile") String mobileNo,@Param("c2Code") String c2Code,
                             @Param("actCode") String actCode, @Param("type") String type);


    @Query("SELECT fr FROM FirmRoleEntity fr" +
            "  LEFT JOIN FirmRoleLockEntity frl on frl.cMobileNo = fr.cMobileNo " +
            "  and frl.cC2Code = fr.cC2Code and frl.cActCode = fr.cActCode " +
            "  where fr.cMobileNo = :mobile and fr.cC2Code = :c2Code and fr.cActCode = :actCode" +
            "  and lower(fr.cRoleType) <> lower(frl.cRoleLock)")
    List<FirmRoleEntity> getNotInLock(@Param("mobile") String mobileNo,@Param("c2Code") String c2Code,
                                      @Param("actCode") String actCode);

    
}
