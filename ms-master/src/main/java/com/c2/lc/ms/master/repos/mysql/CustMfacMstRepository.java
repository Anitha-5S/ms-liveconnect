package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustMfacMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustMfacMstEntityPK;
import com.c2.lc.ms.master.entities.mysql.CustPackMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustPackMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustMfacMstRepository extends JpaRepository<CustMfacMstEntity, CustMfacMstEntityPK> {
}
