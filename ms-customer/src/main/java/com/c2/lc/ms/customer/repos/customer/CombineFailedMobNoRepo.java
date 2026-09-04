package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.CombineFailedMobileNoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CombineFailedMobNoRepo")
public interface CombineFailedMobNoRepo extends JpaRepository<CombineFailedMobileNoEntity, Long> {
}
