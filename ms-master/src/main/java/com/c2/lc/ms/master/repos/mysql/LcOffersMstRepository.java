package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.LcC2CodeMstEntity;
import com.c2.lc.ms.master.entities.mysql.LcOfferMstEntity;
import com.c2.lc.ms.master.entities.mysql.LcOfferMstEntityPK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LcOffersMstRepository extends JpaRepository<LcOfferMstEntity, LcOfferMstEntityPK> {

    @Query("SELECT l FROM LcOfferMstEntity l " +
            "WHERE :currentDate >= l.startDate AND :currentDate <= l.endDate " +
            "ORDER BY l.tCreatedAt ")
    List<LcOfferMstEntity> findAvailableOffers(@Param("currentDate") LocalDateTime currentDate, Pageable page);

    @Query("SELECT l FROM LcOfferMstEntity l " +
            "WHERE :currentDate >= l.startDate AND :currentDate <= l.endDate " +
            "ORDER BY l.tCreatedAt ")
    List<LcOfferMstEntity> findOffersCount(@Param("currentDate") LocalDateTime currentDate);

}
