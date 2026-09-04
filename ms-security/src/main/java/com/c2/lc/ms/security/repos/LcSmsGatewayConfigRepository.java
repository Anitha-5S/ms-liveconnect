package com.c2.lc.ms.security.repos;

import com.c2.lc.ms.security.entities.LcSmsGatewayConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LcSmsGatewayConfigRepository extends JpaRepository<LcSmsGatewayConfigEntity, Long> {
}
