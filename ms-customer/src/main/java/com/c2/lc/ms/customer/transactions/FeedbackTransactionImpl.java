package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.FeedbackEntity;
import com.c2.lc.ms.customer.services.interfaces.FeedbackService;
import com.c2.lc.ms.customer.services.interfaces.FirmService;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.FeedbackTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class FeedbackTransactionImpl extends LcBaseTransactionImpl implements FeedbackTransaction {
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    FirmService firmService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveFeedback(Long userId, Long firmId, FeedbackEntity feedbackEntity) {
        feedbackService.saveFeedback(userId, firmId, feedbackEntity);
    }

//    @Override
//    public List<FeedbackEntity> getFeedbackByDistributorId(Long distributorId) throws RecordNotFoundException {
//        return feedbackService.getFeedbackByDistributorId(distributorId);
//    }

    @Override
    public JsonObject uploadDocument(Long userId, Long firmId, JsonObject json) throws StorageException, IOException, URISyntaxException {
        return feedbackService.uploadDocument(userId, firmId, json);
    }

    @Override
    public JsonArray getListDistributor(String mobileNo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        return feedbackService.getListDistributor(mobileNo,lcHeaderBO);
    }
}
