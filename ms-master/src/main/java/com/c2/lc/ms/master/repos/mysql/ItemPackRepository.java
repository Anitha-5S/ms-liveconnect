package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemPackMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPackRepository extends JpaRepository<UItemPackMstEntity,String> {

    @Query("SELECT u FROM UItemPackMstEntity u where u.cCode = :cCode")
    UItemPackMstEntity findByPackCode(@Param("cCode") String cCode);

    @Query("SELECT u FROM UItemPackMstEntity u")
    List<UItemPackMstEntity> findAllPacks();
}
