package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustContMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustContMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustContMstRepository extends JpaRepository<CustContMstEntity, CustContMstEntityPK> {
}
