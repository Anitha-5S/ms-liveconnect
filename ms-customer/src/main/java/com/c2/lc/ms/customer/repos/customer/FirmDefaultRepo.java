package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.FirmDefaultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("FirmDefaultRepository")
public interface FirmDefaultRepo extends JpaRepository<FirmDefaultEntity, Long> {
}
