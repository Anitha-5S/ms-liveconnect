package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemMstEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface ItemRepository extends JpaRepository<UItemMstEntity,String> {

    //    @Cacheable(value= MasterConstants.ITEM_MST,key="'Item-'+#cCode")
    @Query("SELECT u FROM UItemMstEntity u where u.cCode = :cCode")
    UItemMstEntity findByItemCode(@Param("cCode") String cCode);

    // @Cacheable(value=MasterConstants.ITEM_MST,key="'Item-'+'All'")
    @Query("SELECT u FROM UItemMstEntity u")
    List<UItemMstEntity> findAllItems();

    @Query("SELECT u.cCode FROM UItemMstEntity u where u.cItemMfacCode = :cItemMfacCode")
    List<String> findByMfacCode(@Param("cItemMfacCode") String cItemMfacCode,Pageable pageable);

    @Query("SELECT u.cCode FROM UItemMstEntity u where u.cItemContCode = :cItemContCode")
    List<String> findByContCode(@Param("cItemContCode") String cItemContCode, Pageable pageable);

    // @Query("SELECT u.cCode FROM UItemMstEntity u WHERE u.dAdate >= :dAdate  AND u.cCode NOT IN (SELECT lcb.cItemCode from  LcBlockedEntity lcb)")
    @Query("SELECT u.cCode FROM UItemMstEntity u WHERE u.dAdate >= :dAdate")
    List<String> findByCreatedDate( LocalDate dAdate, Pageable pageable);

}
