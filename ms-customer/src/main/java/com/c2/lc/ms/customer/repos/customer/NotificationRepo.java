package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.NotificationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("NotificationRepository")
public interface NotificationRepo extends JpaRepository<NotificationEntity, Long> {

    @Query("SELECT COUNT(*) FROM NotificationEntity n WHERE n.userId = :userId AND n.status = 'N' ")
    int getCount(Long userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.userId = :userId AND n.status IN ('N', 'R')")
    Page<NotificationEntity> list(Long userId, Pageable page);

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET c_status='R' WHERE n.userId = :userId AND n.status = 'N' " )
    void markAll(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET c_status='D' WHERE n.userId = :userId " )
    void clearAll(@Param("userId") Long userId);
}
