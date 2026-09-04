package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntity;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntityPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OnePharmaEmailsRepository extends JpaRepository<OnePharmaEmailsEntity, OnePharmaEmailsEntityPK> {

    @Query("SELECT o FROM OnePharmaEmailsEntity o WHERE o.cEmail = :emailId")
    OnePharmaEmailsEntity findByEmailId(@Param("emailId") String emailId);

}
