package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.bos.ItemsBO;
import com.c2.lc.ms.master.entities.mysql.UStockiestItemEntity;
import com.c2.lc.ms.master.entities.mysql.UStockiestItemEntityPK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository(value = "UStockiestItemRepository")
public interface UStockiestItemRepository extends JpaRepository<UStockiestItemEntity, UStockiestItemEntityPK> {

    @Query(value = "SELECT * FROM u_stockiest_item  WHERE c_stockiest_code = :sellerC2Code and c_stockiest_item_code= :itemCode",
            nativeQuery = true)
    List<UStockiestItemEntity> findByC2codeAndItemCode(@Param("sellerC2Code") String sellerC2Code, @Param("itemCode") String itemCode);
}
