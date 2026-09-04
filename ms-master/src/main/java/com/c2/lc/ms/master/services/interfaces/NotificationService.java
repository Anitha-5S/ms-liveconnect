package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.entities.mongo.LcNotification;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface NotificationService extends MasterBaseService {

    int count(LcHeaderBO headerBO);

    List<LcNotification> list(LcHeaderBO headerBO, PageBO pageBO);

    void read(String notificationId) throws RecordNotFoundException;

    void readAll(LcHeaderBO header) throws RecordNotFoundException;

    void delete(Long notificationId) throws RecordNotFoundException;

    void deleteAll(LcHeaderBO headerBO) throws RecordNotFoundException;

    void saveNotification(LcNotification lcNotification, LcHeaderBO header) throws IOException, URISyntaxException, StorageException;
}
