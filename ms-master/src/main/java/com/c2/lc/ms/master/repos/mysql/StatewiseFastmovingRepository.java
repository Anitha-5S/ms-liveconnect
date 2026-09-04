package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UStatewiseFastmovingItemsEntity;
import com.c2.lc.ms.master.entities.mysql.UStatewiseFastmovingItemsEntityPK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "StatewiseFastmovingRepository")
public interface StatewiseFastmovingRepository  extends JpaRepository<UStatewiseFastmovingItemsEntity, UStatewiseFastmovingItemsEntityPK> {

    /*@Query("SELECT u.cUcode, uim.cName, uipm.cName, uicm.cName, uim.nLastMrp, uim.cWebImgLink FROM UStatewiseFastmovingItemsEntity u" +
            " JOIN UItemMstEntity uim ON uim.cCode = u.cUcode" +
            " JOIN UItemPackMstEntity uipm ON uim.cPackTypeCode = uipm.cCode "+
            " JOIN UItemContMstEntity uicm ON uim.cItemContCode = uicm.cCode "+
            " where u.cStateCode = :cStateCode ORDER BY u.nCount DESC")
    String getUcodeByStateCodeQuery(@Param("cStateCode") String cStateCode);*/

   /* @Query("SELECT usfi.cUcode FROM UStatewiseFastmovingItemsEntity usfi" +
            "WHERE usfi.c_state_code = :state" +
            "ORDER BY n_count DESC, n_qty DESC")
    List<String> getUcode(String state, Pageable page);

    @Query("SELECT usfi.cUcode FROM UStatewiseFastmovingItemsEntity usfi" +
            "WHERE usfi.c_state_code = :state" +
            "ORDER BY n_count DESC, n_qty DESC")
    List<String> findItemCode(String state);*/

    @Query("SELECT u.cUcode FROM UStatewiseFastmovingItemsEntity u" +
            " JOIN UItemMstEntity uim ON uim.cCode = u.cUcode" +
            " where u.cStateCode = :cStateCode")
    List<String> getUcodeByStateCode(@Param("cStateCode") String cStateCode);

   /* @Query("SELECT COUNT(u.c_ucode) FROM UStatewiseFastmovingItemsEntity u" +
            " JOIN UItemMstEntity uim ON uim.cCode = u.cUcode" +
            " where u.cStateCode = :cStateCode")
    int getCountUcodeByStateCode(@Param("cStateCode") String cStateCode);*/



}
