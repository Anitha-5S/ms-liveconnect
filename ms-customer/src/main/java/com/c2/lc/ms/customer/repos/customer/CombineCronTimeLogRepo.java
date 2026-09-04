package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.CombineCronTimeLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("CombineCronTimeLogRepo")
public interface CombineCronTimeLogRepo extends JpaRepository<CombineCronTimeLogEntity, Long> {
}
