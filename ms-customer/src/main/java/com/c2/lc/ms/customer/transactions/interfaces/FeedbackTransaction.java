package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.FeedbackEntity;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FeedbackTransaction extends LcBaseTransaction {
    void saveFeedback(Long userId, Long firmId, FeedbackEntity feedbackEntity);

//    List<FeedbackEntity> getFeedbackByDistributorId(Long distributorId) throws RecordNotFoundException;

    JsonObject uploadDocument(Long userId, Long firmId, JsonObject json) throws StorageException, IOException, URISyntaxException;

    JsonArray getListDistributor(String mobileNo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException;
}
