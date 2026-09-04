package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.UserOwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserOwnerRepo")
public interface UserOwnerRepo extends JpaRepository<UserOwnerEntity, Long> {

    @Query("SELECT uo FROM UserOwnerEntity uo WHERE uo.childUser = :userId")
    UserOwnerEntity getParent(Long userId);

    @Query("SELECT uo FROM UserOwnerEntity uo WHERE uo.parentUser = :parentId")
    List<UserOwnerEntity> getAllChildUserId(Long parentId);
}

