package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustSchemeMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustSchemeMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface CustSchemeMstRepository extends JpaRepository<CustSchemeMstEntity, CustSchemeMstEntityPK> {

    @Query("SELECT c FROM CustSchemeMstEntity c WHERE c.cC2Code = :cC2Code AND c.cItemCode = :cItemCode AND c.dLdate = :dLdate")
    CustSchemeMstEntity findByCustItemCode(@Param("cC2Code") String cC2Code,
                                           @Param("cItemCode") String cItemCode,
                                           @Param("dLdate") Date dLdate);

    @Query("SELECT max(c.dLdate) FROM CustSchemeMstEntity c WHERE c.cC2Code = :cC2Code AND c.cItemCode = :cItemCode")
    Date getMaxDate(@Param("cC2Code") String cC2Code, @Param("cItemCode") String cItemCode);
}
