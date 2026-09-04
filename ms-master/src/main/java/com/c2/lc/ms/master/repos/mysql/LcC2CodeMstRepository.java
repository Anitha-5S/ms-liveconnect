package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LcC2CodeMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LcC2CodeMstRepository extends JpaRepository<LcC2CodeMstEntity,String> {

    @Query("SELECT l FROM LcC2CodeMstEntity l where l.cCode = :cCode")
    LcC2CodeMstEntity findBySellerCode(@Param("cCode") String cCode);
}
