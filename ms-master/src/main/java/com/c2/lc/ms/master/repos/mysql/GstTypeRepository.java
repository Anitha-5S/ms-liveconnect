package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LoGstTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GstTypeRepository extends JpaRepository<LoGstTypeEntity, Long> {
}
