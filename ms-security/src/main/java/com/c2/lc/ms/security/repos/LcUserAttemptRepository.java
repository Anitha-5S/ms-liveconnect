
package com.c2.lc.ms.security.repos;

import com.c2.lc.ms.security.entities.LcUserAttemptsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("LcUserAttemptsRepository")
public interface LcUserAttemptRepository extends JpaRepository<LcUserAttemptsEntity, Long> {

    @Query(value="SELECT p.* from lc_user_attempt p where p.c_user_id=:c_user_id",nativeQuery = true)
    LcUserAttemptsEntity findByUserName(@Param("c_user_id") String c_user_id);
}

