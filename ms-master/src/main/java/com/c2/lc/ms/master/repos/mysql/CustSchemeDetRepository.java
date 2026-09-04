package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustSchemeDetEntity;
import com.c2.lc.ms.master.entities.mysql.CustSchemeDetEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustSchemeDetRepository extends JpaRepository<CustSchemeDetEntity, CustSchemeDetEntityPK> {

    @Query("SELECT c FROM CustSchemeDetEntity c WHERE c.cC2Code = :cC2Code AND c.cItemCode = :cItemCode AND c.cCategory = :cCategory")
    CustSchemeDetEntity findByItemCustCatCode(@Param("cC2Code") String cC2Code,
                                          @Param("cItemCode") String cItemCode,
                                          @Param("cCategory") String cCategory);
}
