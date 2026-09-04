package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("FeedbackRepository")
public interface FeedbackRepo extends JpaRepository<FeedbackEntity, Long> {

//    @Query("select f from FeedbackEntity f where f.nDistributorId = :distributorId" )
//    List<FeedbackEntity> findByDistributorId(@Param("distributorId") Long distributorId);
}
