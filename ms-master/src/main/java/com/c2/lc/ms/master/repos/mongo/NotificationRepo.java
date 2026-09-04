package com.c2.lc.ms.master.repos.mongo;

import com.c2.lc.ms.master.entities.mongo.LcNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("NotificationRepository")
public interface NotificationRepo extends MongoRepository<LcNotification, String> {

    @Query("{n_user_id: ?0,n_firm_id: ?1,n_branch_id: ?2, c_status: {$ne: D}}")
    List<LcNotification> getUnDeleted(Long userId, Long firmId, Long branchId);

    @Query("{n_user_id: ?0,n_firm_id: ?1,n_branch_id: ?2, c_status: {$eq: A}}")
    List<LcNotification> getUnread(Long userId, Long firmId, Long branchId);

    @Query("{n_user_id: ?0,n_firm_id: ?1,n_branch_id: ?2}")
    List<LcNotification> getAll(Long userId, Long firmId, Long branchId);
}
