package com.c2.lc.ms.customer.repos.comm;


import com.c2.lc.ms.customer.entities.comm.EcoAuthSession;
import com.c2.lc.ms.customer.entities.comm.EcoAuthSessionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EcoAuthSessionRepository extends JpaRepository<EcoAuthSession, EcoAuthSessionPK> {

    @Query("SELECT es FROM EcoAuthSession es WHERE es.id.c2Code = :c2Code AND es.id.brCode = :brCode AND es.id.terminalId = :terminalId AND es.id.type = :type")
    List<EcoAuthSession> findUserSessions(String c2Code, String brCode, String terminalId, String type);
}