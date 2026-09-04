package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UGeoStateMstEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<UGeoStateMstEntity, String> {

    @Query("SELECT usm FROM UGeoStateMstEntity usm WHERE usm.cName LIKE :state%")
    List<UGeoStateMstEntity> findByName(@Param("state")String searchString);
}
