package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustBrandMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustBrandMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustBrandMstRepository extends JpaRepository<CustBrandMstEntity, CustBrandMstEntityPK> {
}
