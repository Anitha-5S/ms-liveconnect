package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustItemCategoryMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustItemCategorySubMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustItemCategoryMstRepository extends JpaRepository<CustItemCategoryMstEntity, CustItemCategorySubMstEntityPK> {

    @Query("SELECT COUNT(distinct c1.cCode) FROM CustItemCategoryMstEntity c1 WHERE c1.c2Code = :c2Code")
    long getCountByC2Code(@Param("c2Code") String c2Code);
}
