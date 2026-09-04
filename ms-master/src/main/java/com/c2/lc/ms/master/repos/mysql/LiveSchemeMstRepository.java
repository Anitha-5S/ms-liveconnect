package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LiveSchemeMstEntity;
import com.c2.lc.ms.master.entities.mysql.LiveSchemeMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "LiveSchemeMstRepository")
public interface LiveSchemeMstRepository extends JpaRepository<LiveSchemeMstEntity, LiveSchemeMstEntityPK> {

    @Query("SELECT l FROM LiveSchemeMstEntity l WHERE l.cFirmCode = :cFirmCode AND l.cItemCode = :cItemCode")
    LiveSchemeMstEntity findByFirmItemCode(@Param("cFirmCode") String cFirmCode,
                                           @Param("cItemCode") String cItemCode
                                           );
}
