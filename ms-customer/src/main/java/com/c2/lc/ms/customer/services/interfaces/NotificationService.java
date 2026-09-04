package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.NotificationEntity;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface NotificationService extends LcBaseService {

    int count(Long userId);

    List<NotificationEntity> list(Long userId, int page, int size);

    void read(Long notificationId) throws RecordNotFoundException;

    void readAll(Long userId) throws RecordNotFoundException;

    void delete(Long notificationId) throws RecordNotFoundException;

    void deleteAll(Long userId) throws RecordNotFoundException;

    void saveNotification(NotificationEntity notificationEntity) throws IOException, URISyntaxException, StorageException;
}
