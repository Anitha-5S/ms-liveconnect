package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustCategoryMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustCategoryMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustCategoryMstRepository extends JpaRepository<CustCategoryMstEntity, CustCategoryMstEntityPK> {

    @Query("SELECT c FROM CustCategoryMstEntity c WHERE c.cC2Code = :cC2Code AND c.cCode = :cCode")
    CustCategoryMstEntity findByCustCatCode(@Param("cC2Code") String cC2Code,@Param("cCode") String cCode);
}
