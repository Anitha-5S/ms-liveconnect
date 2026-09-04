package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemBrandMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemBrandRepository extends JpaRepository<UItemBrandMstEntity,String> {

    @Query("SELECT u FROM UItemBrandMstEntity u where u.cCode = :cCode")
    UItemBrandMstEntity findByBrandCode(@Param("cCode") String cCode);

    @Query("SELECT u FROM UItemBrandMstEntity u")
    List<UItemBrandMstEntity> findAllBrands();
}
