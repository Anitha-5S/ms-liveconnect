package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemMfacMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerRepository extends JpaRepository<UItemMfacMstEntity,String> {

    @Query("SELECT u FROM UItemMfacMstEntity u where u.cCode = :cCode")
    UItemMfacMstEntity findByMfacCode(@Param("cCode") String cCode);
}
