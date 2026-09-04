package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemMoleculeDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UItemMoleculeDetailsRepository extends JpaRepository<UItemMoleculeDetailsEntity,String> {

    @Query("SELECT u FROM UItemMoleculeDetailsEntity u where u.cItemCode = :cItemCode")
    UItemMoleculeDetailsEntity findByItemCode(@Param("cItemCode") String cItemCode);
}
