package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UItemPriceRepository extends JpaRepository<UItemPriceEntity,String> {

    @Query("SELECT u FROM UItemPriceEntity u where u.cCode = :cCode")
    UItemPriceEntity findByItemCode(@Param("cCode") String cCode);
}
