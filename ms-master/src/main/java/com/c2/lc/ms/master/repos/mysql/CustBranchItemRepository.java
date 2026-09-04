package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.CustBranchItemStockEntity;
import com.c2.lc.ms.master.entities.mysql.CustBranchItemStockEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustBranchItemRepository extends JpaRepository<CustBranchItemStockEntity, CustBranchItemStockEntityPK> {

   /* @Query("SELECT lccm.cCode, " +
            "    lccm.cName, " +
            "    usi.cStockiestItemCode, " +
            "    cbis.nRate, " +
            "    cbis.nSaleRate, " +
            "    'NA', " +
            "    sum(COALESCE(cbis.nBalQty, 0)), " +
            "    0.00 " +
            "FROM CustBranchItemStockEntity cbis " +
            "    JOIN UStockiestItemEntity usi ON usi.cStockiestItemCode = cbis.cItemCode " +
            "    AND usi.cStockiestCode = cbis.cC2Code " +
            "    JOIN LcC2CodeMstEntity lccm ON lccm.cCode = usi.cStockiestCode " +
            "WHERE usi.cUcode = :cUcode " +
            "    AND cbis.nBalQty > 0 " +
            "    and lccm.nOrderFlag = 1 " +
            "GROUP BY lccm.cCode, " +
            "    lccm.cName, " +
            "    usi.cStockiestItemCode, " +
            "    cbis.nRate, " +
            "    cbis.nSaleRate " +
            "ORDER BY cbis.nSaleRate desc" )
    //List<SellerItemListInterface> findByUcode(@Param("cUcode") String cUcode, Pageable pageable);
    List<SellerItemList> findByUcode(@Param("cUcode") String cUcode);*/


}
