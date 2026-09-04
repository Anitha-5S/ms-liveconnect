package com.c2.lc.ms.customer.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.FeedbackEntity;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FeedbackService extends LcBaseService {
    void saveFeedback(Long userId, Long firmId, FeedbackEntity feedbackEntity);

//    List<FeedbackEntity> getFeedbackByDistributorId(Long distributorId) throws RecordNotFoundException;

    JsonObject uploadDocument(Long userId, Long firmId, JsonObject data) throws StorageException, IOException, URISyntaxException;

    JsonArray getListDistributor(String mobileNo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;
}
