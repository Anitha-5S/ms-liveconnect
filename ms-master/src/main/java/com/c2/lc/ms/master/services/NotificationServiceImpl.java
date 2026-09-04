package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mongo.LcNotification;
import com.c2.lc.ms.master.repos.mongo.NotificationRepo;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.NotificationService;
import com.microsoft.azure.storage.StorageException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl extends MasterBaseServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public int count(LcHeaderBO headerBO){
        Criteria criteria = Criteria.where("n_user_id").is(headerBO.getUserId()).and("n_firm_id").is(headerBO.getFirmId()).and("n_branch_id").is(headerBO.getFirmId()).and("c_status").ne("D");

        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        return mongoTemplate.find(query, LcNotification.class).size();
    }

    @Override
    public List<LcNotification> list(LcHeaderBO headerBO, PageBO pageBO) {
        Criteria criteria = Criteria.where("n_user_id").is(headerBO.getUserId()).and("n_firm_id").is(headerBO.getFirmId()).and("n_branch_id").is(headerBO.getFirmId()).and("c_status").ne("D");

        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        query.limit(pageBO.getLimit());
        query.skip((long) pageBO.getPage() *pageBO.getLimit());
        query.with(Sort.by(Sort.Direction.DESC, "t_created_timestamp"));
        return mongoTemplate.find(query, LcNotification.class);
//        return notificationRepo.getUnDeleted(headerBO.getUserId(), headerBO.getFirmId(), headerBO.getBranchId());
    }

    @Override
    public void read(String notificationId) throws RecordNotFoundException {
        Optional<LcNotification> entity = notificationRepo.findById(notificationId);
        if (entity.isEmpty()) {
            throw new RecordNotFoundException(notificationId, "Record not found!");
        }
        entity.get().setStatus("R");
        entity.get().setReadTimeStamp(helper.getCurrentTime());
//        notificationRepo.deleteById(notificationId);
        notificationRepo.save(entity.get());
    }

    @Override
    public void readAll(LcHeaderBO header) throws RecordNotFoundException {
        List<LcNotification> lcNotifications = notificationRepo.getUnread(header.getUserId(), header.getFirmId(), header.getFirmId());
        if (lcNotifications.isEmpty()) {
            throw new RecordNotFoundException("Record not found");
        }
        for (LcNotification entity : lcNotifications) {
            System.out.println(entity.getNotificationId());
            entity.setStatus("R");
            entity.setReadTimeStamp(helper.getCurrentTime());
            notificationRepo.save(entity);
        }
    }

    @Override
    public void delete(Long notificationId) throws RecordNotFoundException {
//        NotificationEntity entity = notificationRepo.findById(notificationId).orElse(null);
//        if (entity == null) {
//            throw new RecordNotFoundException(notificationId, "Record not found!");
//        }
//        entity.setStatus("D");
//        entity.setDeletedTimeStamp(helper.getCurrentTime());
//        notificationRepo.save(entity);
    }

    @Override
    public void deleteAll(LcHeaderBO header) throws RecordNotFoundException {
        List<LcNotification> lcNotifications = notificationRepo.getAll(header.getUserId(), header.getFirmId(), header.getFirmId());
        if (lcNotifications.isEmpty()) {
            throw new RecordNotFoundException("Record not found");
        }
        for (LcNotification entity : lcNotifications) {
            entity.setStatus("D");
            entity.setDeletedTimeStamp(helper.getCurrentTime());
            notificationRepo.save(entity);
        }
    }

    @Override
    public void saveNotification(LcNotification entity, LcHeaderBO header) throws IOException, URISyntaxException, StorageException {
        entity.setCreatedTimeStamp(helper.getCurrentTime());
        entity.setNotificationId(new ObjectId().toString());
        entity.setStatus("A");
        entity.setUserId(header.getUserId());
        entity.setFirmId(header.getFirmId());
        entity.setBrCode(header.getFirmId());
        notificationRepo.save(entity);
    }

}
