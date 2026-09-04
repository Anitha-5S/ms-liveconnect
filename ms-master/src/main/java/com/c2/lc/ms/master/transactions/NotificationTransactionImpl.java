package com.c2.lc.ms.master.transactions;


import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mongo.LcNotification;
import com.c2.lc.ms.master.services.interfaces.NotificationService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.NotificationTransaction;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class NotificationTransactionImpl extends MasterBaseTransactionImpl implements NotificationTransaction {

    @Autowired private NotificationService notificationService;

    @Override
    public int count(LcHeaderBO headerBo) {
        return notificationService.count(headerBo);
    }

    @Override
    public List<LcNotification> list(LcHeaderBO headerBO, PageBO pageBO) {
        return notificationService.list(headerBO, pageBO);
    }

    @Override
    public void read(String notificationId) throws RecordNotFoundException {
        notificationService.read(notificationId);
    }

    @Override
    public void readAll(LcHeaderBO header) throws RecordNotFoundException {
        notificationService.readAll(header);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void delete(Long notificationId) throws RecordNotFoundException {
        notificationService.delete(notificationId);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteAll(LcHeaderBO headerBO) throws RecordNotFoundException {
        notificationService.deleteAll(headerBO);
    }

    @Override
    public void saveNotification(LcNotification lcNotification, LcHeaderBO header) throws IOException, URISyntaxException, StorageException {
        notificationService.saveNotification(lcNotification, header);
    }

}
