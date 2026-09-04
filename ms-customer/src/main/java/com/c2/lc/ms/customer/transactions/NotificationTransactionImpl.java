package com.c2.lc.ms.customer.transactions;


import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.NotificationEntity;
import com.c2.lc.ms.customer.services.interfaces.NotificationService;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.NotificationTransaction;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class NotificationTransactionImpl extends LcBaseTransactionImpl implements NotificationTransaction {

    @Autowired private NotificationService notificationService;

    @Override
    public int count(Long userId) {
        return notificationService.count(userId);
    }

    @Override
    public List<NotificationEntity> list(Long notificationId, int page, int size) {
        return notificationService.list(notificationId, page, size);
    }

    @Override
    public void read(Long notificationId) throws RecordNotFoundException {
        notificationService.read(notificationId);
    }

    @Override
    public void readAll(Long userId) throws RecordNotFoundException {
        notificationService.readAll(userId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Long notificationId) throws RecordNotFoundException {
        notificationService.delete(notificationId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteAll(Long userId) throws RecordNotFoundException {
        notificationService.deleteAll(userId);
    }

    @Override
    public void saveNotification(NotificationEntity notificationEntity) throws IOException, URISyntaxException, StorageException {
        notificationService.saveNotification(notificationEntity);
    }

}
