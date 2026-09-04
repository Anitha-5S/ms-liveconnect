package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustActDetEntity;
import com.c2.lc.ms.master.entities.mysql.CustActDetEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustActDetRepository extends JpaRepository<CustActDetEntity, CustActDetEntityPK> { }
