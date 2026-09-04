package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.PincodeMstEntity;
import com.c2.lc.ms.master.entities.mysql.PincodeMstEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "PincodeMstRepository")
public interface PincodeMstRepository extends JpaRepository<PincodeMstEntity, PincodeMstEntityPK> {

    @Query("SELECT u.cStateCode FROM PincodeMstEntity u where u.cCode = :cCode")
    String getStateCodePinCode(@Param("cCode") String cCode);
}
