package com.c2.lc.ms.user.repos;


import com.c2.lc.ms.user.entities.UserFirmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("UserFirmRepo")
public interface UserFirmRepo extends JpaRepository<UserFirmEntity, Long> {

    @Query("SELECT fr FROM UserFirmEntity fr WHERE fr.cMobileNo = :mobile AND fr.cPassword = :pass")
    UserFirmEntity getExists(@Param("mobile") String mobileNo, @Param("pass") String pass);

}

