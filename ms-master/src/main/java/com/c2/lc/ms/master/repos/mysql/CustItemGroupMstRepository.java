package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustItemGroupMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustItemGroupMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustItemGroupMstRepository extends JpaRepository<CustItemGroupMstEntity, CustItemGroupMstEntityPK> {
}
