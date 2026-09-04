package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemMfacMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemMfacRepository extends JpaRepository<UItemMfacMstEntity,String> {

    @Query("SELECT u FROM UItemMfacMstEntity u where u.cCode = :cCode")
    UItemMfacMstEntity findByMfacCode(@Param("cCode") String cCode);

    @Query("SELECT u FROM UItemMfacMstEntity u")
    List<UItemMfacMstEntity> findAllMfacs();


    Optional<UItemMfacMstEntity> findById(String mfacCode);
}
