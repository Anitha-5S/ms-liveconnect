package com.c2.lc.ms.master.repos.mysql;


import com.c2.lc.ms.master.entities.mysql.CustScheduleMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustScheduleMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustScheduleMstRepository extends JpaRepository<CustScheduleMstEntity, CustScheduleMstEntityPK> {
}
