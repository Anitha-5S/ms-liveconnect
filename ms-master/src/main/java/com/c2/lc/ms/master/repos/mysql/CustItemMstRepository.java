package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustItemMstEntity;
import com.c2.lc.ms.master.entities.mysql.CustItemMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustItemMstRepository extends JpaRepository<CustItemMstEntity, CustItemMstEntityPK>

{
    @Query(value = "SELECT * FROM cust_item_mst c WHERE c_c2code = :cC2Code  , c_code = :cCode " +
            "AND br_code = :brCode",
    nativeQuery = true)
    Optional<CustItemMstEntity> findByC2CodeAndCCodeAndBrCode(@Param("cC2Code") String cC2Code,
                                                     @Param("cCode") String cCode,
                                                              @Param("brCode") String brcode);
}
