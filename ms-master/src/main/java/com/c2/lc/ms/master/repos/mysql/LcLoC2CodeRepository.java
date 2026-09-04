package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LcLoC2CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "LcLoC2CodeRepository")
public interface LcLoC2CodeRepository extends JpaRepository<LcLoC2CodeEntity,String> {

    @Query("SELECT l FROM LcLoC2CodeEntity l WHERE l.cC2Code = :cC2Code")
    LcLoC2CodeEntity findByC2Code(@Param("cC2Code") String cC2Code);
}
