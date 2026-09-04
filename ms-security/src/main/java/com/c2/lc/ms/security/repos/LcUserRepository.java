package com.c2.lc.ms.security.repos;

import com.c2.lc.ms.security.entities.LcUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//TODO replace findBy with Query
@Repository("LcUserRepository")
public interface LcUserRepository extends JpaRepository<LcUserEntity, Long> {

    @Query("SELECT lc FROM LcUserEntity lc WHERE lc.mobileNumber = :mobile AND lc.type = :type")
    LcUserEntity findByMobileNumberAndType(@Param("mobile") String mobileNo,@Param("type") String type);

    LcUserEntity findByMobileNumberAndPasswordAndType(String mobileNo, String password, String type);

}
