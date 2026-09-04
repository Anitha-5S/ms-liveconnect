package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UItemContMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<UItemContMstEntity,String> {

    @Query("SELECT u FROM UItemContMstEntity u where u.cCode = :cCode")
    UItemContMstEntity findByContCode(@Param("cCode") String cCode);
}
