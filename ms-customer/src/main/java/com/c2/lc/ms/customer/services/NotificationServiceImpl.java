package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.NotificationEntity;
import com.c2.lc.ms.customer.repos.customer.NotificationRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.NotificationService;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class NotificationServiceImpl extends LcBaseServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepo notificationRepo;

    @Override
    public int count(Long userId){
        return notificationRepo.getCount(userId);
    }

    @Override
    public List<NotificationEntity> list(Long userId, int page, int size) {
        Pageable pageableRequest = PageRequest.of(page, size);
        Page<NotificationEntity> list = notificationRepo.list(userId, pageableRequest);
        return list.getContent();
    }

    @Override
    public void read(Long notificationId) throws RecordNotFoundException {
        NotificationEntity entity = notificationRepo.findById(notificationId).orElse(null);
        if (entity == null) {
            throw new RecordNotFoundException(notificationId, "Record not found!");
        }
        entity.setStatus("R");
        entity.setReadTimeStamp(helper.getCurrentTime());
        notificationRepo.save(entity);
    }

    @Override
    public void readAll(Long userId) throws RecordNotFoundException {
        NotificationEntity entity = notificationRepo.findById(userId).orElse(null);
        if (entity == null) {
            throw new RecordNotFoundException(userId, "Record not found!");
        }
        //entity.setStatus("R");
        entity.setReadTimeStamp(helper.getCurrentTime());
        notificationRepo.markAll(userId);
        notificationRepo.save(entity);
    }

    @Override
    public void delete(Long notificationId) throws RecordNotFoundException {
        NotificationEntity entity = notificationRepo.findById(notificationId).orElse(null);
        if (entity == null) {
            throw new RecordNotFoundException(notificationId, "Record not found!");
        }
        entity.setStatus("D");
        entity.setDeletedTimeStamp(helper.getCurrentTime());
        notificationRepo.save(entity);
    }

    @Override
    public void deleteAll(Long userId) throws RecordNotFoundException {
        NotificationEntity entity = notificationRepo.findById(userId).orElse(null);
        if (entity == null) {
            throw new RecordNotFoundException(userId, "Record not found!");
        }
        // entity.setStatus("D");
        entity.setDeletedTimeStamp(helper.getCurrentTime());
        notificationRepo.clearAll(userId);
        notificationRepo.save(entity);
    }

    @Override
    public void saveNotification(NotificationEntity entity) throws IOException, URISyntaxException, StorageException {
        entity.setCreatedTimeStamp(helper.getCurrentTime());
        entity.setReadTimeStamp(helper.getCurrentTime());
        entity.setDeletedTimeStamp(helper.getCurrentTime());
        notificationRepo.save(entity);
    }

}
