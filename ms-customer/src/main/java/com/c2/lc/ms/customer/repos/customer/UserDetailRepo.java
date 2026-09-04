package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.UserDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("UserDetailRepository")
public interface UserDetailRepo extends JpaRepository<UserDetailEntity, Long> {

    @Query("SELECT u FROM UserDetailEntity u WHERE u.nUserId = :userId")
    UserDetailEntity getByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT n_contact_id FROM user_detail WHERE n_user_id IN :childUserIdList ",nativeQuery = true)
    List<Long> getContactIdList(List<Long> childUserIdList);
}

